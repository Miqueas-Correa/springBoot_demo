package ar.edu.utn.frbb.tup.springBoot_demo.persistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;

@Repository
public class DaoCliente extends AbstractBaseDao {
    private static final String FILE_PATH_CLIENTES = "C:\\Users\\mikim\\OneDrive\\Escritorio\\segundo_anio\\Laboratorio3\\Trabajos\\springBoot_demo\\springBoot_demo\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\springBoot_demo\\base_datos\\Clientes.txt";

    @Override
    protected String getEntityName() {
        return "Cliente";
    }

    public List<Cliente> listarClientes() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            if (lines.isEmpty()) return Collections.emptyList();
            List<Cliente> clientes = new ArrayList<>();
            for (String line : lines) {
                Cliente cliente = parseCliente(line);
                clientes.add(cliente);
            }
            return clientes;
        } catch (IOException e) {
            throw new DataAccessException("Error al leer el archivo de clientes", e);
        }
    }

    private Cliente parseCliente(String data) {
        String[] parts = data.split(";");
        if (parts.length != 6) throw new DataAccessException("Formato incorrecto en línea: " + data, null);

        try {
            Cliente cliente = new Cliente();
            cliente.setDni(Long.parseLong(parts[0]));
            cliente.setNombre_y_apellido(parts[1]);
            cliente.setTelefono(Long.parseLong(parts[2]));
            cliente.setEmail(parts[3]);
            cliente.setFecha_de_nacimiento(LocalDate.parse(parts[4]));
            cliente.setFecha_de_alta(LocalDate.parse(parts[5]));
            return cliente;
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new DataAccessException("Error en el formato del cliente", e);
        }
    }

    public Cliente updateCliente(ClienteDto clienteDto, long dni) {
        if (clienteDto == null) throw new IllegalArgumentException("ClienteDto no puede ser nulo");

        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            boolean clienteModificado = false;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(String.valueOf(dni))) {
                    Cliente cliente = buscarClientePorDni(dni);
                    cliente.setDni(clienteDto.getDni());
                    cliente.setNombre_y_apellido(clienteDto.getNombre_y_apellido());
                    cliente.setEmail(clienteDto.getEmail());
                    cliente.setTelefono(clienteDto.getTelefono());

                    lines.set(i, cliente.toString());
                    clienteModificado = true;
                    break;
                }
            }

            if (!clienteModificado) throw new ClienteNotFoundException("Cliente con DNI " + dni + " no encontrado");

            Files.write(Paths.get(FILE_PATH_CLIENTES), lines);
            return buscarClientePorDni(dni);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo actualizar el cliente", e);
        }
    }

    public void save(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no puede ser nulo");
        }

        try {
            Files.write(
                Paths.get(FILE_PATH_CLIENTES),
                Collections.singletonList(cliente.toString()),
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new DataAccessException("No se pudo guardar el cliente", e);
        }
    }

    public Cliente buscarClientePorDni(Long dni) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            for (String line : lines) {
                Cliente cliente = parseCliente(line);
                if (cliente.getDni().equals(dni)) {
                    return cliente;
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("No se pudo buscar el cliente", e);
        }
        return null;
    }

    public void bajaCliente(Long dni) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CLIENTES));
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                Cliente cliente = parseCliente(line);
                if (!cliente.getDni().equals(dni)) {
                    updatedLines.add(line);
                }
            }

            Files.write(Paths.get(FILE_PATH_CLIENTES), updatedLines);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo eliminar el cliente", e);
        }
    }
}