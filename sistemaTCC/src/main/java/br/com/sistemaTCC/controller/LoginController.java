package br.com.sistemaTCC.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.sistemaTCC.model.Cliente;
import br.com.sistemaTCC.model.Lojista;
import br.com.sistemaTCC.repository.Clientes;
import br.com.sistemaTCC.repository.Lojistas;
import br.com.sistemaTCC.repository.Promocoes;

@Controller
public class LoginController {
	@Autowired
	private Lojistas lojistas;
	@Autowired
	private Clientes clientes;
	@Autowired
	private Promocoes promocoes;

	
	@RequestMapping("/login")
	public String login(){
	   return "Login";
	}
	
	@RequestMapping("/novoUsuario")
	public String NovoUsuario(){
	   return "CadastroLogin";
	}
	
	@RequestMapping("/cadastrarUsuario")
	public String cadastrarUsuario(Lojista lojista){
		lojistas.save(lojista);
	   return "Login";
	}
	
	@RequestMapping(value="/validarLogin", method= RequestMethod.POST)
	public ModelAndView validarLogin(String cpfCNPJ,String senha, String tipo, HttpSession session){
		Long cpf =  Long.valueOf(cpfCNPJ);
		
		if(tipo.equals("Lojista")){
			boolean existe = lojistas.existsById(cpf);
			Lojista lojista = lojistas.getOne(cpf);
			
			if(existe == true ){
				ModelAndView mv = new ModelAndView("HomeLojista");
				mv.addObject("lojista",lojista);
				return mv;
			}
			else{
				ModelAndView mv = new ModelAndView("Login");
				mv.addObject("erro", "Usuário não encontrado");
				return mv;
			}	
		}
		if(tipo.equals("Cliente")){
			boolean existe = clientes.existsById(cpf);
			Cliente cliente =  clientes.getOne(cpf);
			List<Lojista> listLojista = lojistas.findAll();
			if(existe == true){
				ModelAndView mv = new ModelAndView("GaleriaLojistas");
				mv.addObject("cliente",cliente);
				mv.addObject("lojistas",listLojista);
				return mv;
			}
			else{
				ModelAndView mv = new ModelAndView("Login");
				mv.addObject("erro", "Usuário não encontrado");
				return mv;
			}
		}
		if(tipo.equals("ClienteTablet")){
			boolean existe = lojistas.existsById(cpf);
			Lojista lojista = lojistas.getOne(cpf);
			if(existe == true){
				ModelAndView mv = new ModelAndView("Cliente");
				mv.addObject("lojista",lojista);
				return mv;
			}
			else{
				ModelAndView mv = new ModelAndView("Login");
				mv.addObject("erro", "Usuário não encontrado");
				return mv;
			}
		}
		else{
			ModelAndView mv = new ModelAndView("Login");
			mv.addObject("erro", "Usuário não encontrado");
			return mv;
		}
	}
	
	@RequestMapping(value="/redirectListaLojista", method= RequestMethod.POST)
	public ModelAndView redirectListaLojista(Long cpf){
		boolean existe = clientes.existsById(cpf);
		Cliente cliente =  clientes.getOne(cpf);
		List<Lojista> listLojista = lojistas.findAll();
		if(existe == true){
			ModelAndView mv = new ModelAndView("GaleriaLojistas");
			mv.addObject("cliente",cliente);
			mv.addObject("lojistas",listLojista);
			return mv;
		}
		else{
			ModelAndView mv = new ModelAndView("Login");
			mv.addObject("erro", "Usuário não encontrado");
			return mv;
		}
	
	}
}
