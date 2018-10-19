package br.com.sistemaTCC.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.sistemaTCC.model.Cliente;
import br.com.sistemaTCC.model.Lojista;
import br.com.sistemaTCC.model.Parametro;
import br.com.sistemaTCC.model.Pontuacao;
import br.com.sistemaTCC.model.Transacao;
import br.com.sistemaTCC.repository.Clientes;
import br.com.sistemaTCC.repository.Lojistas;
import br.com.sistemaTCC.repository.Parametros;
import br.com.sistemaTCC.repository.Pontuacoes;
import br.com.sistemaTCC.repository.Transacoes;

@Controller
public class PontuacaoController {
	@Autowired
	private Pontuacoes pontuacoes;
	@Autowired
	private Parametros parametros;
	@Autowired
	private Clientes clientes;
	@Autowired
	private Lojistas lojistas;
	@Autowired
	private Transacoes transacoes;
	
	
	@RequestMapping("/caixa")
	public ModelAndView tabelaPontuacaoPendente() {
		List<Pontuacao> todasPontuacoes = pontuacoes.findAll();
		List<Pontuacao> poontuacaoPendente = verficaPendente(todasPontuacoes);
		ModelAndView mv= new ModelAndView("AprovacaoVisitaCliente");
		mv.addObject("pontuacaoPendente", poontuacaoPendente);
		return mv;
	}
	@RequestMapping(value ="/novaPontuacao/{cnpj}",method= RequestMethod.POST)
	public String salvar(@PathVariable Long cnpj, Pontuacao pontuacao) throws ParseException{
		pontuacao.setStatus("APROVADO");
		List<Parametro> listParametros = parametros.findAll();
		Parametro parametro = encontrarParametro(listParametros, cnpj);

		double valor = pontuacao.getValor();
		double valorParam =  parametro.getValor();
		int pontuacaoCliente = 0;
		
		while(valor > 0){
			valor = valor - valorParam;
			if(valor >= 0)
				pontuacaoCliente += 5;
		}
		Lojista lojista = lojistas.getOne(cnpj);
		Cliente cliente =  clientes.getOne(pontuacao.getCpf());
		int pontoCliente = cliente.getPontuacao();
		pontoCliente += pontuacaoCliente;
		cliente.setPontuacao(pontoCliente);
		
		Date now = new Date();
		Transacao transacao = new Transacao();
		transacao.setLojistaID(lojista);
		transacao.setClienteID(cliente);
		transacao.setPontos(pontuacaoCliente);
		transacao.setPontuacaoTotal(pontoCliente);
		transacao.setData(now);
		
		transacoes.save(transacao);
		clientes.save(cliente);
		pontuacoes.save(pontuacao);
		return "redirect:/caixa";	
	}

	@RequestMapping("/caixa/{codigo}")
	public ModelAndView edicaoPontuacao(@PathVariable String codigo){
		String[] ids =  codigo.split("-");
		Long codigoVal = Long.valueOf(ids[0]);
		Long cnpj = Long.valueOf(ids[1]);
		System.out.println("cod-"+codigo );
		System.out.println("cnpj-"+cnpj);
			
		Lojista lojista = lojistas.getOne(cnpj);
		Pontuacao pontuacao = pontuacoes.getOne(codigoVal);
		ModelAndView mv = new ModelAndView("EditarAprovacao");
		mv.addObject(pontuacao);
		mv.addObject("lojista",lojista);
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

}
