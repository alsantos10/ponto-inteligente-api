package com.alsantos.pontointeligente.api.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.alsantos.pontointeligente.api.entities.Empresa;
import com.alsantos.pontointeligente.api.entities.Funcionario;
import com.alsantos.pontointeligente.api.enuns.PerfilEnum;
import com.alsantos.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RepositoryFuncionarioTest {
	
	@Autowired
	private FuncionarioRepository funcRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private final static String CPF = "12345678910";
	private final static String EMAIL = "teste@teste.com";
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepository.save(this.getDataEmpresa());
		this.funcRepository.save(this.getDataFunc(empresa));
	}
	
	@After
	public void testDown() {
		empresaRepository.deleteAll();
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		Funcionario func = funcRepository.findByEmail(EMAIL);
		
		assertEquals(EMAIL, func.getEmail());
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		Funcionario func = funcRepository.findByCpf(CPF);
		
		assertEquals(CPF, func.getCpf());
	}
	
	@Test
	public void testBuscarFuncPorCpfOrEmail() {
		Funcionario func = funcRepository.findByCpfOrEmail(CPF, EMAIL);
		
		assertNotNull(func);
	}
	
	@Test
	public void testBuscarFuncPorEmailOrCpfParaEmailInvalido() {
		Funcionario func = funcRepository.findByCpfOrEmail(CPF, "teste2@teste.com");
		
		assertNotNull(func);
	}

	@Test
	public void testBuscarFuncPorEmailOrCpfParaCpfInvalido() {
		Funcionario func = funcRepository.findByCpfOrEmail("213254654765", EMAIL);
		
		assertNotNull(func);
	}
	
	private Empresa getDataEmpresa() {
		Empresa e = new Empresa();
		e.setCnpj("1234678654433");
		e.setRazaoSocial("Empresa Ficticia");
		return e;
	}
	
	private Funcionario getDataFunc(Empresa empresa) throws NoSuchAlgorithmException {
		Funcionario f = new Funcionario();
		f.setNome("Fulano de Tal");
		f.setPerfil(PerfilEnum.ROLE_USUARIO);
		f.setSenha(PasswordUtils.gerarBCrypt("321432543654"));
		f.setCpf(CPF);
		f.setEmail(EMAIL);
		f.setEmpresa(empresa);
		return f;
	}

}
