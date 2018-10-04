package br.com.sistemaTCC.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.sistemaTCC.model.Cliente;
import br.com.sistemaTCC.model.Parametro;
import br.com.sistemaTCC.model.Pontuacao;
import br.com.sistemaTCC.repository.Clientes;
import br.com.sistemaTCC.repository.Parametros;
import br.com.sistemaTCC.repository.Pontuacoes;

@Controller
public class PontuacaoController {
	@Autowired
	private Pontuacoes pontuacoes;
	
	@Autowired
	private Parametros parametros;
	
	@Autowired
	private Clientes clientes;
	
	
	@RequestMapping("/caixa")
	public ModelAndView tabelaPontuacaoPendente() {
		List<Pontuacao> todasPontuacoes = pontuacoes.findAll();
		List<Pontuacao> poontuacaoPendente = verficaPendente(todasPontuacoes);  
		ModelAndView mv= new ModelAndView("AprovacaoVisitaCliente");
		mv.addObject("pontuacaoPendente", poontuacaoPendente);
		return mv;
	}
	@RequestMapping(value ="/novaPontuacao",method= RequestMethod.POST)
	public String salvar(Pontuacao pontuacao) throws ParseException{
		pontuacao.setStatus("APROVADO");
		Parametro parametro = parametros.getOne(1);

		double valor = pontuacao.getValor();
		double valorParam =  parametro.getValor();
		int pontuacaoCliente = 0;
		
		while(valor > 0){
			valor = valor - valorParam;
			if(valor > 0)
				pontuacaoCliente += 5;
		}
		
		Cliente cliente = clientes.getOne(pontuacao.getCpf());
		int pontoCliente = cliente.getPontuacao();
		pontoCliente += pontuacaoCliente;
		cliente.setPontuacao(pontoCliente);
		clientes.save(cliente);
		pontuacoes.save(pontuacao);
		return "redirect:/caixa";	
	}

	@RequestMapping("/caixa/{codigo}")
	public ModelAndView edicaoPontuacao(@PathVariable Long codigo){
		Pontuacao pontuacao = pontuacoes.getOne(codigo);
		ModelAndView mv = new ModelAndView("EditarAprovacao");
		mv.addObject(pontuacao);
		return mv;	
	}
	

	private List<Pontuacao> verficaPendente(List<Pontuacao> todosPontuacao){
		List<Pontuacao> pontuacaoPendente = new ArrayList<Pontuacao>();
		for(int i=0; i<todosPontuacao.size(); i++){
			if(todosPontuacao.get(i).getStatus().trim().equalsIgnoreCase("PENDENTE")){
				pontuacaoPendente.add(todosPontuacao.get(i));
			}
		}
		return pontuacaoPendente;
	}

}
