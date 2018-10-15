
package br.com.sistemaTCC.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.sistemaTCC.model.Lojista;
import br.com.sistemaTCC.model.Parametro;
import br.com.sistemaTCC.repository.Lojistas;
import br.com.sistemaTCC.repository.Parametros;

@Controller
public class ParametroController {

	@Autowired
	private Parametros parametros;

	@Autowired
	private Lojistas lojistas;
	
	@RequestMapping("/parametro/{cpf}")
	public ModelAndView parametro(@PathVariable Long cpf){
		Lojista lojista =  lojistas.getOne(cpf);
		ModelAndView mv = new ModelAndView("ParametroPromocoes");
		mv.addObject("lojista", lojista);
		return mv;
	}
	
	@RequestMapping("/cadastrarParametro/{cpf}")
	public String cadastrarParametro(@PathVariable Long cpf, Parametro parametro){
		List<Parametro> todosParametros =  parametros.findAll();
		boolean exist = verificarPorLojistaId(todosParametros,cpf);
		Lojista lojista =  lojistas.getOne(cpf);
		System.out.println("tamanho?---" + todosParametros.size());
		System.out.println("existe?---" + exist);
		
		if(exist != true){
			parametro.setLojistaID(lojista);
			parametros.save(parametro);
			ModelAndView mv = new ModelAndView("ParametroPromocoes");
			mv.addObject("lojista", lojista);
			return "redirect:/parametro/"+cpf;		
		}
		else{
			Parametro parametro1 = encontrarParametro(todosParametros, cpf);
			parametro1.setTipo(parametro.getTipo());
			parametro1.setValor(parametro.getValor());
			parametros.save(parametro1);
			ModelAndView mv = new ModelAndView("ParametroPromocoes");
			mv.addObject("lojista", lojista);
			return "redirect:/parametro/"+cpf;	
		}
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
}
