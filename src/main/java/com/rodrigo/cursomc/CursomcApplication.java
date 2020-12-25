package com.rodrigo.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rodrigo.cursomc.domain.Categoria;
import com.rodrigo.cursomc.domain.Cidade;
import com.rodrigo.cursomc.domain.Estado;
import com.rodrigo.cursomc.domain.Produto;
import com.rodrigo.cursomc.repositories.CategoriaRepository;
import com.rodrigo.cursomc.repositories.CidadeRepository;
import com.rodrigo.cursomc.repositories.EstadoRepository;
import com.rodrigo.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository categoriaRepository; 
	@Autowired
	private ProdutoRepository produtoRepository; 
	@Autowired
	private EstadoRepository estadoRepository; 
	@Autowired
	private CidadeRepository cidadeRepository; 
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null,"Informática"); 
		Categoria cat2 = new Categoria(null,"Escritório"); 
		
		Produto p1 = new Produto(null, "Computador",2000.00 ); 
		Produto p2 = new Produto(null, "Impressora",2000.00 ); 
		Produto p3 = new Produto(null, "Mouse",2000.00 ); 
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p1,p2, p3));
		
		p1.getCategorias().addAll(Arrays.asList(cat1)); 
		p2.getCategorias().addAll(Arrays.asList(cat2)); 
		p3.getCategorias().addAll(Arrays.asList(cat1)); 
		
		
		categoriaRepository.saveAll(Arrays.asList(cat1,cat2)); 
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		
		Estado est1 = new Estado(null, "Minas Gerais"); 
		Estado est2 = new Estado(null, "São Paulo"); 
		
		Cidade c1 = new Cidade(null, "Uberlândia"); 
		Cidade c2 = new Cidade(null, "São Paulo"); 
		Cidade c3 = new Cidade(null, "Campinas"); 
		
		est1.getCidades().addAll(Arrays.asList(c1)); 
		est2.getCidades().addAll(Arrays.asList(c2, c3)); 
		
		c1.setEstado(est1);
		c2.setEstado(est2);
		c3.setEstado(est2);
		
		estadoRepository.saveAll(Arrays.asList(est1,est2)); 
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3)); 
	}

}
