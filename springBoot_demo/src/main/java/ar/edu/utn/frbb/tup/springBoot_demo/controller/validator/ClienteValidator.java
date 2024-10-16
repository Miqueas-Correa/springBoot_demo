package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.InvalidAgeException;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteValidator {
    @Autowired
    private ServiceCliente serviceCliente;

    public void validate(Cuenta clienteDto) throws ClienteAlreadyExistsException, InvalidAgeException {
        validateDni(clienteDto.getDni());
        validateNombre(clienteDto.getNombre_y_apellido());
        validateFechaNacimiento(clienteDto.getFechaNacimiento());
        validateBanco(clienteDto.getBanco());
        validateTelefono(clienteDto.getTelefono());
        validateEmail(clienteDto.getEmail());
    }

    // metodos
    // validar telefono
    private void validateTelefono(Long telefono) {
        if (telefono == null) throw new IllegalArgumentException("El telefono no puede ser nulo");
        if (telefono < 10000000 || telefono > 99999999) throw new IllegalArgumentException("El telefono debe tener 8 digitos");
        if (telefono.toString().length() > 8) throw new IllegalArgumentException("El telefono debe tener 8 digitos");
    }
    // validar email
    private void validateEmail(String email) {
        if (email == null) throw new IllegalArgumentException("El email no puede ser nulo");
        if (email.length() > 30) throw new IllegalArgumentException("El email no puede tener mas de 30 caracteres");
        if (serviceCliente.validarEmail(email)) throw new IllegalArgumentException("El email debe ser de esta manera: ejemplo@ejemplo.com");
    }
    // validar banco
    private void validateBanco(String banco) {
        if (!serviceCliente.nombre_del_banco(banco)) throw new IllegalArgumentException("El banco debe ser nacion o provincia");
    }
    // validar dni
    private void validateDni(Long dni) throws ClienteAlreadyExistsException {
        if (String.valueOf(dni).length() != 8 || dni == null) throw new IllegalArgumentException("El DNI debe tener 8 digitos y no puede ser nulo.");
        if (serviceCliente.buscarClientePorDni(dni) != null) throw new ClienteAlreadyExistsException("El DNI ya existe");
        if (String.valueOf(dni).charAt(0) == '0') throw new IllegalArgumentException("El DNI no puede empezar con 0");
    }
    // validar nombre
    private void validateNombre(String nombre_y_apellido) {
        nombre_y_apellido = nombre_y_apellido.trim();
        if (nombre_y_apellido == null) throw new IllegalArgumentException("El nombre y el apellido no puede ser nulo.");
        if (nombre_y_apellido.equals("")) throw new IllegalArgumentException("El nombre y el apellido no puede estar vacío.");
    }
    // validar fecha de nacimiento
    private void validateFechaNacimiento(LocalDate fechaNacimiento)throws InvalidAgeException {
        if (fechaNacimiento == null) throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula.");
        if (!serviceCliente.validarFechaNacimiento(fechaNacimiento)) throw new InvalidAgeException("La persona debe tener al menos 18 años.");
    }
}