package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TransferirValidatorTest {

    @Mock
    private ServiceCuenta serviceCuenta;

    @InjectMocks
    private TransferirValidator transferirValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateMontoNegativo() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(-100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "El monto debe ser mayor a 0.");
    }

    @Test
    void validateCuentasIguales() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(1L);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "Las cuentas no pueden ser iguales.");
    }

    @Test
    void validateCuentaOrigenNoExiste() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "La cuenta de origen no existe.");
    }

    @Test
    void validateCuentaDestinoNoExiste() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);

        Cuenta cuentaOrigen = new Cuenta();
        when(serviceCuenta.buscarCuenta(1L)).thenReturn(cuentaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "La cuenta de destino no existe.");
    }

    @Test
    void validateSaldoNegativo() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setSaldo(-50.0);
        Cuenta cuentaDestino = new Cuenta();

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(cuentaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "La cuenta no tiene saldo suficiente.");
    }

    @Test
    void validateSaldoInsuficiente() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setSaldo(50.0);
        cuentaOrigen.setEstaA(true);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setEstaA(true);

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(cuentaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "La cuenta no tiene saldo suficiente.");
    }

    @Test
    void validateCuentasInactivas() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setSaldo(200.0);
        cuentaOrigen.setEstaA(false);
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setEstaA(true);

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(cuentaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "Ambas cuentas deben estar activas.");
    }

    @Test
    void validateMonedaDiferente() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setSaldo(200.0);
        cuentaOrigen.setEstaA(true);
        cuentaOrigen.setMoneda("USD");
        
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setEstaA(true);
        cuentaDestino.setMoneda("ARS");

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(cuentaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "Las cuentas deben tener la misma moneda.");
    }
}
