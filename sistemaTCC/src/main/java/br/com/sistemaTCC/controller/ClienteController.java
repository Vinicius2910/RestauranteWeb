package br.com.sistemaTCC.controller;

import java.util.ArrayList;
import java.util.Calendar;
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
	
	@RequestMapping("/cliente")
	private ModelAndView cliente(Long cpf){
		
		boolean existe = clientes.existsById(cpf);
		Cliente cliente = new Cliente();
		
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
				pontucaoCliente.setData(Calendar.getInstance());
				pontucaoCliente.setStatus("PENDENTE");
				pontuacoes.save(pontucaoCliente);
			}
		}
		else{
			cliente.setCpf(cpf);
			clientes.save(cliente);
			Parametro parametro = parametros.getOne(1);
			
			if(parametro.getTipo() == 1){
				cliente.setPontuacao(5);
				clientes.save(cliente);
			}
			else{
				Pontuacao pontucaoCliente = new Pontuacao();
				pontucaoCliente.setCpf(cpf);
				pontucaoCliente.setData(Calendar.getInstance());
				pontucaoCliente.setStatus("PENDENTE");
				pontuacoes.save(pontucaoCliente);
			}
		}
		
		ModelAndView mv= new ModelAndView("PontuacaoCliente");
		mv.addObject("cliente", cliente);
		List<Premio> todosPremios = premios.findAll();
		mv.addObject("premios", todosPremios);
		List<Pontuacao> todosPontuacao= pontuacoes.findAll();
		List<Pontuacao> pontuacaoPendente = verficaPendente(todosPontuacao);
		mv.addObject("pontuacaoPendente", pontuacaoPendente);
		return mv;
	}
	
	private List<Pontuacao> verficaPendente(List<Pontuacao> todosPontuacao){
		List<Pontuacao> pontuacaoPendente = new ArrayList<Pontuacao>();
		for(int i=0; i<todosPontuacao.size(); i++){
			if(todosPontuacao.get(i).getStatus() =="PENDENTE"){
				pontuacaoPendente.add(todosPontuacao.get(i));
			}
		}
		
		return pontuacaoPendente;
	}

}
