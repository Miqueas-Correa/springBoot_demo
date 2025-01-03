package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.InvalidAgeException;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class ClienteValidator {
    public void validate(ClienteDto clienteDto) throws ClienteAlreadyExistsException, InvalidAgeException {
        validateDni(clienteDto.getDni());
        validateNombre(clienteDto.getNombre_y_apellido());
        validateFechaNacimiento(clienteDto.getFechaNacimiento());
        validateTelefono(clienteDto.getTelefono());
        validateEmail(clienteDto.getEmail());
    }

    // metodos
    // validar telefono
    private void validateTelefono(Long telefono) {
        if (telefono == null) throw new IllegalArgumentException("El telefono no puede ser nulo");
        if (telefono.toString().length() != 10) throw new IllegalArgumentException("El telefono debe tener 10 digitos");
    }
    // validar email
    private void validateEmail(String email) {
        if (email == null) throw new IllegalArgumentException("El email no puede ser nulo");
        if (email.length() > 30) throw new IllegalArgumentException("El email no puede tener mas de 30 caracteres");
        if (!validarEmail(email)) throw new IllegalArgumentException("El email debe ser de esta manera: ejemplo@ejemplo.com");
    }
    private static Boolean validarEmail(String email) {
        String regex = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$";
        return email != null && email.matches(regex);
    }
    // validar dni
    private void validateDni(Long dni) throws ClienteAlreadyExistsException {
        if (dni == null) throw new IllegalArgumentException("El DNI no puede ser nulo.");
        if (String.valueOf(dni).length() != 8) throw new IllegalArgumentException("El DNI debe tener 8 dígitos.");
        if (String.valueOf(dni).charAt(0) == '0') throw new IllegalArgumentException("El DNI no puede empezar con 0.");
    }
    // validar nombre
    private void validateNombre(String nombre_y_apellido) {
        if (nombre_y_apellido == null) throw new IllegalArgumentException("El nombre y el apellido no pueden ser nulos.");
        if (nombre_y_apellido.trim().isEmpty()) throw new IllegalArgumentException("El nombre y el apellido no pueden estar vacíos.");
    }
    // validar fecha de nacimiento
    private void validateFechaNacimiento(LocalDate fechaNacimiento)throws InvalidAgeException {
        if (fechaNacimiento == null) throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula.");
    }
}