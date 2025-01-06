package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import java.time.LocalDate;
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
    void validateMoneda_ValidPesos_NoException() {
        cuentaDto.setMoneda("pesos");
        assertDoesNotThrow(() -> validator.validateMoneda(cuentaDto.getMoneda()));
    }

    @Test
    void validateMoneda_ValidDolares_NoException() {
        cuentaDto.setMoneda("dolares");
        assertDoesNotThrow(() -> validator.validateMoneda(cuentaDto.getMoneda()));
    }

    @Test
    void validateMoneda_CaseInsensitive_NoException() {
        cuentaDto.setMoneda("PESOS");
        assertDoesNotThrow(() -> validator.validateMoneda(cuentaDto.getMoneda()));
    }

    @Test
    void validateMoneda_EmptyString_ThrowsException() {
        cuentaDto.setMoneda("");
        assertThrows(IllegalArgumentException.class, () -> validator.validateMoneda(cuentaDto.getMoneda()));
    }

    @Test
    void validateTipoCuenta_InvalidTipo_ThrowsException() {
        cuentaDto.setTipoCuenta("X");
        assertThrows(IllegalArgumentException.class, () -> validator.validateTipoCuenta(cuentaDto.getTipoCuenta()));
    }

    @Test
    void validateTipoCuenta_ValidC_NoException() {
        cuentaDto.setTipoCuenta("c");
        assertDoesNotThrow(() -> validator.validateTipoCuenta(cuentaDto.getTipoCuenta()));
    }

    @Test
    void validateTipoCuenta_ValidA_NoException() {
        cuentaDto.setTipoCuenta("a");
        assertDoesNotThrow(() -> validator.validateTipoCuenta(cuentaDto.getTipoCuenta()));
    }

    @Test
    void validateTipoCuenta_CaseInsensitive_NoException() {
        cuentaDto.setTipoCuenta("A");
        assertDoesNotThrow(() -> validator.validateTipoCuenta(cuentaDto.getTipoCuenta()));
    }

    @Test
    void validateTitular_NullTitular_ThrowsException() {
        cuentaDto.setTitular(null);
        assertThrows(IllegalArgumentException.class, () -> validator.validateTitular(cuentaDto.getTitular()));
    }

    @Test
    void validateTitular_ValidTitular_NoException() {
        cuentaDto.setTitular(123L);
        assertDoesNotThrow(() -> validator.validateTitular(cuentaDto.getTitular()));
    }

    @Test
    void validateSaldo_NegativeSaldo_ThrowsException() {
        cuentaDto.setSaldo(-100.0);
        assertThrows(IllegalArgumentException.class, () -> validator.validateSaldo(cuentaDto.getSaldo()));
    }

    @Test
    void validateSaldo_ZeroSaldo_NoException() {
        cuentaDto.setSaldo(0.0);
        assertDoesNotThrow(() -> validator.validateSaldo(cuentaDto.getSaldo()));
    }

    @Test
    void validateSaldo_PositiveSaldo_NoException() {
        cuentaDto.setSaldo(1000.0);
        assertDoesNotThrow(() -> validator.validateSaldo(cuentaDto.getSaldo()));
    }

    @Test
    void validateBanco_NullBanco_ThrowsException() {
        cuentaDto.setBanco(null);
        assertThrows(IllegalArgumentException.class, () -> validator.validateBanco(cuentaDto.getBanco()));
    }

    @Test
    void validateBanco_InvalidBanco_ThrowsException() {
        cuentaDto.setBanco("santander");
        assertThrows(IllegalArgumentException.class, () -> validator.validateBanco(cuentaDto.getBanco()));
    }

    @Test
    void validateBanco_ValidNacion_NoException() {
        cuentaDto.setBanco("nacion");
        assertDoesNotThrow(() -> validator.validateBanco(cuentaDto.getBanco()));
    }

    @Test
    void validateBanco_ValidProvincia_NoException() {
        cuentaDto.setBanco("provincia");
        assertDoesNotThrow(() -> validator.validateBanco(cuentaDto.getBanco()));
    }

    @Test
    void validateBanco_CaseInsensitive_NoException() {
        cuentaDto.setBanco("NACION");
        assertDoesNotThrow(() -> validator.validateBanco(cuentaDto.getBanco()));
    }

    // Tests de validación de fecha de creación
    @Test
    void validateFechaCreacion_FutureDate_ThrowsException() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> validator.validateFechaCreacion(futureDate));
    }

    @Test
    void validateFechaCreacion_NullDate_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateFechaCreacion(null));
    }

    @Test
    void validateFechaCreacion_CurrentDate_NoException() {
        assertDoesNotThrow(() -> validator.validateFechaCreacion(LocalDate.now()));
    }

    @Test
    void validateFechaCreacion_PastDate_NoException() {
        assertDoesNotThrow(() -> validator.validateFechaCreacion(LocalDate.now().minusDays(1)));
    }

    @Test
    void validateMovimientos_NullMovimientos_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> validator.validateMovimientos(null));
    }

    @Test
    void validateMovimientos_EmptyList_NoException() {
        List<String> movimientos = new ArrayList<>();
        assertDoesNotThrow(() -> validator.validateMovimientos(movimientos));
    }

    @Test
    void validateMovimientos_ValidList_NoException() {
        List<String> movimientos = new ArrayList<>();
        movimientos.add("Movimiento 1");
        assertDoesNotThrow(() -> validator.validateMovimientos(movimientos));
    }

    @Test
    void validateCompleteAccount_AllValidFields_NoException() {
        cuentaDto.setBanco("nacion");
        cuentaDto.setMoneda("pesos");
        cuentaDto.setTipoCuenta("c");
        cuentaDto.setTitular(123L);
        cuentaDto.setSaldo(1000.0);
        assertDoesNotThrow(() -> validator.validate(cuentaDto));
    }

    @Test
    void validateCompleteAccount_AllValidFieldsAlternative_NoException() {
        cuentaDto.setBanco("provincia");
        cuentaDto.setMoneda("dolares");
        cuentaDto.setTipoCuenta("a");
        cuentaDto.setTitular(456L);
        cuentaDto.setSaldo(2000.0);
        assertDoesNotThrow(() -> validator.validate(cuentaDto));
    }
}