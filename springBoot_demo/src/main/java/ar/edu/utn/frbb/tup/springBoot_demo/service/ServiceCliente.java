package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.util.List;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCliente;

@Service
public class ServiceCliente {
    private DaoCliente daoCliente;

    public ServiceCliente(DaoCliente daoCliente) {
        this.daoCliente = daoCliente;
    }

    // modificar el cliente
    public Cliente update(ClienteDto clienteDto, long dni) {
        // no nesesito validar xq se encarga el cliente validator antes de llegar aca
        // acutualizo y devuelvo el cliente actualizado
        return daoCliente.updateCliente(clienteDto, dni);
    }

    // guardar cliente
    public void save(ClienteDto clienteDto) {
        if (clienteDto == null) throw new IllegalArgumentException("El cliente no puede ser nulo");
        Cliente cliente = new Cliente(clienteDto);
        daoCliente.save(cliente);
    }

    public Cliente buscarClientePorDni(Long dni) {
        return daoCliente.buscarClientePorDni(dni);
    }

    // metodo para dar de alta
    public Cliente altaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente(clienteDto);

        if (daoCliente.buscarClientePorDni(cliente.getDni()) != null) throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        if (cliente.getEdad() < 18) throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");

        daoCliente.save(cliente);
        return cliente;
    }

    // metodo para dar de baja
    public void bajaCliente(Long dni) {
        daoCliente.bajaCliente(dni);
    }

    // mostrar lista de clientes
    public List<Cliente> darClientes(){
        return daoCliente.listarClientes();
    }
}