package com.rodrigo.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.cursomc.domain.Categoria;
import com.rodrigo.cursomc.exception.ObjectNotFoundException;
import com.rodrigo.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired //instaciada por injeção
	private CategoriaRepository repo; 
	
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id); 
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj); 
	}
	
	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repo.save(obj); 
	}
}
