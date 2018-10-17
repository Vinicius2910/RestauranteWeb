package br.com.sistemaTCC.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.sistemaTCC.model.Cliente;
import br.com.sistemaTCC.model.Lojista;
import br.com.sistemaTCC.model.Parametro;
import br.com.sistemaTCC.model.Pontuacao;
import br.com.sistemaTCC.model.Premio;
import br.com.sistemaTCC.model.Transacao;
import br.com.sistemaTCC.repository.Clientes;
import br.com.sistemaTCC.repository.Lojistas;
import br.com.sistemaTCC.repository.Parametros;
import br.com.sistemaTCC.repository.Pontuacoes;
import br.com.sistemaTCC.repository.Premios;
import br.com.sistemaTCC.repository.Transacoes;

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
	@Autowired
	private Lojistas lojistas;
	@Autowired
	private Transacoes transacoes;
	
	@RequestMapping("/pontos/{cnpj}")
	public ModelAndView pontos(@PathVariable Long cnpj ){
	   ModelAndView mv = new ModelAndView("Cliente");
	   Lojista lojista = lojistas.getOne(cnpj);
	   mv.addObject("lojista", lojista);
	   return mv;
	}

	@RequestMapping("/cadastrarCliente/{cnpj}")
	public ModelAndView cadastrarCliente(@PathVariable Long cnpj ,Cliente cliente){
		Lojista lojista = lojistas.getOne(cnpj);
		clientes.save(cliente);		
		List<Parametro> listParametros = parametros.findAll();
		Parametro parametro = encontrarParametro(listParametros, cnpj);
		Date now = new Date();
		Transacao transacao = new Transacao();
		transacao.setClienteID(cliente);
		transacao.setLojistaID(lojista);
		transacao.setData(now);
		
		if(parametro.getTipo() == 1){
			cliente.setPontuacao(5);
			clientes.save(cliente);
			transacao.setPontos(5);
			transacao.setPontuacaoTotal(cliente.getPontuacao());
			transacoes.save(transacao);
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
		mv.addObject("lojista", lojista);
		List<Premio> todosPremios = premios.findAll();
		List<Premio> premioPorId  = filtrarPorID(todosPremios, cnpj);
		mv.addObject("premios", premioPorId);
		List<Pontuacao> todosPontuacao= pontuacoes.findAll();
		List<Pontuacao> pontuacaoPendente = verficaPendente(todosPontuacao, cliente.getCpf());
		mv.addObject("pontuacaoPendente", pontuacaoPendente);
		return mv;
	}
	
	@RequestMapping("/cliente/{cnpj}")
	private ModelAndView cliente(@PathVariable Long cnpj ,Long cpf){
		boolean existe = clientes.existsById(cpf);
		Cliente cliente = new Cliente();
		Lojista lojista = lojistas.getOne(cnpj);
		Date now = new Date();
		List<Parametro> listParametros = parametros.findAll();
		Transacao transacao = new Transacao();
		transacao.setLojistaID(lojista);
		transacao.setData(now);
		
		if(existe == true){
			Parametro parametro = encontrarParametro(listParametros, cnpj);
			if(parametro.getTipo() == 1){
				cliente = clientes.getOne(cpf);
				int pontuacao = cliente.getPontuacao();
				pontuacao += 5;
				cliente.setPontuacao(pontuacao);
				clientes.save(cliente);
				transacao.setClienteID(cliente);
				transacao.setPontos(5);
				transacao.setPontuacaoTotal(cliente.getPontuacao());
				transacoes.save(transacao);
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
			mv.addObject("lojista", lojista);
			List<Premio> todosPremios = premios.findAll();
			List<Premio> premioPorId  = filtrarPorID(todosPremios, cnpj);
			mv.addObject("premios", premioPorId);
			List<Pontuacao> todosPontuacao= pontuacoes.findAll();
			List<Pontuacao> pontuacaoPendente = verficaPendente(todosPontuacao, cliente.getCpf());
			mv.addObject("pontuacaoPendente", pontuacaoPendente);
			return mv;
		}
		else{
			ModelAndView mv = new ModelAndView("ManterCliente");
			cliente.setCpf(cpf);
			mv.addObject("cliente", cliente);
			mv.addObject("lojista", lojista);
			return mv;
		}
	}
	
	@RequestMapping("/redirectCliente/{cnpj}")
	private ModelAndView redirectCliente(@PathVariable Long cnpj ,Long cpf){
		boolean existe = clientes.existsById(cpf);
		Cliente cliente = new Cliente();
		Lojista lojista = lojistas.getOne(cnpj);
		cliente = clientes.getOne(cpf);

		if(existe == true){
			ModelAndView mv= new ModelAndView("PontuacaoCliente");
			mv.addObject("cliente", cliente);
			mv.addObject("lojista", lojista);
			List<Premio> todosPremios = premios.findAll();
			List<Premio> premioPorId  = filtrarPorID(todosPremios, cnpj);
			mv.addObject("premios", premioPorId);
			List<Pontuacao> todosPontuacao= pontuacoes.findAll();
			List<Pontuacao> pontuacaoPendente = verficaPendente(todosPontuacao, cliente.getCpf());
			mv.addObject("pontuacaoPendente", pontuacaoPendente);
			return mv;
		}
		else{
			ModelAndView mv = new ModelAndView("ManterCliente");
			cliente.setCpf(cpf);
			mv.addObject("cliente", cliente);
			mv.addObject("lojista", lojista);
			return mv;
		}
	}

	
	@RequestMapping("/clienteGaleria/{cnpj}")
	private ModelAndView clienteGaleria(@PathVariable String cnpj){
		String[] ids = cnpj.split("-");
		Long cnpjLojista = Long.valueOf(ids[0]);
		Long cpfCliente = Long.valueOf(ids[1]);
		
		boolean existe = clientes.existsById(cpfCliente);
		Cliente cliente = new Cliente();
		Lojista lojista = lojistas.getOne(cnpjLojista);
		cliente = clientes.getOne(cpfCliente);

		if(existe == true){
			List<Transacao> listTransacao = transacoes.findAll();
			List<Transacao> listTransacaoPorId = encontrarTransacao(listTransacao, cnpjLojista);
			int pontosPorLojista = contarPontos(listTransacaoPorId, cnpjLojista);
					
			ModelAndView mv= new ModelAndView("GaleriaPremioCliente");
			mv.addObject("pontosPorLojista", pontosPorLojista);
			mv.addObject("cliente", cliente);
			mv.addObject("lojista", lojista);
			List<Premio> todosPremios = premios.findAll();
			List<Premio> premioPorId  = filtrarPorID(todosPremios, cnpjLojista);
			mv.addObject("premios", premioPorId);
			List<Pontuacao> todosPontuacao= pontuacoes.findAll();
			List<Pontuacao> pontuacaoPendente = verficaPendente(todosPontuacao, cliente.getCpf());
			mv.addObject("pontuacaoPendente", pontuacaoPendente);
			return mv;
		}
		else{
			ModelAndView mv = new ModelAndView("ManterCliente");
			cliente.setCpf(cpfCliente);
			mv.addObject("cliente", cliente);
			mv.addObject("lojista", lojista);
			return mv;
		}
	}

	@RequestMapping("/consultarClientes/{cnpj}")
	private ModelAndView consultarCliente(@PathVariable Long cnpj){
		Lojista lojista = lojistas.getOne(cnpj);
		ModelAndView mv= new ModelAndView("ConsultarClientes");
		List<Cliente> cliente = clientes.findAll();
		mv.addObject("cliente", cliente);
		mv.addObject("lojista", lojista);
		return mv;
	}
	
	@RequestMapping("/extrato/{cnpj}")
	private ModelAndView extratoCliente(@PathVariable String cnpj){
		String[] ids = cnpj.split("-");
		Long cnpjLojista = Long.valueOf(ids[0]);
		Long cpfCliente = Long.valueOf(ids[1]);
		
		List <Transacao> listTransacao = transacoes.findAll(); 
		List <Transacao> transacoes = puxarExtrato(listTransacao, cnpjLojista, cpfCliente);
		Lojista lojista = lojistas.getOne(cnpjLojista);
		Cliente cliente = clientes.getOne(cpfCliente);
		ModelAndView mv= new ModelAndView("ExtratoCliente");
		mv.addObject("transacoes", transacoes);
		mv.addObject("cliente", cliente);
		mv.addObject("lojista", lojista);
		return mv;
	}
	
	@RequestMapping("/mediaClientes/{cnpj}")
	private ModelAndView MediaCliente(@PathVariable Long cnpj){
		Lojista lojista = lojistas.getOne(cnpj);
		ModelAndView mv= new ModelAndView("MediaClientes");
		List<Cliente> cliente = clientes.findAll();
		if(cliente.size() > 1){
			double media =  media(cliente);
			int totalClientes = cliente.size();
			mv.addObject("media", media);
			mv.addObject("totalClientes", totalClientes);
			mv.addObject("lojista", lojista);
		}
		else{
			mv.addObject("media", 0);
			mv.addObject("totalClientes", 0);
			mv.addObject("lojista", lojista);
		}
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
	
	public List<Premio> filtrarPorID(List<Premio> todasPromocoes, Long cpf){
		List<Premio> porCpf = new ArrayList<Premio>();
		for(int i =0; i<todasPromocoes.size(); i++){
			if(todasPromocoes.get(i).getLojistaID().getCpfCNPJ() == cpf){
				porCpf.add(todasPromocoes.get(i));
			}
		}
		return porCpf;
	}
	
	public boolean verificarPorLojistaId(List<Parametro> todosParametros, Long cpf){
		boolean exist = false;
		for(int i=0; i<todosParametros.size();i++){
			System.out.println("esse id ["+todosParametros.get(i).getLojistaID().getCpfCNPJ()+"] é igual? --" +cpf);
			if(todosParametros.get(i).getLojistaID().getCpfCNPJ().toString().trim().equalsIgnoreCase(cpf.toString().trim())){
				exist = true;
			}
		}
		return exist;
	}
	
	public Parametro encontrarParametro(List<Parametro> todosParametros, Long cpf){
		Parametro parametro = null;
		for(int i=0; i<todosParametros.size();i++){
			if(todosParametros.get(i).getLojistaID().getCpfCNPJ().toString().trim().equalsIgnoreCase(cpf.toString().trim())){
				parametro = todosParametros.get(i);
				break;
			}
		}
		return parametro;
	}

	public List<Transacao> encontrarTransacao(List<Transacao> todasTransacao, Long cnpj){
		List<Transacao> transacao= new ArrayList<Transacao>();
		for(int i=0; i<todasTransacao.size();i++){
			if(todasTransacao.get(i).getLojistaID().getCpfCNPJ().toString().trim().equalsIgnoreCase(cnpj.toString().trim())){
				transacao.add(todasTransacao.get(i));
				break;
			}
		}
		return transacao;
	}
	
	public List<Transacao> puxarExtrato(List<Transacao> todasTransacao, Long cnpj, Long cpf){
		List<Transacao> transacao= new ArrayList<Transacao>();
		for(int i=0; i<todasTransacao.size();i++){
			if(todasTransacao.get(i).getLojistaID().getCpfCNPJ().toString().trim().equalsIgnoreCase(cnpj.toString().trim())){
				if(todasTransacao.get(i).getClienteID().getCpf().toString().trim().equalsIgnoreCase(cpf.toString().trim())){
					transacao.add(todasTransacao.get(i));
				}
			}
		}
		return transacao;
	}
	
	public int contarPontos(List<Transacao> todasTransacao, Long cnpj){
		int pontos = 0;
		for(int i=0; i<todasTransacao.size();i++){
			if(todasTransacao.get(i).getLojistaID().getCpfCNPJ().toString().trim().equalsIgnoreCase(cnpj.toString().trim())){
				pontos += todasTransacao.get(i).getPontos();
				break;
			}
		}
		return pontos;
	}
}
