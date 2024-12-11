package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;
@Repository
public class DaoMovimientos extends AbstractBaseDao {
    private static final String FILE_PATH_MOVIMIENTOS = "C:\\Users\\mikim\\OneDrive\\Escritorio\\segundo_anio\\Laboratorio3\\Trabajos\\springBoot_demo\\springBoot_demo\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\springBoot_demo\\base_datos\\Movimientos.txt";

    @Override
    protected String getEntityName() {
        return "Movimientos";
    }

    // listar todos los movimientos
    public List<Movimientos> darMovimientos() {
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
        String[] parts = data.split(";");

        // Verificar la cantidad esperada de partes
        if (parts.length != 6) throw new IOException("Error en el formato del archivo: se esperaban " + 6 + " partes.");

        try {
            Movimientos movimiento = new Movimientos();
            movimiento.setId(Long.parseLong(parts[0]));
            movimiento.setNumeroCuenta(Long.parseLong(parts[1]));
            movimiento.setFecha_y_hs(LocalDateTime.parse(parts[2]));
            movimiento.setDescripcion(parts[3]);
            // parsear los movimientos correspondientes a la cuenta del cliente
            return movimiento;
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new DataAccessException("Error al parsear los datos del movimiento", e);
        }
    }

    // parseo el archivo y lo guardo en un banco
    public void save(MovimientosDto movimientosDto) {
        try {
            Movimientos movimientos = new Movimientos(movimientosDto);
            // genero el id al movimiento
            movimientos.setId(generateId(FILE_PATH_MOVIMIENTOS));
            // guardar el cliente en el archivo
            Files.write(Paths.get(FILE_PATH_MOVIMIENTOS), Collections.singletonList(movimientos.toString()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo guardar el movimiento", e);
        }
    }

    // buscar movimientos por cuenta asociada (numeroCuenta)
    public List<Movimientos> buscar(Long numeroCuenta) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_MOVIMIENTOS));
            List<Movimientos> movimientos = new ArrayList<>();
            for (String line : lines) {
                Movimientos movimiento = parseMovimientos(line);
                if (movimiento != null && movimiento.getNumeroCuenta().equals(numeroCuenta)) {
                    movimientos.add(movimiento);
                }
                return movimientos;
            }
            return Collections.emptyList();
        }catch (IOException e) {
            return Collections.emptyList();
        }
    }
}