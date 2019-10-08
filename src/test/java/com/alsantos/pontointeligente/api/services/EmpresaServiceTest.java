package com.alsantos.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.alsantos.pontointeligente.api.entities.Empresa;
import com.alsantos.pontointeligente.api.repository.EmpresaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTest {

	@MockBean
	private EmpresaRepository empRepo;
	
	@Autowired
	private EmpresaService empService;
	
	private final static String CNPJ = "123243254";
	
	@Before
	public void setUp() {
		BDDMockito.given(this.empRepo.findByCnpj(Mockito.anyString())).willReturn(new Empresa());
		BDDMockito.given(this.empRepo.save(Mockito.any(Empresa.class))).willReturn(new Empresa());
	}
	
	@Test
	public void testBuscaEmpresaPorCnpj() {
		Optional<Empresa> e = this.empService.buscarPorCnpj(CNPJ);
		
		assertTrue(e.isPresent());
	}
	
	@Test
	public void testPersistirEmpresa() {
		Empresa e = this.empService.persistir(this.empRepo.findByCnpj(CNPJ));
		
		assertNotNull(e);
	}
}
