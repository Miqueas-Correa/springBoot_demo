package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;

public class DaoCliente extends AbstractBaseDao {
    private static final String FILE_PATH_CLIENTES = "App\\src\\main\\java\\ar\\utn\\frbb\\tup\\base_datos\\Clientes.txt";

    @Override
    protected String getEntityName() {
        return "Cliente";
    }

    // listar todos los clientes
    public List<Cliente> listarClientes() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            List<Cliente> clientes = new ArrayList<>();
            for (String line : lines) {
                Cliente cliente = parseCliente(line);
                if (cliente!= null) clientes.add(cliente);
            }
            return clientes;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    // modificar el cliente
    public Cliente updateCliente(ClienteDto clienteDto, long dni) {
        try {
            // Leer todas las líneas del archivo
            List<String> lineas = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));

            // Buscar y modificar la línea que corresponde al cliente
            for (int i = 0; i < lineas.size(); i++) {
                if (lineas.get(i).startsWith(String.valueOf(dni))) {
                    Cliente cliente = buscarClientePorDni(dni); // no valido que no sea nulo xq se valida antes
                    // considero que solo estos datos pueden ser modificados
                    cliente.setNombre_y_apellido(clienteDto.getNombre_y_apellido());
                    cliente.setBanco(clienteDto.getBanco());
                    cliente.setEmail(clienteDto.getEmail());
                    cliente.setTelefono(clienteDto.getTelefono());
                    // Crear la nueva línea con los datos actualizados del cliente
                    String updatedClienteData = cliente.toString();
                    // Actualizar la línea en la lista
                    lineas.set(i, updatedClienteData);
                    // devuelve el cliente actualizado
                    return cliente;
                }
            }
            // Sobrescribir el archivo con las líneas actualizadas
            Files.write(Paths.get(FILE_PATH_CLIENTES), lineas);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo actualizar el cliente", e);
        }
        // excepcion si no se encuentra el cliente
        throw new ClienteNotFoundException("Cliente con DNI " + dni + " no encontrado");
    }

    // parseo el archivo y lo guardo en un banco
    public void save(Cliente cliente) {
        try {
            // guardar el cliente en el archivo
            Files.write(Paths.get(FILE_PATH_CLIENTES), Collections.singletonList(cliente.toString()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo guardar el cliente", e);
        }
    }

    // parseo el archivo y lo guardo en un cliente
    private Cliente parseCliente(String data) throws IOException {
        try {
            String[] parts = data.split(";");
            // hago saltar la excepcion si las partes no son 6
            if (parts.length!= 6) throw new IOException("Error en el formato del archivo");
            if (parts.length == 6) {
                Cliente cliente = new Cliente();
                cliente.setDni(Long.parseLong(parts[0]));
                cliente.setNombre_y_apellido(parts[1]);
                cliente.setTelefono(Long.valueOf(parts[3]));
                cliente.setEmail(parts[4]);
                cliente.setFecha_de_nacimiento(LocalDate.parse(parts[5]));
                cliente.setDni(Long.valueOf(parts[6]));
                if (cliente != null) {
                    // parsear las cuentas correspondientes al cliente del cliente
                    return cliente;
                }
            }
            return null;
        } catch (IOException e) {
            throw new DataAccessException("Error en el formato del archivo", e);
        }
    }

    public Cliente buscarClientePorDni(Long dni) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            for (String line : lines) {
                Cliente cliente = parseCliente(line);
                if (cliente.getDni().equals(dni)) return cliente;
            }
        } catch (IOException e) {
            throw new DataAccessException("No se pudo buscar el cliente", e);
        }
        return null;
    }

    public void deleteCliente(Long dni) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            List<String> updatedLines = new ArrayList<>();
            for (String line : lines) {
                Cliente cliente = parseCliente(line);
                // saltea el cliente que se quiere eliminar
                if (cliente.getDni().equals(dni)) continue;
                updatedLines.add(line);
            }
            Files.write(Paths.get(FILE_PATH_CLIENTES), updatedLines);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo eliminar el cliente", e);
        }
    }
}