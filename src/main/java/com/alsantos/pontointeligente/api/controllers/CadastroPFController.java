package com.alsantos.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alsantos.pontointeligente.api.dtos.CadastroPFDto;
import com.alsantos.pontointeligente.api.entities.Funcionario;
import com.alsantos.pontointeligente.api.enuns.PerfilEnum;
import com.alsantos.pontointeligente.api.response.Response;
import com.alsantos.pontointeligente.api.services.EmpresaService;
import com.alsantos.pontointeligente.api.services.FuncionarioService;
import com.alsantos.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

	private final static Logger log = LoggerFactory.getLogger(CadastroPJController.class);

	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPFController() {
		
	}
	
	/**
	 * Cadastra uma pessoa jurídica no sistema
	 * 
	 * @param cadastroPJDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPJDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroPFDto>> cadastrar(
			@Valid @RequestBody CadastroPFDto cadastroPFDto,
			BindingResult result
			) throws NoSuchAlgorithmException {
		
		log.info("Cadastrando Funcionario: {}", cadastroPFDto.toString());
		
		Response<CadastroPFDto> response = new Response<CadastroPFDto>();
		
		validarDadosExistentes(cadastroPFDto, result);
		Funcionario funcionario = this.converterDTOparaFuncionario(cadastroPFDto);
		
		if (result.hasErrors()) {
			log.info("Erro validando dados de cadastro PF {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj())
			.ifPresent(emp -> funcionario.setEmpresa(emp));
		
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroDTO(funcionario));
		
		return ResponseEntity.ok(response);
	}

	/**
	 * Valida se a empresa ou funcionário já estão cadastrados no banco de dados
	 * 
	 * @param cadastroPFDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
		
		if(!this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj()).isPresent()) {			
			result.addError(new ObjectError("empresa", "Empresa não está cadastrada."));
		}
		
		this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));
		
		this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para funcionario
	 * 
	 * @param cadastroPFDto
	 * @return
	 */
	private Funcionario converterDTOparaFuncionario(@Valid CadastroPFDto cadastroPFDto) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf(cadastroPFDto.getCpf());
		funcionario.setEmail(cadastroPFDto.getEmail());
		funcionario.setNome(cadastroPFDto.getNome());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
		cadastroPFDto.getQtdHorasAlmoco()
			.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco((Float.valueOf(qtdHorasAlmoco))));
		cadastroPFDto.getQtdHorasTrabalhoDia()
			.ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		cadastroPFDto.getValorHora()
			.ifPresent(valorHora -> funcionario.setValorHora(valorHora));
		return funcionario;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionario e empresa
	 * 
	 * @param funcionario
	 * @return
	 */
	private CadastroPFDto converterCadastroDTO(Funcionario funcionario) {
		CadastroPFDto dto = new CadastroPFDto();
		dto.setId(funcionario.getId());
		dto.setNome(funcionario.getNome());
		dto.setEmail(funcionario.getEmail());
		dto.setCpf(funcionario.getCpf());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		funcionario.getQtdHorasAlmocoOpt()
			.ifPresent(qtdHorasAlmoco -> dto.setQtdHorasAlmoco(Optional.ofNullable(qtdHorasAlmoco.toString())));
		
		funcionario.getQtdHorasTrabalhoDiaOpt()
			.ifPresent(qtdHorasTrabalhoDia -> dto.setQtdHorasTrabalhoDia(Optional.ofNullable(qtdHorasTrabalhoDia.toString())));
		
		dto.setValorHora(Optional.ofNullable(funcionario.getValorHora()));

		return dto;
	}
}
