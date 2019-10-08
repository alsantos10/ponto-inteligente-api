package com.alsantos.pontointeligente.api.services;

import java.util.Optional;

import com.alsantos.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {
	
	/**
	 * Persiste um funcionario no banco de dados
	 * 
	 * @param funcionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 * Busca um funcionario dado o CPF
	 * 
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	/**
	 * Busca funcionario dado o email
	 * 
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	/**
	 * Busca funcionario dado o ID
	 * 
	 * @param funcionarioId
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorId(Long funcionarioId);
}
