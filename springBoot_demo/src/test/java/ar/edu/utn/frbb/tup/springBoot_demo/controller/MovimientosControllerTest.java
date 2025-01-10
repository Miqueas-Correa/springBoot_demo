package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Transaccion;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceMovimientos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovimientosControllerTest {

    @Mock
    private ServiceMovimientos serviceMovimientos;

    @InjectMocks
    private MovimientosController movimientosController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void darMovimientosPorId_WhenTransaccionesNull_ReturnsNotFound() {
        Movimientos movimientos = new Movimientos();
        movimientos.setTransacciones(null);
        when(serviceMovimientos.buscar(1L)).thenReturn(movimientos);

        ResponseEntity<Movimientos> response = movimientosController.darMovimientos(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void darMovimientosPorId_WhenTransaccionesEmpty_ReturnsNotFound() {
        Movimientos movimientos = new Movimientos();
        movimientos.setTransacciones(new ArrayList<>());
        when(serviceMovimientos.buscar(1L)).thenReturn(movimientos);

        ResponseEntity<Movimientos> response = movimientosController.darMovimientos(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void darMovimientosPorId_WhenHasTransacciones_ReturnsOkWithMovimientos() {
        Movimientos movimientos = new Movimientos();
        List<Transaccion> transacciones = Arrays.asList(new Transaccion());
        movimientos.setTransacciones(transacciones);
        when(serviceMovimientos.buscar(1L)).thenReturn(movimientos);

        ResponseEntity<Movimientos> response = movimientosController.darMovimientos(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movimientos, response.getBody());
        assertNotNull(response.getBody().getTransacciones());
        assertFalse(response.getBody().getTransacciones().isEmpty());
    }
}