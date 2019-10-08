package com.alsantos.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alsantos.pontointeligente.api.entities.Empresa;
import com.alsantos.pontointeligente.api.repository.EmpresaRepository;
import com.alsantos.pontointeligente.api.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	private final static Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando uma empresa por CNPJ {}", cnpj);
		return Optional.ofNullable(this.empresaRepo.findByCnpj(cnpj));
	}
	
	@Override
	public Empresa persistir(Empresa empresa) {
		log.debug("Persistindo uma empresa {}", empresa);
		return this.empresaRepo.save(empresa);
	}
}
