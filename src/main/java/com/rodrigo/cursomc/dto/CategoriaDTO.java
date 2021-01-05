package com.rodrigo.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.rodrigo.cursomc.domain.Categoria;


public class CategoriaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id; 
	@NotEmpty(message = "Prenchimento obrigatório")
	@Length(min = 5, max = 80, message = "O Tamanho deve ser entre 5 e 80")
	private String nome;

	public CategoriaDTO(){
		
	}
	
	public CategoriaDTO(Categoria categoria){
		this.id = categoria.getId(); 
		this.nome = categoria.getNome(); 
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
