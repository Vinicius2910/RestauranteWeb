package br.com.sistemaTCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemaTCC.model.Cliente;

public interface Clientes extends JpaRepository<Cliente, Long>{

}
