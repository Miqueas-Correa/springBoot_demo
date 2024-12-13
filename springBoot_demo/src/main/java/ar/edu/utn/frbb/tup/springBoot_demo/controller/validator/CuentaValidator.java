package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;

@Component
public class CuentaValidator {
    public void validate(CuentaDto cuentaDto) {
        validateMoneda(cuentaDto.getMoneda());
        validateTipoCuenta(cuentaDto.getTipoCuenta());
        validateTitular(cuentaDto.getTitular());
        validateSaldo(cuentaDto.getSaldo());
    }

    private void validateTipoCuenta(String tipoCuenta) {
        if (tipoCuenta == null || (!"c".equalsIgnoreCase(tipoCuenta) && !"a".equalsIgnoreCase(tipoCuenta))) throw new IllegalArgumentException("Tipo de cuenta no soportada");
    }

    private void validateMoneda(String moneda) {
        if (moneda == null || (!"pesos".equalsIgnoreCase(moneda) && !"dolares".equalsIgnoreCase(moneda))) throw new IllegalArgumentException("Moneda no soportada");
    }

    public void validateTitular(Long titular) {
        if (titular == null) throw new IllegalArgumentException("Titular no puede ser nulo");
    }

    public void validateSaldo(Double saldo) {
        if (saldo < 0) throw new IllegalArgumentException("Saldo no puede ser negativo");
    }

    public void validateNumeroCuenta(Long numeroCuenta) {
        if (numeroCuenta == null) throw new IllegalArgumentException("Numero de cuenta no puede ser nulo");
    }

    public void validateMovimientos(List<String> movimientos) {
        if (movimientos == null) throw new IllegalArgumentException("Movimientos no puede ser nulo");
    }

    public void validateFechaCreacion(LocalDate fechaCreacion) {
        if (fechaCreacion == null) throw new IllegalArgumentException("FechaCreacion no puede ser nulo");
        // la fecha no puede ser mayor a la fecha actual
        if (fechaCreacion.isAfter(LocalDate.now())) throw new IllegalArgumentException("FechaCreacion no puede ser mayor a la fecha actual");
    }
}