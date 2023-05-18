package org.example;

import org.example.domain.entity.Cliente;
import org.example.domain.repositorio.Clientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class VendasApplication {

    @Bean
    public CommandLineRunner init(@Autowired Clientes clientes){
        return args -> {
            System.out.println("Salvando Cliente");
            clientes.salvar(new Cliente("Douglas"));
            clientes.salvar(new Cliente("João"));

            List<Cliente> todosClientes = clientes.obterTodos();
            todosClientes.forEach(System.out::println);

            System.out.println("Atualizando Cliente");
            todosClientes.forEach(c -> {
                c.setNome(c.getNome() + " Atualizado.");
                clientes.atualizar(c);
            });

            todosClientes = clientes.obterTodos();
            todosClientes.forEach(System.out::println);

            System.out.println("Buscando Cliente");
            clientes.buscarPorNome("ão").forEach(System.out::println);

            System.out.println("Deletando Cliente");
            clientes.obterTodos().forEach(clientes::deletar);


            todosClientes = clientes.obterTodos();
            if(todosClientes.isEmpty()){
                System.out.println("Nenhum Cliente encontrado.");
            }else{
                todosClientes.forEach(System.out::println);
            }
        };
    }

    @Autowired
    @Qualifier("applicationName")
    private String applicationName;

    @GetMapping("/hello")
    public String helloWorld(){
        return applicationName;
    }

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);


    }
}
