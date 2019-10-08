package com.alsantos.pontointeligente.api.repository;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.alsantos.pontointeligente.api.entities.Empresa;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RepositoryEmpresaTest {
	
	private static final Logger log = LoggerFactory.getLogger(RepositoryEmpresaTest.class);
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private static final String CNPJ = "12343432423432";
	
	@Before
	public void setup() throws Exception {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa Teste");
		empresa.setCnpj(CNPJ);
		this.empresaRepository.save(empresa);
	}
	
	@Test
	public void testBuscarPorCnpj() {
		log.debug("Aqui "+ CNPJ);
		Empresa e = empresaRepository.findByCnpj(CNPJ);
		log.debug("Aqui novo "+ e.getCnpj());
		
		assertEquals(CNPJ, e.getCnpj());
	}
	
	@After
	public final void testDown() {
		this.empresaRepository.deleteAll();
	}
}
