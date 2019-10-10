package com.alsantos.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

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

import com.alsantos.pontointeligente.api.dtos.CadastroPJDto;
import com.alsantos.pontointeligente.api.entities.Empresa;
import com.alsantos.pontointeligente.api.entities.Funcionario;
import com.alsantos.pontointeligente.api.enuns.PerfilEnum;
import com.alsantos.pontointeligente.api.response.Response;
import com.alsantos.pontointeligente.api.services.EmpresaService;
import com.alsantos.pontointeligente.api.services.FuncionarioService;
import com.alsantos.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {
	
	private final static Logger log = LoggerFactory.getLogger(CadastroPJController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPJController() {
		
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
	public ResponseEntity<Response<CadastroPJDto>> cadastrar(
			@Valid @RequestBody CadastroPJDto cadastroPJDto,
			BindingResult result
			) throws NoSuchAlgorithmException {
		
		log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();
		
		validarDadosExistentes(cadastroPJDto, result);
		Empresa empresa = this.converterDTOparaEmpresa(cadastroPJDto);
		Funcionario funcionario = this.converterDTOparaFuncionario(cadastroPJDto);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados cadastro PJ {}", result.getAllErrors());
			result.getAllErrors().forEach(err -> response.getErrors().add(err.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroDTO(funcionario));
		
		return ResponseEntity.ok(response);
	}

	/**
	 * Valida se a empresa ou funcionário já estão cadastrados no banco de dados
	 * 
	 * @param cadastroPJDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
			.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));
		
		this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));
		
		this.funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já cadastrado.")));
		
	}

	/**
	 * Converte os dados do DTO para empresa
	 * 
	 * @param cadastroPJDto
	 * @return
	 */
	private Empresa converterDTOparaEmpresa(CadastroPJDto cadastroPJDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());
		return empresa;
	}

	/**
	 * Converte os dados do DTO para funcionario
	 * 
	 * @param cadastroPJDto
	 * @return
	 */
	private Funcionario converterDTOparaFuncionario(@Valid CadastroPJDto cadastroPJDto) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
		return funcionario;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionario e empresa
	 * 
	 * @param funcionario
	 * @return
	 */
	private CadastroPJDto converterCadastroDTO(Funcionario funcionario) {
		CadastroPJDto dto = new CadastroPJDto();
		dto.setId(funcionario.getId());
		dto.setNome(funcionario.getNome());
		dto.setEmail(funcionario.getEmail());
		dto.setCpf(funcionario.getCpf());
		dto.setCnpj(funcionario.getEmpresa().getCnpj());
		dto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		return dto;
	}
	
}
