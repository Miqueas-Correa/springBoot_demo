package ar.edu.utn.frbb.tup.springBoot_demo.parsistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;

public class DaoCuenta extends AbstractBaseDao {
    private static final String FILE_PATH_CUENTAS = "App\\src\\main\\java\\ar\\utn\\frbb\\tup\\base_datos\\Cuentas.txt";

    @Override
    protected String getEntityName() {
        return "Cuenta";
    }

    // guardar la cuenta en el archivo
    public static void save(Cuenta cuenta) {
        String cuentaData = cuenta.getEstaA() + ";"
        + cuenta.getId_del_titular() + ";"
        + cuenta.getSaldo() + ";"
        + cuenta.getAlias() + ";"
        + cuenta.getCbu_cvu() + ";"
        + cuenta.getTipo_de_cuenta() + ";"
        + cuenta.getMoneda() + ";"
        + cuenta.getFecha_de_apertura() + ";"
        + cuenta.getMovimientos();

        // guardar la cuenta en el archivo
        try {
            Files.write(Paths.get(FILE_PATH_CUENTAS), Collections.singletonList(cuentaData), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new DataAccessException("No se pudo guardar la cuenta", e);
        }
    }

    // parseo el archivo y lo guardo en un cliente
    public static Cuenta parseCuenta(String data) {
        String[] parts = data.split(";");
        if (parts.length >= 9) {
            Cuenta cuenta = new Cuenta();
            cuenta.setMovimientos(new ArrayList<>());
            cuenta.setEstaA(Boolean.parseBoolean(parts[0]));
            cuenta.setId_del_titular(Long.valueOf(parts[1]));
            cuenta.setSaldo(Double.parseDouble(parts[2]));
            cuenta.setAlias(parts[3]);
            return cuenta;
        }
        return null;
    }
}