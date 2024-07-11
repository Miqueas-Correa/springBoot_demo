package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.parsistence.DaoCliente;

@Service
public class ServiceCliente {
    DaoCliente daoCliente;

    public ServiceCliente(DaoCliente daoCliente) {
        this.daoCliente = daoCliente;
    }

    // guardar cliente
    public void save(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("El cliente no puede ser nulo");
        daoCliente.save(cliente);
    }

// verificar que el nombre del banco sea nacion o provincia
    public boolean nombre_del_banco(String nombre_del_banco) {
        if (!nombre_del_banco.equalsIgnoreCase("nacion") && !nombre_del_banco.equalsIgnoreCase("provincia")) {
            return false;
        }else{
            return true;
        }
    }

    // metodo para validar email
    public boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // metodo para validar fecha
    public boolean validarFechaNacimiento(String fecha) {
        try {
            LocalDate fechaNac = LocalDate.parse(fecha);
            LocalDate fechaActual = LocalDate.now();
            // isAfter compara dos fechas y devuelve true si la fecha 1 es posterior a la fecha 2
            if (!fechaNac.isAfter(fechaActual)) {
                // between calcula el periodo entre dos fechas
                Period periodo = Period.between(fechaNac, fechaActual);
                // getYears devuelve el número de años entre dos fechas
                return periodo.getYears() >= 18;
            }
            return false;
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha no válido. Por favor, ingrese la fecha en el formato YYYY-MM-DD.");
            return false;
        }
    }

    public Cliente buscarClientePorDni(Long dni) {
        return daoCliente.BuscarClientePorDni(dni);
    }
}