package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CuentaValidatorTest {

    private CuentaValidator validator;
    private CuentaDto cuentaDto;

    @BeforeEach
    void setUp() {
        validator = new CuentaValidator();
        cuentaDto = new CuentaDto();
    }

    @Test
    void validateMoneda_InvalidMoneda_ThrowsException() {
        cuentaDto.setMoneda("X");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(cuentaDto));
    }

    @Test
    void validateTipoCuenta_InvalidTipo_ThrowsException() {
        cuentaDto.setTipoCuenta("X");
        assertThrows(IllegalArgumentException.class, () -> validator.validate(cuentaDto));
    }

    @Test
    void validateTitular_NullTitular_ThrowsException() {
        cuentaDto.setTitular(null);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(cuentaDto));
    }

    @Test
    void validateSaldo_NegativeSaldo_ThrowsException() {
        cuentaDto.setSaldo(-100.0);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(cuentaDto));
    }

    @Test
    void validateNumeroCuenta_NullNumeroCuenta_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateNumeroCuenta(null));
    }

    @Test
    void validateMovimientos_NullMovimientos_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateMovimientos(null));
    }

    @Test
    void validateEstaA_NullEstaA_ThrowsException() {
        cuentaDto.setEstaA(null);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(cuentaDto));
    }

    @Test
    void validateFechaCreacion_FutureDate_ThrowsException() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> validator.validateFechaCreacion(futureDate));
    }

    @Test
    void validateFechaCreacion_NullDate_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateFechaCreacion(null));
    }

    @Test
    void validateMovimientos_ValidList_NoException() {
        List<String> movimientos = new ArrayList<>();
        movimientos.add("Movimiento 1");
        assertDoesNotThrow(() -> validator.validateMovimientos(movimientos));
    }

    @Test
    void validateSaldo_ZeroSaldo_NoException() {
        assertDoesNotThrow(() -> validator.validateSaldo(0.0));
    }

    @Test
    void validateSaldo_PositiveSaldo_NoException() {
        assertDoesNotThrow(() -> validator.validateSaldo(100.0));
    }
}
