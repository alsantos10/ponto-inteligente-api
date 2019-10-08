package com.alsantos.pontointeligente.api.repository;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.alsantos.pontointeligente.api.entities.Empresa;
import com.alsantos.pontointeligente.api.entities.Funcionario;
import com.alsantos.pontointeligente.api.entities.Lancamento;
import com.alsantos.pontointeligente.api.enuns.PerfilEnum;
import com.alsantos.pontointeligente.api.enuns.TipoEnum;
import com.alsantos.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RepositoryLancamentoTest {
	
	private final static String CPF = "123454534543";
	private final static String EMAIL = "email@email.com";
	private final static String ENDERECO = "Rua de Teste";
	private Long funcId;
	
	@Autowired
	private EmpresaRepository empRepo;
	
	@Autowired
	private FuncionarioRepository funcRepo;
	
	@Autowired
	private LancamentoRepository lancRepo;
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empRepo.save(this.getDataEmpresa());
		Funcionario funcionario = this.funcRepo.save(this.getDataFunc(empresa));
		this.funcId = funcionario.getId();
		this.lancRepo.save(this.getDataLancamentoEntrada(funcionario));
		this.lancRepo.save(this.getDataLancamentoSaida(funcionario));
	}
	
	@After
	public void testDown() {
		this.empRepo.deleteAll();
		this.funcRepo.deleteAll();
		this.lancRepo.deleteAll();
	}
	
	@Test
	public void testBuscarLancamento() {
		List<Lancamento> l1 = this.lancRepo.findByFuncionarioId(this.funcId);
		
		assertEquals(2, l1.size());
	}
	
	@Test
	public void testBuscarLancamentoPage() {
		@SuppressWarnings("deprecation")
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancRepo.findByFuncionarioId(this.funcId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
	}
	
	private Lancamento getDataLancamentoEntrada(Funcionario funcionario) {
		Lancamento l = new Lancamento();
		l.setDescricao("Entrada");
		l.setLocalizacao(ENDERECO);
		l.setTipo(TipoEnum.INICIO_TRABALHO);
		l.setData(new Date());
		l.setFuncionario(funcionario);
		return l;
	}
	
	private Lancamento getDataLancamentoSaida(Funcionario funcionario) {
		Lancamento l = new Lancamento();
		l.setDescricao("Saida");
		l.setLocalizacao("Rua de Teste");
		l.setTipo(TipoEnum.TERMINO_TRABALHO);
		l.setData(new Date());
		l.setFuncionario(funcionario);
		return l;
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
