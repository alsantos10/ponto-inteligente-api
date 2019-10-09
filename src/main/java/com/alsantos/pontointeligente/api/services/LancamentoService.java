package com.alsantos.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.alsantos.pontointeligente.api.entities.Lancamento;

public interface LancamentoService {
	
	/**
	 * Retorna lancamento por ID
	 * 
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscarPorId(Long id);

	/**
	 * Buscar lista paginada de lancamentos de um funcionario
	 * 
	 * @param funcionarioId
	 * @param pageable
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest);
	
	/**
	 * Persiste um lancamento no banco de dados
	 * 
	 * @param lancamento
	 * @return Lancamento
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remove um lancamento do banco de dados
	 * 
	 * @param id
	 */
	void remover(Long id);
}
