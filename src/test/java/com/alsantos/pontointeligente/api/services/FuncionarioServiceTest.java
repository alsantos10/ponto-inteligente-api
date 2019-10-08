package com.alsantos.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.alsantos.pontointeligente.api.entities.Funcionario;
import com.alsantos.pontointeligente.api.repository.FuncionarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {
	
	private final static String CPF = "12211221432";
	private final static String EMAIL = "email@teste.com";
	private final Long funcionarioId = (long) 12354;

	@MockBean
	private FuncionarioRepository funcRepo;
	
	@Autowired
	private FuncionarioService funcService;
	
	@Before
	public void setUp() {
		BDDMockito.given(this.funcRepo.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcRepo.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcRepo.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
	}
	
	@Test
	public void testPersistirFuncionario() {
		Funcionario f = this.funcService.persistir(new Funcionario());
		assertNotNull(f);
	}
	
	@Test
	public void testBuscarPorCpf() {
		Optional<Funcionario> f = this.funcService.buscarPorCpf(CPF);
		assertTrue(f.isPresent());
	}
	
	@Test
	public void testBuscarPorEmail() {
		Optional<Funcionario> f = this.funcService.buscarPorEmail(EMAIL);
		assertTrue(f.isPresent());
	}
	
	@Test
	public void testBuscarPorId() {
		Optional<Funcionario> f = this.funcService.buscarPorId(funcionarioId);
		assertNotNull(f);
	}
}
