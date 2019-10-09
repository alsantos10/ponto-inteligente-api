package com.alsantos.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.alsantos.pontointeligente.api.entities.Empresa;
import com.alsantos.pontointeligente.api.entities.Funcionario;
import com.alsantos.pontointeligente.api.entities.Lancamento;
import com.alsantos.pontointeligente.api.enuns.PerfilEnum;
import com.alsantos.pontointeligente.api.enuns.TipoEnum;
import com.alsantos.pontointeligente.api.repository.LancamentoRepository;
import com.alsantos.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {
	
	private final Long funcionarioId = (long) 12344354;
	@SuppressWarnings("deprecation")
	private final PageRequest pageable = new PageRequest(0, 10);
	private final static String CPF = "123454534543";
	private final static String EMAIL = "email@email.com";
	
	@MockBean
	LancamentoRepository lancRepo;
	
	@Autowired
	LancamentoService lancService;
	
	@Before
	public void setUp() {
		BDDMockito
			.given(this.lancRepo.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
			.willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		BDDMockito.given(this.lancRepo.getOne(Mockito.anyLong())).willReturn(new Lancamento());
		BDDMockito.given(this.lancRepo.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
	}

	@Test
	public void testBuscarPorId() {
		Optional<Lancamento> l = this.lancService.buscarPorId(funcionarioId);
		assertNotNull(l);
	}
	
	@Test
	public void testBuscarPorFuncionarioId() {
		Page<Lancamento> lancamentos = this.lancService.buscarPorFuncionarioId(funcionarioId, pageable);
		assertNotNull(lancamentos);
	}
	
	@Test
	public void testPersistirLancamento() throws NoSuchAlgorithmException {
		Lancamento l1 = this.getDataLancamento(this.getDataFunc(this.getDataEmpresa()));
		Lancamento l = this.lancService.persistir(l1);
		assertNotNull(l);
	}
	
	private Lancamento getDataLancamento(Funcionario funcionario) {
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
