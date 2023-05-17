package org.example.domain.repositorio;

import org.example.domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class Clientes {

    private static String INSERT = "insert into cliente(nome) values(?) ";
    private static String SELECT_ALL = "SELECT * FROM CLIENTE ";
    private static String UPDATE = "UPDATE cliente set nome = ? WHERE id = ? ";
    private static String DELETE = "DELETE FROM cliente WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Cliente salvar(Cliente cliente) {
        jdbcTemplate.update( INSERT, cliente.getNome());
        return cliente;
    }

    public Cliente atualizar(Cliente cliente){
        jdbcTemplate.update( UPDATE, cliente.getNome(),cliente.getId());
        return cliente;
    }

    public void deletar(Cliente cliente){
        deletar(cliente.getId());
    }

    public void deletar(Integer id){
        jdbcTemplate.update( DELETE, id);
    }

    public List<Cliente> buscarPorNome(String nome){
         return jdbcTemplate.query( SELECT_ALL.concat("WHERE nome like ? "),
                 new Object[]{"%" + nome + "%"},
                 obterClienteRowMapper());
    }
    public List<Cliente> obterTodos(){
        return jdbcTemplate.query( SELECT_ALL, obterClienteRowMapper());
    }

    private static RowMapper<Cliente> obterClienteRowMapper() {
        return new RowMapper<Cliente>() {
            @Override
            public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = rs.getInt("id");
                String nome = rs.getString("nome");
                return new Cliente(id, nome);
            }
        };
    }
}
