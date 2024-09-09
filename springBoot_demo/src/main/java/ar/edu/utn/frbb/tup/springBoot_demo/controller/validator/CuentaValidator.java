package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.CuentaDto;

@Component
public class CuentaValidator {
    public void validate(CuentaDto cuentaDto) {
        validateMoneda(cuentaDto.getMoneda());
        validateTipoCuenta(cuentaDto.getTipoCuenta());
    }

    private void validateTipoCuenta(String tipoCuenta) {
        if (!"C".equals(tipoCuenta) || !"A".equals(tipoCuenta)) {
            throw new IllegalArgumentException("Tipo de cuenta no soportada");
        }
    }

    private void validateMoneda(String moneda) {
        if (!"P".equals(moneda) ||!"D".equals(moneda)) {
            throw new IllegalArgumentException("Moneda no soportada");
        }
    }
}