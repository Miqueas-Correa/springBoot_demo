package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;

public class DaoMovimientos extends AbstractBaseDao {
    private static final String FILE_PATH_MOVIMIENTOS = "App\\src\\main\\java\\ar\\utn\\frbb\\tup\\base_datos\\Movimientos.txt";

    @Override
    protected String getEntityName() {
        return "Movimientos";
    }

    // listar todos los movimientos
    public List<Movimientos> listarMovimientos() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_MOVIMIENTOS));
            List<Movimientos> movimientos = new ArrayList<>();
            for (String line : lines) {
                Movimientos movimiento = parseMovimientos(line);
                if (movimiento!= null) movimientos.add(movimiento);
            }
            return movimientos;
        }catch (IOException e) {
            return Collections.emptyList();
        }
    }

    // parseo los movimientos
    private Movimientos parseMovimientos(String data) throws IOException {
        try {
            String[] parts = data.split(";");
            // hago saltar la excepcion si las partes no son 6
            if (parts.length!= 6) throw new IOException("Error en el formato del archivo");
            if (parts.length == 6) {
                Movimientos movimiento = new Movimientos();
                movimiento.setNumeroCuenta(Long.parseLong(parts[0]));
                movimiento.setFecha(LocalDate.parse(parts[1]));
                movimiento.setHora(LocalTime.parse(parts[3]));
                movimiento.setDescripcion(parts[4]);
                if (movimiento != null) {
                    // parsear los movimientos correspondientes a la cuenta del cliente
                    return movimiento;
                }
            }
            return null;
        } catch (IOException e) {
            throw new DataAccessException("Error en el formato del archivo", e);
        }
    }

    // modificar el moviimiento
    public Movimientos updateCliente(Movimientos clienteDto, long id) {
        try {
            // Leer todas las líneas del archivo
            List<String> lineas = Files.readAllLines(Paths.get(FILE_PATH_MOVIMIENTOS));

            // Buscar y modificar la línea que corresponde al cliente
            for (int i = 0; i < lineas.size(); i++) {
                if (lineas.get(i).startsWith(String.valueOf(id))) {
                    Movimientos movimientos = buscarMovimientoPorId(id); // no valido que no sea nulo xq se valida antes
                    // considero que solo estos datos pueden ser modificados
                    movimientos.setNombre_y_apellido(clienteDto.getNombre_y_apellido());
                    movimientos.setBanco(clienteDto.getBanco());
                    movimientos.setEmail(clienteDto.getEmail());
                    movimientos.setTelefono(clienteDto.getTelefono());
                    // Crear la nueva línea con los datos actualizados del cliente
                    String updatedClienteData = movimientos.toString();
                    // Actualizar la línea en la lista
                    lineas.set(i, updatedClienteData);
                    // devuelve el cliente actualizado
                    return movimientos;
                }
            }
            // Sobrescribir el archivo con las líneas actualizadas
            Files.write(Paths.get(FILE_PATH_CLIENTES), lineas);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo actualizar el cliente", e);
        }
        // excepcion si no se encuentra el cliente
        throw new ClienteNotFoundException("Cliente con DNI " + id + " no encontrado");
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
}