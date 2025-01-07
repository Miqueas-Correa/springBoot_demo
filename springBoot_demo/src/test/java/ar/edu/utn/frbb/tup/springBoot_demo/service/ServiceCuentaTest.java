package ar.edu.utn.frbb.tup.springBoot_demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import ar.edu.utn.frbb.tup.springBoot_demo.model.*;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.*;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ServiceCuentaTest {

    @Mock
    private DaoCuenta daoCuenta;

    @Mock
    private DaoMovimientos daoMovimientos;

    @InjectMocks
    private ServiceCuenta serviceCuenta;

    private Cuenta cuenta;
    private CuentaDto cuentaDto;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setSaldo(1000.0);
        cuenta.setEstaA(true);
        cuenta.setTipoCuenta("c");
        cuenta.setMoneda("dolares");
        cuenta.setTitular(12345678L);
        cuenta.setBanco("nacion");

        cuentaDto = new CuentaDto();
        cuentaDto.setSaldo(1000.0);
        cuentaDto.setEstaA(true);
        cuentaDto.setTipoCuenta("c");
        cuentaDto.setMoneda("dolares");
        cuentaDto.setTitular(12345678L);
        cuentaDto.setBanco("provincia");
    }

    @Test
    void testDarCuentasExitoso() {
        List<Cuenta> cuentas = Arrays.asList(cuenta);
        when(daoCuenta.listarCuentas()).thenReturn(cuentas);

        var response = serviceCuenta.darCuentas();

        assertEquals("EXITOSA", response.getMensaje().getEstado());
        assertEquals("Aqui la lista de cuentas:", response.getMensaje().getMensaje());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(cuentas, response.getDatos());
        verify(daoCuenta).listarCuentas();
    }

    @Test
    void testBuscarCuentaExitoso() {
        when(daoCuenta.buscarCuenta(1L)).thenReturn(cuenta);

        var response = serviceCuenta.buscarCuenta(1L);

        assertEquals("EXITOSA", response.getMensaje().getEstado());
        assertEquals("Aqui la cuenta:", response.getMensaje().getMensaje());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(cuenta, response.getDatos());
        verify(daoCuenta).buscarCuenta(1L);
    }

    @Test
    void testDepositarExitoso() {
        Respuesta<Cuenta> respuestaCuenta = new Respuesta<>(new MsjResponce("EXITOSA", ""), cuenta, HttpStatus.OK);
        when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);

        var response = serviceCuenta.depositar(respuestaCuenta, 500.0);

        assertEquals("EXITOSA", response.getMensaje().getEstado());
        assertEquals("Se deposito el monto.", response.getMensaje().getMensaje());
        assertEquals(1500.0, response.getDatos().getSaldo());
        verify(daoCuenta).update(any(CuentaDto.class), eq(1L));
    }

    @Test
    void testDepositarMontoNegativo() {
        Respuesta<Cuenta> respuestaCuenta = new Respuesta<>(new MsjResponce("EXITOSA", ""), cuenta, HttpStatus.OK);

        var response = serviceCuenta.depositar(respuestaCuenta, -100.0);

        assertEquals("FALLIDA.", response.getMensaje().getEstado());
        assertEquals("El monto debe ser mayor a 0.", response.getMensaje().getMensaje());
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertNull(response.getDatos());
    }

    @Test
    void testRetirarExitoso() {
        Respuesta<Cuenta> respuestaCuenta = new Respuesta<>(new MsjResponce("EXITOSA", ""), cuenta, HttpStatus.OK);
        when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);

        var response = serviceCuenta.retirar(respuestaCuenta, 500.0);

        assertEquals("EXITOSA", response.getMensaje().getEstado());
        assertEquals("Se retiro el monto.", response.getMensaje().getMensaje());
        assertEquals(500.0, response.getDatos().getSaldo());
        verify(daoCuenta).update(any(CuentaDto.class), eq(1L));
    }

    @Test
    void testRetirarSaldoInsuficiente() {
        cuenta.setSaldo(100.0);
        Respuesta<Cuenta> respuestaCuenta = new Respuesta<>(new MsjResponce("EXITOSA", ""), cuenta, HttpStatus.OK);

        var response = serviceCuenta.retirar(respuestaCuenta, 200.0);

        assertEquals("FALLIDA.", response.getMensaje().getEstado());
        assertEquals("Saldo insuficiente.", response.getMensaje().getMensaje());
        assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
        assertNull(response.getDatos());
    }

    @Test
    void testDarDeAltaCuentaExitoso() throws CuentaAlreadyExistsException {
        when(daoCuenta.save(any(Cuenta.class))).thenReturn(cuenta);

        var response = serviceCuenta.darDeAltaCuenta(cuentaDto);

        assertEquals("EXITOSA", response.getMensaje().getEstado());
        assertEquals("Se guardo la cuenta.", response.getMensaje().getMensaje());
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        verify(daoCuenta).save(any(Cuenta.class));
    }

    @Test
    void testDarDeBajaCuentaExitoso() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setEstaA(true);
        when(daoCuenta.buscarCuenta(1L)).thenReturn(cuenta);

        Cuenta cuentaActualizada = new Cuenta();
        cuentaActualizada.setNumeroCuenta(1L);
        cuentaActualizada.setEstaA(false); // Cuenta actualizada a inactiva
        when(daoCuenta.update(any(CuentaDto.class), eq(1L))).thenReturn(cuentaActualizada);

        var response = serviceCuenta.darDeBajaCuenta(1L);

        assertEquals("EXITOSA", response.getMensaje().getEstado());
        assertEquals("Se dio de baja la cuenta.", response.getMensaje().getMensaje());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNotNull(response.getDatos()); // Asegúrate de que no sea null
        assertFalse(response.getDatos().getEstaA());
        verify(daoCuenta).update(any(CuentaDto.class), eq(1L));
    }
}