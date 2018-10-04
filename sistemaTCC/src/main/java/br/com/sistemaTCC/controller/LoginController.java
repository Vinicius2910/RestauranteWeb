package br.com.sistemaTCC.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.sistemaTCC.model.Lojista;
import br.com.sistemaTCC.repository.Clientes;
import br.com.sistemaTCC.repository.Lojistas;

@Controller
public class LoginController {
	@Autowired
	private Lojistas lojistas;

	@Autowired
	private Clientes clientes;
	
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
	   return "ModalDeSucesso";
	}
	
	@RequestMapping(value="/validarLoginLojista", method= RequestMethod.POST)
	public String validarLogin(Lojista lojista){
		boolean existe = lojistas.existsById(lojista.getCpfCNPJ());
		
		if(existe == true){
			return "HomeLojista";	
		}
		else{
			return "Login";
		}
	   
	}
	
}
