package com.rodrigo.cursomc.services;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigo.cursomc.domain.ItemPedido;
import com.rodrigo.cursomc.domain.PagamentoComBoleto;
import com.rodrigo.cursomc.domain.Pedido;
import com.rodrigo.cursomc.domain.enums.EstadoPagamento;
import com.rodrigo.cursomc.exception.ObjectNotFoundException;
import com.rodrigo.cursomc.repositories.ItemPedidoRepository;
import com.rodrigo.cursomc.repositories.PagamentoRepository;
import com.rodrigo.cursomc.repositories.PedidoRepository;
import com.rodrigo.cursomc.repositories.ProdutoRepository;

@Service
public class PedidoService {

	@Autowired //instaciada por injeção
	private PedidoRepository repo; 
	
	@Autowired
	private BoletoService boletoService; 
	
	@Autowired
	private PagamentoRepository pagamentoRepository; 
	
	@Autowired
	private ProdutoRepository produtoRepository; 
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository; 
	
	@Autowired
	private ClienteService clienteService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id); 
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstance(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto)obj.getPagamento(); 
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstance()); 
		}
		
		obj = repo.save(obj); 
		pagamentoRepository.save(obj.getPagamento()); 
		for(ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto( produtoRepository.findById(ip.getProduto().getId() ).get());
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens()); 
		System.out.println(obj);
		return obj; 
	}
}
