package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.InvalidAgeException;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCliente;

@Service
public class ServiceCliente {
    private DaoCliente daoCliente;

    public ServiceCliente(DaoCliente daoCliente) {
        this.daoCliente = daoCliente;
    }

    // modificar el cliente
    public Cliente update(Cuenta clienteDto, long dni) {
        // no nesesito validar xq se encarga el cliente validator antes de llegar aca
        // acutualizo y devuelvo el cliente actualizado
        return daoCliente.updateCliente(clienteDto, dni);
    }

    // guardar cliente
    public void save(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("El cliente no puede ser nulo");
        daoCliente.save(cliente);
    }

    // verificar que el nombre del banco sea nacion o provincia, que solo tenga solo estos 2 es solo tema de diseño
    public boolean nombre_del_banco(String nombre_del_banco) {
        return !nombre_del_banco.equalsIgnoreCase("nacion") && !nombre_del_banco.equalsIgnoreCase("provincia");
    }

    // metodo para validar email
    public boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // metodo para validar fecha
    public boolean validarFechaNacimiento(LocalDate fechaNac) {
        try {
            // isAfter compara dos fechas y devuelve true si la fecha 1 es posterior a la fecha 2
            if (!fechaNac.isAfter(LocalDate.now())) {
                // between calcula el periodo entre dos fechas
                Period periodo = Period.between(fechaNac, LocalDate.now());
                // getYears devuelve el número de años entre dos fechas
                return periodo.getYears() >= 18;
            }
            throw new InvalidAgeException("La edad no puede ser en el futuro.");
        } catch (DateTimeParseException e) {
            throw new InvalidAgeException("Formato de fecha no válido. Por favor, ingrese la fecha en el formato YYYY-MM-DD.");
        }
    }

    public Cliente buscarClientePorDni(Long dni) {
        return daoCliente.buscarClientePorDni(dni);
    }

    // metodo para dar de alta
    public Cliente darDeAltaCliente(Cuenta clienteDto) throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente(clienteDto);

        if (daoCliente.buscarClientePorDni(cliente.getDni()) != null) throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        if (cliente.getEdad() < 18) throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");

        daoCliente.save(cliente);
        return cliente;
    }

    // metodo para dar de baja
    public void darDeBajaCliente(Long dni) {
        daoCliente.deleteCliente(dni);
    }

    // mostrar lista de clientes
    public List<Cliente> darClientes(){
        return daoCliente.listarClientes();
    }
}