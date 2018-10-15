package br.com.sistemaTCC.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import br.com.sistemaTCC.model.Cliente;
import br.com.sistemaTCC.model.Lojista;
import br.com.sistemaTCC.model.Promocao;
import br.com.sistemaTCC.repository.Clientes;
import br.com.sistemaTCC.repository.Lojistas;
import br.com.sistemaTCC.repository.Promocoes;

@Controller
public class PromocaoController {
	
	@Autowired
	private Promocoes promocoes;
	
	@Autowired
	private Lojistas lojistas;
	
	@Autowired
	private Clientes clientes;
	
	@RequestMapping("/promocoesPorLojista/{cpf}")
	public ModelAndView tabelaPromocoes(@PathVariable Long cpf) {
		Lojista lojista =  lojistas.getOne(cpf);
		List<Promocao> todasPromocoes = promocoes.findAll();
		List<Promocao> promocaoPorId  = filtrarPorID(todasPromocoes, cpf);
		ModelAndView mv= new ModelAndView("Promocoes");
		mv.addObject("promocoes", promocaoPorId);
		mv.addObject("lojista", lojista);
		return mv;
	}
	
	@RequestMapping(value ="/novaPromocao/{cpf}",method= RequestMethod.POST)
	public String salvar(@PathVariable Long cpf, Promocao promocao){
		Lojista lojista = lojistas.getOne(cpf);
		promocao.setLojistaID(lojista);
		promocoes.save(promocao);
		return "redirect:/promocoesPorLojista/"+cpf;	
	}

	@RequestMapping("/promocoes/{codigo}")
	public ModelAndView edicao(@PathVariable Long codigo){
		Promocao promocao = promocoes.getOne(codigo);
		ModelAndView mv = new ModelAndView("EditarPromocao");
		mv.addObject(promocao);
		return mv;	
	}

	@RequestMapping(value="/deletarPromocao",method = RequestMethod.DELETE)
	public String excluir(Promocao promocao){
		 promocoes.deleteById(promocao.getCodigo());
		 String cpf = promocao.getLojistaID().getCpfCNPJ().toString();
		 return "redirect:/promocoesPorLojista/"+ cpf;	
	}
	
	@RequestMapping("/deletarPromocao/{codigo}")
	public ModelAndView retornaCodigoExclusao(@PathVariable Long codigo){
		Promocao promocao = promocoes.getOne(codigo);
		ModelAndView mv = new ModelAndView("ExcluirPromocao");
		mv.addObject(promocao);
		return mv;	
	}
	
	@RequestMapping("/homeLojista")
	public ModelAndView homeLojista(){
		ModelAndView mv = new ModelAndView("HomeLojista");
		return mv;
	}
	
	@RequestMapping("/galeriaPromocoes/{cnpj}")
	private ModelAndView galeriaPromocoes(@PathVariable String cnpj){
		String[] ids = cnpj.split("-");
		Long cnpjLojista = Long.valueOf(ids[0]);
		Long cpfCliente = Long.valueOf(ids[1]);
		
		System.out.println(cnpj);
		List<Promocao> todasPromocoes = promocoes.findAll();
		List<Promocao> promocaoPorId  = filtrarPorID(todasPromocoes, cnpjLojista);
		Lojista lojista = lojistas.getOne(cnpjLojista);
		Cliente cliente = clientes.getOne(cpfCliente);
		ModelAndView mv= new ModelAndView("GaleriaPromocoes");
		mv.addObject("cliente",cliente );
		mv.addObject("promocoes", promocaoPorId);
		mv.addObject("lojista", lojista);
		return mv;
	}
	
	public List<Promocao> filtrarPorID(List<Promocao> todasPromocoes, Long cpf){
		List<Promocao> porCpf = new ArrayList<Promocao>();
		for(int i =0; i<todasPromocoes.size(); i++){
			if(todasPromocoes.get(i).getLojistaID().getCpfCNPJ().toString().trim().equalsIgnoreCase(cpf.toString().trim())){
				porCpf.add(todasPromocoes.get(i));
			}
		}
		return porCpf;
	}
}
