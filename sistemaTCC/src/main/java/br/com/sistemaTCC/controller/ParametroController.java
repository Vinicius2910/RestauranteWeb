package br.com.sistemaTCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.sistemaTCC.model.Parametro;
import br.com.sistemaTCC.repository.Parametros;

@Controller
public class ParametroController {

	@Autowired
	private Parametros parametros;
	
	@RequestMapping("/parametro")
	public ModelAndView parametro(){
		ModelAndView mv = new ModelAndView("ParametroPromocoes");
		return mv;
	}
	
	@RequestMapping("/cadastrarParametro")
	public ModelAndView cadastrarParametro(Parametro parametro){
		parametros.save(parametro);
		ModelAndView mv = new ModelAndView("ParametroPromocoes");
		return mv;
	}
}
