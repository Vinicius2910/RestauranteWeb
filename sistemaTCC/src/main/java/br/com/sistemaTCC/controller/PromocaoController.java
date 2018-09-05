package br.com.sistemaTCC.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import br.com.sistemaTCC.model.Promocao;
import br.com.sistemaTCC.repository.Promocoes;

@Controller
public class PromocaoController {
	
	@Autowired
	private Promocoes promocoes;
	
	@RequestMapping("/promocoes")
	public ModelAndView tabelaPromocoes() {
		List<Promocao> todasPromocoes = promocoes.findAll();
		ModelAndView mv= new ModelAndView("Promocoes");
		mv.addObject("promocoes", todasPromocoes);
		return mv;
	}
	
	@RequestMapping(value ="/novaPromocao",method= RequestMethod.POST)
	public String salvar(Promocao promocao){
		promocoes.save(promocao);
		return "redirect:/promocoes";	
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
		 return "redirect:/promocoes";	
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
	
}
