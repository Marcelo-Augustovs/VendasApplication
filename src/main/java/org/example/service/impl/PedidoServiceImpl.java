package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.entity.Cliente;
import org.example.domain.entity.ItemPedido;
import org.example.domain.entity.Pedido;
import org.example.domain.entity.Produto;
import org.example.domain.enums.StatusPedido;
import org.example.domain.repository.Clientes;
import org.example.domain.repository.ItensPedidos;
import org.example.domain.repository.Pedidos;
import org.example.domain.repository.Produtos;
import org.example.exception.PedidoNaoEncontradoException;
import org.example.exception.RegrasNegocioException;
import org.example.rest.dto.ItemPedidoDTO;
import org.example.rest.dto.PedidoDTO;
import org.example.service.PedidoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedidos itensPedidosRepository;


    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow( () -> new RegrasNegocioException("Código de cliente invalido"));

        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDate.now());
        pedido.setTotal(dto.getTotal());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedido = converterItems(pedido,dto.getItems());
        repository.save(pedido);
        itensPedidosRepository.saveAll(itemPedido);
        pedido.setItens(itemPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        repository.findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return repository.save(pedido);})
                .orElseThrow( () -> new PedidoNaoEncontradoException() );
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> itens){

        if(itens.isEmpty()){
            throw new RegrasNegocioException("Não é possível realizar um pedido sem item.");
        }

        return itens.stream()
                .map( dto -> {
                  Integer produtoId = dto.getProduto();
                  Produto produto = produtosRepository.findById(produtoId)
                          .orElseThrow( () -> new RegrasNegocioException("Código de produto inválido"));

                  ItemPedido itemPedido = new ItemPedido();

                  itemPedido.setQuantitade(dto.getQuantidade());
                  itemPedido.setPedido(pedido);
                  itemPedido.setProduto(produto);
                  return itemPedido;})
                .collect(Collectors.toList() );
    }
}
