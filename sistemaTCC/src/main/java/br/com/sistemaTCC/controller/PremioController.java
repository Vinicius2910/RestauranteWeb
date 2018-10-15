package br.com.sistemaTCC.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sistemaTCC.model.Cliente;
import br.com.sistemaTCC.model.Lojista;
import br.com.sistemaTCC.model.Premio;
import br.com.sistemaTCC.model.Transacao;
import br.com.sistemaTCC.repository.Clientes;
import br.com.sistemaTCC.repository.Lojistas;
import br.com.sistemaTCC.repository.Premios;
import br.com.sistemaTCC.repository.Transacoes;

@Controller
public class PremioController {
	
	@Autowired
	private Premios premios;
	@Autowired
	private Lojistas lojistas;
	@Autowired
	private Clientes clientes;
	@Autowired
	private Transacoes transacoes;
	
	@RequestMapping("/premiosPorId/{cpf}")
	public ModelAndView tabelaPremios(@PathVariable Long cpf) {
		Lojista lojista =  lojistas.getOne(cpf);
		List<Premio> todosPremios = premios.findAll();
		List<Premio> promocaoPorId  = filtrarPorID(todosPremios, cpf);
		ModelAndView mv= new ModelAndView("ManterPremio");
		mv.addObject("premios", promocaoPorId);
		mv.addObject("lojista", lojista);
		return mv;
	}
	
	@RequestMapping(value ="/novoPremio/{cpf}",method= RequestMethod.POST)
	public String salvar(@PathVariable Long cpf, Premio premio){
		Lojista lojista = lojistas.getOne(cpf);
		premio.setLojistaID(lojista);
		premios.save(premio);
		return "redirect:/premiosPorId/"+cpf;	
	}

	@RequestMapping("/resgatarPremio/{codigo}")
	public ModelAndView resgatarPremio(@PathVariable String codigo){
		String ids = codigo.toString(); 
		String [] listId = ids.split("-");
		
		Long cpf = Long.valueOf(listId[0]);
		Long codigo1 = Long.valueOf(listId[1]);
		
		Premio premio = premios.getOne(codigo1);
		Cliente cliente = clientes.getOne(cpf);
		if( cliente.getPontuacao() < premio.getQtdPontos())
		{
			ModelAndView mv = new ModelAndView("FalhouResgatarPremio");
			mv.addObject(premio);
			mv.addObject(cliente);
			return mv;
		}
		else
		{
			ModelAndView mv = new ModelAndView("ResgatarPremio");
			cliente.setPontuacao(cliente.getPontuacao() - premio.getQtdPontos());
			clientes.save(cliente);
			Lojista lojista = lojistas.getOne(premio.getLojistaID().getCpfCNPJ());
			Date now = new Date();
			Transacao transacao = new Transacao();
			transacao.setClienteID(cliente);
			transacao.setLojistaID(lojista);
			transacao.setPontos(-premio.getQtdPontos());
			transacao.setPontuacaoTotal(cliente.getPontuacao());
			transacao.setData(now);
			transacoes.save(transacao);
			mv.addObject(premio);
			mv.addObject(cliente);
			return mv;	
		}	
	}

	@RequestMapping("/premios/{codigo}")
	public ModelAndView edicao(@PathVariable Long codigo){
		Premio premio = premios.getOne(codigo);
		ModelAndView mv = new ModelAndView("EditarPremio");
		mv.addObject(premio);
		return mv;	
	}

	@RequestMapping(value="/deletarPremio",method = RequestMethod.DELETE)
	public String excluir(Premio premio){
		premios.deleteById(premio.getCodigo());
		String cpf = premio.getLojistaID().getCpfCNPJ().toString();
		return "redirect:/premiosPorId/"+cpf;	
	}
	
	@RequestMapping("/deletarPremio/{codigo}")
	public ModelAndView retornaCodigoExclusao(@PathVariable Long codigo){
		Premio premio = premios.getOne(codigo);
		ModelAndView mv = new ModelAndView("ExcluirPremio");
		mv.addObject(premio);
		return mv;	
	}
	
	public List<Premio> filtrarPorID(List<Premio> todasPromocoes, Long cpf){
		List<Premio> porCpf = new ArrayList<Premio>();
		for(int i =0; i<todasPromocoes.size(); i++){
			if(todasPromocoes.get(i).getLojistaID().getCpfCNPJ() == cpf){
				porCpf.add(todasPromocoes.get(i));
			}
		}
		return porCpf;
	}
	
}
