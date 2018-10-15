package br.com.sistemaTCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemaTCC.model.Transacao;

public interface Transacoes extends JpaRepository<Transacao, Long>  {

}
