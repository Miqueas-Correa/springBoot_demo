package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.CuentaDto;

@Component
public class CuentaValidator {
    public void validate(CuentaDto cuentaDto) {
        validateMoneda(cuentaDto.getMoneda());
        validateTipoCuenta(cuentaDto.getTipoCuenta());
        validateTitular(cuentaDto.getTitular());
        validateSaldo(cuentaDto.getSaldo());
        validateMovimientos(cuentaDto.getMovimientos());
        validateEstaA(cuentaDto.getEstaA());
        validateFechaCreacion(cuentaDto.getFechaCreacion());
    }

    private void validateTipoCuenta(String tipoCuenta) {
        if (!"C".equals(tipoCuenta) || !"A".equals(tipoCuenta)) throw new IllegalArgumentException("Tipo de cuenta no soportada");
    }

    private void validateMoneda(String moneda) {
        if (!"P".equals(moneda) ||!"D".equals(moneda)) throw new IllegalArgumentException("Moneda no soportada");
    }

    public void validateTitular(Long titular) {
        if (titular == null) throw new IllegalArgumentException("Titular no puede ser nulo");
    }

    public void validateSaldo(double saldo) {
        if (saldo < 0) throw new IllegalArgumentException("Saldo no puede ser negativo");
    }

    public void validateNumeroCuenta(Long numeroCuenta) {
        if (numeroCuenta == null) throw new IllegalArgumentException("Numero de cuenta no puede ser nulo");
    }

    public void validateMovimientos(List<String> movimientos) {
        if (movimientos == null) throw new IllegalArgumentException("Movimientos no puede ser nulo");
    }

    public void validateEstaA(Boolean estaA) {
        if (estaA == null) throw new IllegalArgumentException("EstaA no puede ser nulo");
    }

    public void validateFechaCreacion(LocalDateTime fechaCreacion) {
        if (fechaCreacion == null) throw new IllegalArgumentException("FechaCreacion no puede ser nulo");
        // la fecha no puede ser mayor a la fecha actual
        if (fechaCreacion.isAfter(LocalDateTime.now())) throw new IllegalArgumentException("FechaCreacion no puede ser mayor a la fecha actual");
    }
}