package com.alsantos.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.alsantos.pontointeligente.api.entities.Lancamento;
import com.alsantos.pontointeligente.api.repository.LancamentoRepository;
import com.alsantos.pontointeligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {
	
	private final static Logger log = LoggerFactory.getLogger(LancamentoService.class);
	
	@Autowired
	private LancamentoRepository lancaRepo;

	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscando lancamentos para o funcionario ID {}", id);
		return this.lancaRepo.findById(id);
	}

	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
		log.info("Buscando lancamentos do funcionario pagenado {}", funcionarioId);
		return this.lancaRepo.findByFuncionarioId(funcionarioId, pageRequest);
	}

	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persistindo o lancamento {}", lancamento);
		return this.lancaRepo.save(lancamento);
	}

	public void remover(Long id) {
		log.info("Removendo lancamento ID {}", id);
		this.lancaRepo.deleteById(id);
	}
}
