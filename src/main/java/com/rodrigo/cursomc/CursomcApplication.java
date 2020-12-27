package com.rodrigo.cursomc;


import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rodrigo.cursomc.domain.Categoria;
import com.rodrigo.cursomc.domain.Cidade;
import com.rodrigo.cursomc.domain.Cliente;
import com.rodrigo.cursomc.domain.Endereco;
import com.rodrigo.cursomc.domain.Estado;
import com.rodrigo.cursomc.domain.Pagamento;
import com.rodrigo.cursomc.domain.PagamentoComBoleto;
import com.rodrigo.cursomc.domain.PagamentoComCartao;
import com.rodrigo.cursomc.domain.Pedido;
import com.rodrigo.cursomc.domain.Produto;
import com.rodrigo.cursomc.domain.enums.EstadoPagamento;
import com.rodrigo.cursomc.domain.enums.TipoCliente;
import com.rodrigo.cursomc.repositories.CategoriaRepository;
import com.rodrigo.cursomc.repositories.CidadeRepository;
import com.rodrigo.cursomc.repositories.ClienteRepository;
import com.rodrigo.cursomc.repositories.EnderecoRepository;
import com.rodrigo.cursomc.repositories.EstadoRepository;
import com.rodrigo.cursomc.repositories.PagamentoRepository;
import com.rodrigo.cursomc.repositories.PedidoRepository;
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
	@Autowired
	private ClienteRepository clienteRepository; 
	@Autowired
	private EnderecoRepository enderecoRepository; 
	@Autowired
	private PedidoRepository pedidoRepository; 
	@Autowired
	private PagamentoRepository pagamentoRepository; 
	
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
		
		Cliente cli1 = new Cliente(null, "Maria Silva","maria@gmail.com", "84744645343",TipoCliente.PESSOAFISICA); 
		
		cli1.getTelefones().addAll(Arrays.asList("32567354","92778854")); 
		
		Endereco e1 =new Endereco(null,"Rua Flores", "300", "Apto 303", "Jardim","03938337",cli1,c1); 
		Endereco e2 =new Endereco(null,"Rua Flores", "300", "Apto 303", "Jardim","03938337",cli1,c2); 
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2)); 
		
		clienteRepository.saveAll(Arrays.asList(cli1)); 
		enderecoRepository.saveAll(Arrays.asList(e1,e2)); 
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		//sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
	}

}
