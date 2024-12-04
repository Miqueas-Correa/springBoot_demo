package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;

public class DaoCuenta extends AbstractBaseDao {
    private static final String FILE_PATH_CUENTAS = "App\\src\\main\\java\\ar\\utn\\frbb\\tup\\base_datos\\Cuentas.txt";

    @Override
    protected String getEntityName() {
        return "Cuenta";
    }

    // listar cuentas
    public List<Cuenta> listarCuentas() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CUENTAS));
            List<Cuenta> cuentas = new ArrayList<>();
            for (String line : lines) {
                Cuenta cuenta = parseCuenta(line);
                if (cuenta!= null) cuentas.add(cuenta);
            }
            return cuentas;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    // buscar cuenta por numero de cuenta
    public Cuenta buscarCuenta(Long numeroCuenta) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CUENTAS));
            for (String line : lines) {
                Cuenta cuenta = parseCuenta(line);
                if (cuenta.getNumeroCuenta().equals(numeroCuenta)) return cuenta;
            }
        } catch (IOException e) {
            // lanza una excepción si no se puede leer el archivo
            throw new DataAccessException("Error al leer el archivo de cuentas", e);
        }
        return null;
    }

    // listar las cuentas del cliente
    public List<Cuenta> listarCuentas(long titular) {
        List<Cuenta> cuentasFiltradas = new ArrayList<>();
        try {
            // Leer todas las líneas del archivo
            List<String> lineas = Files.readAllLines(Paths.get(FILE_PATH_CUENTAS));
            // Filtrar las líneas que corresponden a las cuentas del cliente
            for (String linea : lineas) {
                Cuenta cuenta = parseCuenta(linea);
                if (cuenta.getTitular().equals(titular)) {
                    cuentasFiltradas.add(cuenta);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de cuentas");
        }
        return cuentasFiltradas == null ? Collections.emptyList() : cuentasFiltradas;
    }

    // actualizo la cuenta
    public Cuenta update(CuentaDto cuentaDto, long numeroCuenta) {
        try {
            // Leer todas las líneas del archivo
            List<String> lineas = Files.readAllLines(Paths.get(FILE_PATH_CUENTAS));
            boolean cuentaActualizada = false;
            Cuenta cuenta = buscarCuenta(numeroCuenta);

            // Buscar y modificar la línea que corresponde a la cuenta del cliente
            for (int i = 0; i < lineas.size(); i++) {
                if (lineas.get(i).startsWith(String.valueOf(numeroCuenta))) {
                    if (cuenta != null) {
                        // Considero que solo estos datos pueden ser modificados
                        cuenta.setEstaA(cuentaDto.getEstaA());
                        cuenta.setSaldo(cuentaDto.getSaldo());
                        cuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
                        cuenta.setMoneda(cuentaDto.getMoneda());

                        // Crear la nueva línea con los datos actualizados
                        String updatedCuentaData = cuenta.toString();
                        // Actualizar la línea en la lista
                        lineas.set(i, updatedCuentaData);
                        cuentaActualizada = true;
                        break;
                    }
                }
            }

            if (!cuentaActualizada) throw new DataAccessException("La cuenta no fue encontrada para actualizar.", null);

            // Sobrescribir el archivo con las líneas actualizadas
            Files.write(Paths.get(FILE_PATH_CUENTAS), lineas);

            return cuenta;
        } catch (IOException e) {
            throw new DataAccessException("No se pudo actualizar la cuenta", e);
        }
    }

    // el titular es el dni del cliente al cual pertenece la cuenta
    public Cuenta buscarCuentaPorTitular(Long titular) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH_CUENTAS));
            for (String line : lines) {
                Cuenta cuenta = parseCuenta(line);
                if (cuenta.getTitular().equals(titular)) return cuenta;
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el cliente");
        }
        return null;
    }

    // guardar la cuenta en el archivo
    // parseo el archivo y lo guardo en un banco
    public void save(Cuenta cuenta) {
        try {
            // genero el numero de cuenta
            cuenta.setNumeroCuenta(generateId(FILE_PATH_CUENTAS));
            // guardar cuenta en el archivo
            Files.write(Paths.get(FILE_PATH_CUENTAS), Collections.singletonList(cuenta.toString() + System.lineSeparator()), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo guardar la cuenta correctamente", e);
        }
    }

    // parseo el archivo y lo guardo en una cuenta
    public Cuenta parseCuenta(String data) {
        String[] parts = data.split(";");
        if (parts.length >= 8) {
            Cuenta cuenta = new Cuenta();
            cuenta.setNumeroCuenta(Long.valueOf(parts[0]));
            cuenta.setTitular(Long.valueOf(parts[1]));
            cuenta.setEstaA(Boolean.parseBoolean(parts[2]));
            cuenta.setSaldo(Double.parseDouble(parts[3]));
            cuenta.setTipoCuenta(parts[4]);
            cuenta.setMoneda(parts[6]);
            cuenta.setFechaCreacion(LocalDateTime.parse(parts[7]));
            return cuenta;
        }
        return null;
    }
}