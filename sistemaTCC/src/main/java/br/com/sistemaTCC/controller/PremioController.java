package br.com.sistemaTCC.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import br.com.sistemaTCC.model.Premio;
import br.com.sistemaTCC.repository.Premios;

@Controller
public class PremioController {
	
	@Autowired
	private Premios premios;
	
	@RequestMapping("/premios")
	public ModelAndView tabelaPremios() {
		List<Premio> todosPremios = premios.findAll();
		ModelAndView mv= new ModelAndView("ManterPremio");
		mv.addObject("premios", todosPremios);
		return mv;
	}
	
	@RequestMapping(value ="/novoPremio",method= RequestMethod.POST)
	public String salvar(Premio premio){
		premios.save(premio);
		return "redirect:/premios";	
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
		 return "redirect:/premios";	
	}
	
	@RequestMapping("/deletarPremio/{codigo}")
	public ModelAndView retornaCodigoExclusao(@PathVariable Long codigo){
		Premio premio = premios.getOne(codigo);
		ModelAndView mv = new ModelAndView("ExcluirPremio");
		mv.addObject(premio);
		return mv;	
	}
	
}
