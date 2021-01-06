package com.rodrigo.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rodrigo.cursomc.domain.Cliente;
import com.rodrigo.cursomc.domain.Cliente;
import com.rodrigo.cursomc.dto.ClienteDTO;
import com.rodrigo.cursomc.exception.DataIntegrityException;
import com.rodrigo.cursomc.exception.ObjectNotFoundException;
import com.rodrigo.cursomc.repositories.ClienteRepository;

@Service
public class ClienteService {

	@Autowired //instaciada por injeção
	private ClienteRepository repo; 
	
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id); 
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj,obj); 
		return repo.save(newObj); 
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma cliente porque há entidades relacionadas."); 
		}
	}
	
	public List<Cliente> findAll(){
		return repo.findAll(); 
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy); 
		return repo.findAll(pageRequest); 
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(),
				           clienteDTO.getEmail(), null, null); 
	}
	
	private void updateData(Cliente newObj,Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
