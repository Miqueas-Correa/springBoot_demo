package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCliente;

@Service
public class ServiceCliente {
    @Autowired
    private DaoCliente daoCliente;

    // modificar el cliente
    public Respuesta<Cliente> update(ClienteDto clienteDto, long dni) {
        // acutualizo y devuelvo el cliente actualizado
        return new Respuesta<>(
            new MsjResponce("EXITOSA", "Se actualizo el cliente con DNI " + dni), 
            daoCliente.updateCliente(clienteDto, dni), 
            HttpStatus.OK);
    }

    // guardar cliente
    public void save(ClienteDto clienteDto) {
        if (clienteDto == null) throw new IllegalArgumentException("El cliente no puede ser nulo");
        Cliente cliente = new Cliente(clienteDto);
        daoCliente.save(cliente);
    }

    public Respuesta<Cliente> buscarClientePorDni(Long dni) {
        return new Respuesta<>(
            new MsjResponce("EXITOSA", "Aqui el cliente con el dni: " + dni), 
            daoCliente.buscarClientePorDni(dni), 
            HttpStatus.OK
        );
    }

    // metodo para dar de alta
    public Respuesta<Cliente> altaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente(clienteDto);

        if (daoCliente.buscarClientePorDni(cliente.getDni()) != null) throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        if (cliente.getEdad() < 18) throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");

        daoCliente.save(cliente);
        return new Respuesta<Cliente>(new MsjResponce("EXITOSA", "Cliente creado con éxito"), cliente, HttpStatus.CREATED);
    }

    // metodo para dar de baja
    public void bajaCliente(Long dni) {
        daoCliente.bajaCliente(dni);
    }

    // mostrar lista de clientes
    public Respuesta<List<Cliente>> darClientes(){
        return new Respuesta<>(
            new MsjResponce("EXITOSA", "Aqui la lista de clientes."), 
            daoCliente.listarClientes(), 
            HttpStatus.OK
        );
    }
}