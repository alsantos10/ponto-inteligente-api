package com.alsantos.pontointeligente.api.services.impl;

import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alsantos.pontointeligente.api.entities.Funcionario;
import com.alsantos.pontointeligente.api.repository.FuncionarioRepository;
import com.alsantos.pontointeligente.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
	
	private final static Logger log = LoggerFactory.getLogger(FuncionarioService.class);

	@Autowired
	private FuncionarioRepository funcRepo;
	
	@Override
	public Funcionario persistir(Funcionario funcionario) {
		log.info("Persistindo funcionario", funcionario);
		return this.funcRepo.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		log.info("Buncando funcionario por CPF {}", cpf);
		return Optional.ofNullable(this.funcRepo.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		log.info("Buscando funcionario por email {}", email);
		return Optional.ofNullable(this.funcRepo.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long funcionarioId) {
		log.info("Buscando funcionario por ID");
		return this.funcRepo.findById(funcionarioId);
	}
}
