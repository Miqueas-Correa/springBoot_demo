package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Transaccion;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;
@Repository
public class DaoMovimientos extends AbstractBaseDao {
    private static final String FILE_PATH_MOVIMIENTOS = "C:\\Users\\mikim\\OneDrive\\Escritorio\\segundo_anio\\Laboratorio3\\Trabajos\\springBoot_demo\\springBoot_demo\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\springBoot_demo\\base_datos\\Movimientos.txt";

    @Override
    protected String getEntityName() {
        return "Movimientos";
    }

    public Movimientos buscar(Long numeroCuenta) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_MOVIMIENTOS));
            List<Transaccion> transacciones = new ArrayList<>();
            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length != 5) {
                    throw new IOException("Se esperaban 5 elementos en la línea: " + line);
                }
                Long cuentaActual = Long.parseLong(parts[0]);
                if (cuentaActual.equals(numeroCuenta)) {
                    Transaccion transaccion = new Transaccion();
                    transaccion.setFecha(LocalDate.parse(parts[1]));
                    transaccion.setTipo(parts[2]);
                    transaccion.setDescripcionBreve(parts[3]);
                    transaccion.setMonto(Double.parseDouble(parts[4]));
                    transacciones.add(transaccion);
                }
            }
            if (transacciones.isEmpty()) return null;

            return new Movimientos(numeroCuenta, transacciones);
        } catch (IOException e) {
            throw new DataAccessException("Error al leer los movimientos", e);
        }
    }

    public void save(Movimientos movimientos) {
        try {
            List<String> lines = movimientos.getTransacciones().stream()
                    .map(t -> movimientos.getNumeroCuenta() + ";" + t.toString())
                    .collect(Collectors.toList());

            Files.write(Paths.get(FILE_PATH_MOVIMIENTOS), lines, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new DataAccessException("Error al guardar los movimientos", e);
        }
    }
}