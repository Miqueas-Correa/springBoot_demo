package ar.edu.utn.frbb.tup.springBoot_demo.model;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Banco {
    private List<Cliente> clientes = new ArrayList<>();
    private Set<Long> dniUsados = new HashSet<>();

    public boolean estaDniUsado(Long dni){
        return this.dniUsados.contains(dni);
    }

    public void agregarDni(long dni){
        this.dniUsados.add(dni);
    }

    public List<Cliente> getClientes() {
        return this.clientes;
    }

    public void setClientes(Cliente clientes) {
        this.clientes.add(clientes);
    }

    public Cliente buscarClientePorDni(Long dni){
        for (Cliente cliente : this.clientes) {
            if(cliente.getDni().equals(dni)){
                return cliente;
            }
        }
        return null;
    }

    public boolean buscarAlias(String alias){
        for (Cliente cliente : this.clientes) {
            if (cliente.estaAliasUsado(alias)) {
                return true;
            }
        }
        return false;
    }
}
