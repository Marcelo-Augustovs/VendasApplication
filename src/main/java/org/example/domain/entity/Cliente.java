package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "id")
    private Integer id;

    @Column (name = "nome", length = 100)
    @NotEmpty(message = "Campo nome é Obrigatório.")
    private String nome;

    @Column (name = "cpf", length = 11)
    @NotEmpty(message = "Campo CPF é Obrigatório.")
    @CPF(message = "Informe um CPF Válido")
    private String cpf;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente",fetch = FetchType.LAZY)
    private Set<Pedido> pedidos;

    public Set<Pedido> getPedidos() {
        if(this.pedidos == null){
            this.pedidos = new HashSet<>();
        }
        return this.pedidos;
    }
}
