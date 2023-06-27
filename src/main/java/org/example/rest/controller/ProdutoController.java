package org.example.rest.controller;

import jakarta.validation.Valid;
import org.example.domain.entity.Produto;
import org.example.domain.repository.Produtos;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    private Produtos produtos;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto save( @RequestBody @Valid Produto produto){
        return produtos.save(produto);
    }

    @GetMapping("{id}")
    public Produto findById(@PathVariable(name = "id") Integer id){
      return produtos.findById(id)
              .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @GetMapping
    public List<Produto> listaProdutos(Produto filtro){
        ExampleMatcher matcher = ExampleMatcher.matching()
                                  .withIgnoreCase()
                                  .withStringMatcher(
                                         ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro,matcher);
       return produtos.findAll(example);
    }

    @PutMapping("{id}")
    public Produto update(@PathVariable Integer id,
                          @RequestBody @Valid Produto produto){
      return produtos.findById(id)
                .map(produtoExistente -> {
                    produto.setId(produtoExistente.getId());
                    produtos.save(produto);
                    return produto;} )
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id){
        produtos.findById(id)
                .map( produto -> {
                    produtos.delete(produto);
                    return produto;} )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
