package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.MovimientoAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceMovimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.MovimientosValidator;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MovimientosControllerTest {

    @Mock
    private ServiceMovimientos serviceMovimientos;

    @Mock
    private MovimientosValidator movimientosValidator;

    @InjectMocks
    private MovimientosController movimientosController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void darMovimientos_WhenEmpty_ReturnsNoContent() {
        when(serviceMovimientos.darMovimientos()).thenReturn(new ArrayList<>());
        
        ResponseEntity<List<Movimientos>> response = movimientosController.darMovimientos();
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void darMovimientos_WhenNotEmpty_ReturnsOkWithMovimientos() {
        List<Movimientos> movimientos = Arrays.asList(new Movimientos(), new Movimientos());
        when(serviceMovimientos.darMovimientos()).thenReturn(movimientos);
        
        ResponseEntity<List<Movimientos>> response = movimientosController.darMovimientos();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movimientos, response.getBody());
    }

    @Test
    void darMovimientosPorId_WhenNotFound_ReturnsNotFound() {
        when(serviceMovimientos.buscar(1L)).thenReturn(null);
        
        ResponseEntity<List<Movimientos>> response = movimientosController.darMovimientos(1L);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void darMovimientosPorId_WhenFound_ReturnsOkWithMovimientos() {
        List<Movimientos> movimientos = Arrays.asList(new Movimientos());
        when(serviceMovimientos.buscar(1L)).thenReturn(movimientos);
        
        ResponseEntity<List<Movimientos>> response = movimientosController.darMovimientos(1L);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movimientos, response.getBody());
    }

    @Test
    void crearMovimientos_WhenValid_ReturnsCreated() throws MovimientoAlreadyExistsException {
        MovimientosDto dto = new MovimientosDto();
        doNothing().when(movimientosValidator).validate(any(MovimientosDto.class));
        doNothing().when(serviceMovimientos).save(any(MovimientosDto.class));
        
        ResponseEntity<Movimientos> response = movimientosController.crearMovimientos(dto);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(serviceMovimientos).save(dto);
    }

    @Test
    void crearMovimientos_WhenAlreadyExists_ReturnsConflict() throws MovimientoAlreadyExistsException {
        MovimientosDto dto = new MovimientosDto();
        doNothing().when(movimientosValidator).validate(any(MovimientosDto.class));
        doThrow(MovimientoAlreadyExistsException.class).when(serviceMovimientos).save(any(MovimientosDto.class));
        
        ResponseEntity<Movimientos> response = movimientosController.crearMovimientos(dto);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
