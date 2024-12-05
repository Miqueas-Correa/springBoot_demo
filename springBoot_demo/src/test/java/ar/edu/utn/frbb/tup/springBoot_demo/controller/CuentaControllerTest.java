package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.TransferirValidator;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CuentaControllerTest {

    @Mock
    private CuentaValidator cuentaValidator;

    @Mock
    private TransferirValidator transferirValidator;

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
        when(cuentaService.darCuentas()).thenReturn(new ArrayList<>());
        ResponseEntity<List<Cuenta>> response = cuentaController.darCuentas();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void darCuentas_WhenNotEmpty_ReturnsOk() {
        List<Cuenta> cuentas = List.of(new Cuenta());
        when(cuentaService.darCuentas()).thenReturn(cuentas);
        ResponseEntity<List<Cuenta>> response = cuentaController.darCuentas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentas, response.getBody());
    }

    @Test
    void crearCuenta_WhenValid_ReturnsCreated() throws CuentaAlreadyExistsException {
        CuentaDto dto = new CuentaDto();
        Cuenta cuenta = new Cuenta();
        when(cuentaService.darDeAltaCuenta(dto)).thenReturn(cuenta);
        doNothing().when(cuentaValidator).validate(dto);

        ResponseEntity<Cuenta> response = cuentaController.crearCuenta(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cuenta, response.getBody());
    }

    @Test
    void crearCuenta_WhenAlreadyExists_ReturnsConflict() throws CuentaAlreadyExistsException {
        CuentaDto dto = new CuentaDto();
        doThrow(CuentaAlreadyExistsException.class).when(cuentaValidator).validate(dto);

        ResponseEntity<Cuenta> response = cuentaController.crearCuenta(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void depositar_WhenSuccess_ReturnsOkMessage() throws CuentaNotFoundException {
        Long id = 1L;
        Double monto = 100.0;
        Cuenta cuenta = new Cuenta();
        MsjResponce expectedResponse = new MsjResponce("EXITOSA", "Depósito realizado");

        when(cuentaService.buscarCuenta(id)).thenReturn(cuenta);
        when(cuentaService.depositar(cuenta, monto)).thenReturn(expectedResponse);

        MsjResponce response = cuentaController.depositar(monto, id);

        assertEquals(expectedResponse, response);
    }

    @Test
    void transferir_WhenSuccess_ReturnsOkMessage() throws CuentaNotFoundException {
        TransferirDto dto = new TransferirDto();
        MsjResponce expectedResponse = new MsjResponce("EXITOSA", "Transferencia realizada");

        doNothing().when(transferirValidator).validate(dto);
        when(cuentaService.transferir(dto)).thenReturn(expectedResponse);

        MsjResponce response = cuentaController.transferir(dto);

        assertEquals(expectedResponse, response);
    }

    @Test
    void transferir_WhenError_ReturnsErrorMessage() throws CuentaNotFoundException {
        TransferirDto dto = new TransferirDto();
        doThrow(CuentaNotFoundException.class).when(transferirValidator).validate(dto);

        MsjResponce response = cuentaController.transferir(dto);

        assertEquals("FALLIDA", response.getEstado());
        assertEquals("Error en la transferencia. Verifique los datos.", response.getMensaje());
    }
}
