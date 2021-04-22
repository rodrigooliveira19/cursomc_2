package com.rodrigo.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URI;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rodrigo.cursomc.domain.Cidade;
import com.rodrigo.cursomc.domain.Cliente;
import com.rodrigo.cursomc.domain.Endereco;
import com.rodrigo.cursomc.domain.enums.Perfil;
import com.rodrigo.cursomc.domain.enums.TipoCliente;
import com.rodrigo.cursomc.dto.ClienteDTO;
import com.rodrigo.cursomc.dto.ClienteNewDTO;
import com.rodrigo.cursomc.exception.AuthorizationException;
import com.rodrigo.cursomc.exception.DataIntegrityException;
import com.rodrigo.cursomc.exception.ObjectNotFoundException;
import com.rodrigo.cursomc.repositories.ClienteRepository;
import com.rodrigo.cursomc.repositories.EnderecoRepository;
import com.rodrigo.cursomc.security.UserSS;

@Service
public class ClienteService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	@Autowired //instaciada por injeção
	private ClienteRepository repo; 
	@Autowired
	private EnderecoRepository enderecoRepository; 
	@Autowired
	private S3Service s3Service; 
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix; 
	
	@Value("${img.profile.size}")
	private Integer size; 
	
	
	public Cliente find(Integer id) {
		UserSS user =  UserService.authenticated(); 
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado"); 
		}
		
		Optional<Cliente> obj = repo.findById(id); 
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj); 
		enderecoRepository.saveAll(obj.getEnderecos()); 
		return obj; 
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
			throw new DataIntegrityException("Não é possível excluir uma cliente porque há pedidos relacionadas ao cliente."); 
		}
	}
	
	public List<Cliente> findAll(){
		return repo.findAll(); 
	}
	
	public Cliente findByEmail(String email) {
		UserSS user =  UserService.authenticated(); 
		if(user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado"); 
		}
		
		Cliente obj = repo.findByEmail(user.getUsername()); 
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! id: "+user.getId() + " Tipo: "+Cliente.class.getName()); 
		}
		
		return obj; 
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy); 
		return repo.findAll(pageRequest); 
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(),
				           clienteDTO.getEmail(), null, null,null); 
	}
	
	public Cliente fromDTO(ClienteNewDTO clienteDTO) {
		Cliente cli = new Cliente(null, clienteDTO.getNome(),clienteDTO.getEmail(), clienteDTO.getCpfOuCnpj(), 
				TipoCliente.toEnum(clienteDTO.getTipoCliente()),bCryptPasswordEncoder.encode(clienteDTO.getSenha())); 
		Endereco end = new Endereco(null, clienteDTO.getLogradouro(), clienteDTO.getNumero(), 
				clienteDTO.getBairro(), clienteDTO.getComplemento(), clienteDTO.getCep(), cli, new Cidade(clienteDTO.getCidadeId(),null));
		cli.getEnderecos().add(end);  
		cli.getTelefones().add(clienteDTO.getTelefone1()); 
		if(clienteDTO.getTelefone2() != null)
			cli.getTelefones().add(clienteDTO.getTelefone2()); 
		if(clienteDTO.getTelefone3() != null)
			cli.getTelefones().add(clienteDTO.getTelefone3()); 
		
		return cli; 
	}
	
	private void updateData(Cliente newObj,Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		//Verificar se o usuario está logado. 
		UserSS user =  UserService.authenticated(); 
		if(user == null) {
			throw new AuthorizationException("Acesso negado"); 
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile); 
		
		//Recortar a imagem - Formato quadrado 
		jpgImage = imageService.cropSquare(jpgImage); 
		//Diminui o tamanho
		jpgImage =  imageService.resize(jpgImage,size); 
		
		String fileName =  prefix + user.getId() + "jpg"; 
		
		return s3Service.uploadFile(fileName, imageService.getInputStream(jpgImage, "jpg") , "image"); 
	}
}
