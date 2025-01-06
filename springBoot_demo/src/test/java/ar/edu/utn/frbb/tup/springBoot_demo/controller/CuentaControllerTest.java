package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CuentaControllerTest {

    @Mock
    private CuentaValidator cuentaValidator;

    @Mock
    private ServiceCuenta cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void darCuentas_WhenEmpty_ReturnsNoContent() {
        Respuesta<List<Cuenta>> respuestaMock = new Respuesta<>(new MsjResponce("Test", "Test"), new ArrayList<>(), HttpStatus.NO_CONTENT);
        when(cuentaService.darCuentas()).thenReturn(respuestaMock);
        ResponseEntity<Respuesta<List<Cuenta>>> response = cuentaController.darCuentas();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void darCuentas_WhenNotEmpty_ReturnsOk() {
        List<Cuenta> cuentas = List.of(new Cuenta());
        Respuesta<List<Cuenta>> respuestaMock = new Respuesta<>(new MsjResponce("Test", "Test"), cuentas, HttpStatus.OK);
        when(cuentaService.darCuentas()).thenReturn(respuestaMock);
        ResponseEntity<Respuesta<List<Cuenta>>> response = cuentaController.darCuentas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentas, response.getBody().getDatos());
    }

    @Test
    void crearCuenta_WhenValid_ReturnsCreated() throws CuentaAlreadyExistsException {
        CuentaDto dto = new CuentaDto();
        Cuenta cuenta = new Cuenta();
        Respuesta<Cuenta> respuestaMock = new Respuesta<>(new MsjResponce("Test", "Test"), cuenta, HttpStatus.OK);
        when(cuentaService.darDeAltaCuenta(dto)).thenReturn(respuestaMock);
        doNothing().when(cuentaValidator).validate(dto);

        ResponseEntity<Respuesta<Cuenta>> response = cuentaController.crearCuenta(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cuenta, response.getBody().getDatos());
    }

    @Test
    void crearCuenta_WhenAlreadyExists_ReturnsConflict() {
        CuentaDto dto = new CuentaDto();
        doThrow(CuentaAlreadyExistsException.class).when(cuentaValidator).validate(dto);

        ResponseEntity<Respuesta<Cuenta>> response = cuentaController.crearCuenta(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void depositar_WhenCuentaNotFound_ReturnsNotFound() {
        Long id = 1L;
        Double monto = 100.0;
        when(cuentaService.buscarCuenta(id)).thenReturn(null);

        ResponseEntity<Respuesta<Cuenta>> response = cuentaController.depositar(monto, id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void depositar_WhenServiceThrowsException_ReturnsConflict() throws CuentaNotFoundException {
        Long id = 1L;
        Double monto = 100.0;
        Cuenta cuenta = new Cuenta();
        Respuesta<Cuenta> respuestaMock = new Respuesta<>(new MsjResponce("Test", "Test"), cuenta, HttpStatus.OK);
        when(cuentaService.buscarCuenta(id)).thenReturn(respuestaMock);

        doThrow(new CuentaNotFoundException("Test exception")).when(cuentaService).depositar(respuestaMock, monto);

        ResponseEntity<Respuesta<Cuenta>> response = cuentaController.depositar(monto, id);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void retirar_WhenCuentaNotFound_ReturnsNotFound() {
        Long id = 1L;
        Double monto = 100.0;
        when(cuentaService.buscarCuenta(id)).thenReturn(null);

        ResponseEntity<Respuesta<Cuenta>> response = cuentaController.retirar(monto, id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void borrarCuenta_WhenCuentaNotFound_ReturnsNotFound() {
        Long numeroCuenta = 1L;
        when(cuentaService.buscarCuenta(numeroCuenta)).thenReturn(null);

        ResponseEntity<Respuesta<Cuenta>> response = cuentaController.borrarCuenta(numeroCuenta);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}