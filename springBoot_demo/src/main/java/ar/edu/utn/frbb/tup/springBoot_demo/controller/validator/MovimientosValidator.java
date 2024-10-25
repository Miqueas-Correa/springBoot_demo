package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.dto.MovimientosDto;

public class MovimientosValidator {
    public void validate(MovimientosDto movimientosDto) {
        validateNumeroCuenta(movimientosDto.getNumeroCuenta());
        validateDescripcion(movimientosDto.getDescripcion());
    }

    public void validateNumeroCuenta(Long numeroCuenta) {
        if (numeroCuenta == null) throw new IllegalArgumentException("Numero de cuenta no puede ser nulo");
    }

    public void validateDescripcion(String descripcion) {
        if (descripcion == null) throw new IllegalArgumentException("Descripcion no puede ser nulo");
    }
}
