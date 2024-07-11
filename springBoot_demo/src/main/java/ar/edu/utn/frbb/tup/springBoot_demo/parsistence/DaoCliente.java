package ar.edu.utn.frbb.tup.springBoot_demo.parsistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Banco;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;

public class DaoCliente extends AbstractBaseDao {
    private static final String FILE_PATH_CLIENTES = "App\\src\\main\\java\\ar\\utn\\frbb\\tup\\base_datos\\Clientes.txt";
    private DaoCuenta daoCuenta = new DaoCuenta();
    @Override
    protected String getEntityName() {
        return "Cliente";
    }

    // modificar el cliente
    public void updateCliente(Cliente cliente) {
        try {
            // Leer todas las líneas del archivo
            List<String> lineas = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));

            // Buscar y modificar la línea que corresponde al cliente
            for (int i = 0; i < lineas.size(); i++) {
                if (lineas.get(i).startsWith(String.valueOf(cliente.getDni()))) {
                    // Crear la nueva línea con los datos actualizados del cliente
                    String updatedClienteData = cliente.getDni() + ";"
                        + cliente.getNombre_y_apellido() + ";"
                        + cliente.getDireccion() + ";"
                        + cliente.getTelefono() + ";"
                        + cliente.getEmail() + ";"
                        + cliente.getFecha_de_nacimiento() + ";"
                        + cliente.getBanco() + ";"
                        + cliente.getFecha_de_alta();
                // TODO FALTA LAS CUENTAS
                    // Actualizar la línea en la lista
                    lineas.set(i, updatedClienteData);
                    break;
                }
            }
            // Sobrescribir el archivo con las líneas actualizadas
            Files.write(Paths.get(FILE_PATH_CLIENTES), lineas);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo actualizar el cliente", e);
        }
    }

    // parseo el archivo y lo guardo en un banco
    public void save(Cliente cliente) {
        String clienteData = cliente.getDni() + ";"
        + cliente.getNombre_y_apellido() + ";"
        + cliente.getDireccion() + ";"
        + cliente.getTelefono() + ";"
        + cliente.getEmail() + ";"
        + cliente.getFecha_de_nacimiento() + ";"
        + cliente.getBanco() + ";"
        + cliente.getFecha_de_alta();

        try {
            // guardo las cuentas que tenga el cliente
            for (Cuenta cuenta : cliente.getCuentas()) {
                this.daoCuenta.save(cuenta);
            }

            // guardar el cliente en el archivo
            Files.write(Paths.get(FILE_PATH_CLIENTES), Collections.singletonList(clienteData), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo guardar el cliente", e);
        } catch (DataAccessException e) {
            throw new DataAccessException("No se pudo guardar las cuentas del cliente", e);
        }
    }

    // pongo este metodo en esta clase porque me parece que es mas de cliente que banco
    public Banco parseBanco() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            Banco banco = new Banco();
            for (String line : lines) {
                Cliente cliente = parseCliente(line);
                if (cliente != null) {
                    banco.setClientes(cliente);
                    banco.agregarDni(cliente.getDni());
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("No se pudo leer el cliente", e);
        }
        return new Banco();
    }

    // parseo el archivo y lo guardo en un cliente
    private Cliente parseCliente(String data) {
        String[] parts = data.split(";");
        if (parts.length >= 6) {
            Cliente cliente = new Cliente();
            cliente.setDni(Long.parseLong(parts[0]));
            cliente.setNombre_y_apellido(parts[1]);
            cliente.setDireccion(parts[2]);
            cliente.setTelefono(Long.valueOf(parts[3]));
            cliente.setEmail(parts[4]);
            cliente.setFecha_de_nacimiento(LocalDate.parse(parts[5]));
            cliente.setDni(Long.valueOf(parts[6]));

            // parsear las cuentas correspondientes al cliente del cliente
            return cliente;
        }
        return null;
    }

    public Cliente BuscarClientePorDni(Long dni) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            for (String line : lines) {
                Cliente cliente = parseCliente(line);
                if (cliente.getDni().equals(dni)) {
                    return cliente;
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el cliente");
        }
        return null;
    }
}
