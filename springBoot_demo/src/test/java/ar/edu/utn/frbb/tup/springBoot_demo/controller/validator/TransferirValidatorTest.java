package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
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
        cuentaOrigen.setSaldo(200.0);
        cuentaOrigen.setEstaA(true);
        cuentaOrigen.setMoneda("pesos");

        Respuesta<Cuenta> respuestaCuentaOrigen = new Respuesta<>(
                new MsjResponce("EXITOSA", "Cuenta encontrada."),
                cuentaOrigen,
                HttpStatus.OK
        );

        Respuesta<Cuenta> respuestaCuentaDestino = new Respuesta<>(
                new MsjResponce("FALLIDA", "Cuenta no encontrada."),
                null,
                HttpStatus.NOT_FOUND
        );

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(respuestaCuentaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(respuestaCuentaDestino);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "La cuenta de destino no existe.");
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
        cuentaOrigen.setMoneda("pesos");
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setSaldo(100.0);
        cuentaDestino.setEstaA(true);
        cuentaDestino.setMoneda("pesos");

        Respuesta<Cuenta> respuestaOrigen = new Respuesta<>(
                new MsjResponce("EXITOSA", "Cuenta encontrada."),
                cuentaOrigen,
                HttpStatus.OK
        );
        Respuesta<Cuenta> respuestaDestino = new Respuesta<>(
                new MsjResponce("EXITOSA", "Cuenta encontrada."),
                cuentaDestino,
                HttpStatus.OK
        );

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(respuestaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(respuestaDestino);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "Ambas cuentas deben estar activas.");
    }

    @Test
    void validateMonedaDiferente() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setMoneda("pesos");
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setSaldo(200.0);
        cuentaOrigen.setEstaA(true);
        cuentaOrigen.setMoneda("pesos");
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setSaldo(100.0);
        cuentaDestino.setEstaA(true);
        cuentaDestino.setMoneda("dolares");

        Respuesta<Cuenta> respuestaOrigen = new Respuesta<>(
                new MsjResponce("EXITOSA", "Cuenta encontrada."),
                cuentaOrigen,
                HttpStatus.OK
        );

        Respuesta<Cuenta> respuestaDestino = new Respuesta<>(
                new MsjResponce("EXITOSA", "Cuenta encontrada."),
                cuentaDestino,
                HttpStatus.OK
        );

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(respuestaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(respuestaDestino);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "Las cuentas deben tener la misma moneda, junto con la moneda de la operacion.");
    }

    @Test
    void validateMonedaInvalida() {
        TransferirDto dto = new TransferirDto();
        dto.setMonto(100.0);
        dto.setCuentaOrigen(1L);
        dto.setCuentaDestino(2L);
        dto.setMoneda("euros");
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setSaldo(200.0);
        cuentaOrigen.setEstaA(true);
        cuentaOrigen.setMoneda("pesos");
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setSaldo(100.0);
        cuentaDestino.setEstaA(true);
        cuentaDestino.setMoneda("pesos");

        Respuesta<Cuenta> respuestaOrigen = new Respuesta<>(
                new MsjResponce("EXITOSA", "Cuenta encontrada."),
                cuentaOrigen,
                HttpStatus.OK
        );

        Respuesta<Cuenta> respuestaDestino = new Respuesta<>(
                new MsjResponce("EXITOSA", "Cuenta encontrada."),
                cuentaDestino,
                HttpStatus.OK
        );

        when(serviceCuenta.buscarCuenta(1L)).thenReturn(respuestaOrigen);
        when(serviceCuenta.buscarCuenta(2L)).thenReturn(respuestaDestino);

        assertThrows(IllegalArgumentException.class, () -> transferirValidator.validate(dto),
                "La moneda de la operación no es válida.");
    }
}