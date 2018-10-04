package br.com.sistemaTCC.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.sistemaTCC.model.Cliente;
import br.com.sistemaTCC.model.Parametro;
import br.com.sistemaTCC.model.Pontuacao;
import br.com.sistemaTCC.model.Premio;
import br.com.sistemaTCC.repository.Clientes;
import br.com.sistemaTCC.repository.Parametros;
import br.com.sistemaTCC.repository.Pontuacoes;
import br.com.sistemaTCC.repository.Premios;

@Controller
public class ClienteController {
	
	@Autowired
	private Clientes clientes;
	@Autowired
	private Premios premios;
	@Autowired
	private Parametros parametros;
	@Autowired
	private Pontuacoes pontuacoes;
	
	@RequestMapping("/pontos")
	public String pontos(){
	   return "Cliente";
	}

	@RequestMapping("/cadastrarCliente")
	public ModelAndView cadastrarCliente(Cliente cliente){
		
		clientes.save(cliente);
		Parametro parametro = parametros.getOne(1);
		Date now = new Date();
		
		if(parametro.getTipo() == 1){
			cliente.setPontuacao(5);
			clientes.save(cliente);
		}
		else{
			Pontuacao pontucaoCliente = new Pontuacao();
			pontucaoCliente.setCpf(cliente.getCpf());
			pontucaoCliente.setData(now);
			pontucaoCliente.setStatus("PENDENTE");
			pontuacoes.save(pontucaoCliente);
		}
		
		ModelAndView mv= new ModelAndView("PontuacaoCliente");
		mv.addObject("cliente", cliente);
		List<Premio> todosPremios = premios.findAll();
		mv.addObject("premios", todosPremios);
		List<Pontuacao> todosPontuacao= pontuacoes.findAll();
		List<Pontuacao> pontuacaoPendente = verficaPendente(todosPontuacao, cliente.getCpf());
		mv.addObject("pontuacaoPendente", pontuacaoPendente);
		return mv;
	}
	
	@RequestMapping("/cliente")
	private ModelAndView cliente(Long cpf){
		
		boolean existe = clientes.existsById(cpf);
		Cliente cliente = new Cliente();
		Date now = new Date();
		
		if(existe == true){
			Parametro parametro = parametros.getOne(1);
			if(parametro.getTipo() == 1){
				cliente = clientes.getOne(cpf);
				int pontuacao = cliente.getPontuacao();
				pontuacao += 5;
				cliente.setPontuacao(pontuacao);
				clientes.save(cliente);
			}
			else{
				cliente = clientes.getOne(cpf);
				Pontuacao pontucaoCliente = new Pontuacao();
				pontucaoCliente.setCpf(cpf);
				pontucaoCliente.setData(now);
				pontucaoCliente.setStatus("PENDENTE");
				pontuacoes.save(pontucaoCliente);
			}
			
			ModelAndView mv= new ModelAndView("PontuacaoCliente");
			mv.addObject("cliente", cliente);
			List<Premio> todosPremios = premios.findAll();
			mv.addObject("premios", todosPremios);
			List<Pontuacao> todosPontuacao= pontuacoes.findAll();
			List<Pontuacao> pontuacaoPendente = verficaPendente(todosPontuacao, cliente.getCpf());
			mv.addObject("pontuacaoPendente", pontuacaoPendente);
			return mv;
		}
		else{
			ModelAndView mv = new ModelAndView("ManterCliente");
			cliente.setCpf(cpf);
			mv.addObject("cliente", cliente);
			return mv;
		}
	}
	
	@RequestMapping("/consultarClientes")
	private ModelAndView consultarCliente(){
		ModelAndView mv= new ModelAndView("ConsultarClientes");
		List<Cliente> cliente = clientes.findAll();
		mv.addObject("cliente", cliente);
		return mv;
	}
	
	@RequestMapping("/mediaClientes")
	private ModelAndView MediaCliente(){
		ModelAndView mv= new ModelAndView("MediaClientes");
		List<Cliente> cliente = clientes.findAll();
		double media =  media(cliente);
		int totalClientes = cliente.size();
		mv.addObject("media", media);
		mv.addObject("totalClientes", totalClientes);
		return mv;
	}
	
	private List<Pontuacao> verficaPendente(List<Pontuacao> todosPontuacao, Long cpf){
		List<Pontuacao> pontuacaoPendente = new ArrayList<Pontuacao>();
		for(int i=0; i<todosPontuacao.size(); i++){
			if(todosPontuacao.get(i).getStatus().trim().equalsIgnoreCase("PENDENTE") && todosPontuacao.get(i).getCpf() == cpf){
				pontuacaoPendente.add(todosPontuacao.get(i));
			}
		}
		return pontuacaoPendente;
	}
	
	private double media(List<Cliente> todosClientes)
	{
		int soma=0;
		for(int i=0; i < todosClientes.size(); i++){
			soma += todosClientes.get(i).getPontuacao();
		}
		
		double media = soma/todosClientes.size();
		return media;
	}
}
