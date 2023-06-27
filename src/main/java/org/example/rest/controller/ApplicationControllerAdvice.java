package org.example.rest.controller;

import org.example.exception.PedidoNaoEncontradoException;
import org.example.exception.RegrasNegocioException;
import org.example.rest.ApiErros;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleRegraNegocioException(RegrasNegocioException ex){
        String mensagemErro = ex.getMessage();
        return new ApiErros(mensagemErro);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErros handlePedidoNaoEncontradoException(PedidoNaoEncontradoException ex){
        return  new ApiErros(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleMethodNotValidException(MethodArgumentNotValidException ex){
        List<String> erros = ex.getBindingResult().getAllErrors()
                .stream()
                .map(erro -> erro.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiErros(erros);
    }

}
