package br.com.sistemaTCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sistemaTCC.model.Pontuacao;

public interface Pontuacoes extends JpaRepository<Pontuacao, Long> {

}
