package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCliente;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ClienteControllerTest {

    @Mock
    private ServiceCliente serviceCliente;

    @Mock
    private ClienteValidator clienteValidator;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void darCliente_WhenFound_ReturnsOkWithCorrectMessage() {
        Cliente cliente = new Cliente();
        Respuesta<Cliente> respuesta = new Respuesta<>(new MsjResponce("EXITOSA", "Cliente encontrado"), cliente, HttpStatus.OK);
        when(serviceCliente.buscarClientePorDni(1L)).thenReturn(respuesta);

        ResponseEntity<Respuesta<Cliente>> response = clienteController.darCliente(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EXITOSA", response.getBody().getMensaje().getEstado());
        assertEquals(cliente, response.getBody().getDatos());
    }

    @Test
    void crearCliente_WhenValid_ReturnsCreated() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        Cliente cliente = new Cliente();
        Respuesta<Cliente> respuesta = new Respuesta<>(new MsjResponce("EXITOSA", "Cliente creado"), cliente, HttpStatus.CREATED);
        doNothing().when(clienteValidator).validate(any(ClienteDto.class));
        when(serviceCliente.altaCliente(any(ClienteDto.class))).thenReturn(respuesta);
        ResponseEntity<Respuesta<Cliente>> response = clienteController.crearCliente(clienteDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(respuesta, response.getBody());
    }

    @Test
    void crearCliente_WhenAlreadyExists_ReturnsConflict() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        doThrow(ClienteAlreadyExistsException.class).when(clienteValidator).validate(any(ClienteDto.class));
        ResponseEntity<Respuesta<Cliente>> response = clienteController.crearCliente(clienteDto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void modificarCliente_WhenNotFound_ReturnsNotFound() {
        when(serviceCliente.buscarClientePorDni(anyLong())).thenReturn(null);
        ResponseEntity<Respuesta<Cliente>> response = clienteController.modificarCliente(new ClienteDto(), 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void modificarCliente_WhenValidUpdate_ReturnsOk() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        Cliente cliente = new Cliente();
        Respuesta<Cliente> respuesta = new Respuesta<>(new MsjResponce("EXITOSA", "Cliente modificado"), cliente, HttpStatus.OK);
        when(serviceCliente.buscarClientePorDni(1L)).thenReturn(respuesta);            when(serviceCliente.update(any(ClienteDto.class), anyLong())).thenReturn(respuesta);
        ResponseEntity<Respuesta<Cliente>> response = clienteController.modificarCliente(clienteDto, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(respuesta, response.getBody());
    }


    @Test
    void darDeBajaCliente_WhenNotFound_ReturnsNotFound() {
        when(serviceCliente.buscarClientePorDni(anyLong())).thenReturn(null);
        ResponseEntity<Respuesta<Cliente>> response = clienteController.darDeBajaCliente(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void darDeBajaCliente_WhenSuccessful_ReturnsOkWithCorrectMessage() {
        when(serviceCliente.buscarClientePorDni(1L)).thenReturn(new Respuesta<>(null, new Cliente(), HttpStatus.OK));
        doNothing().when(serviceCliente).bajaCliente(1L);
        ResponseEntity<Respuesta<Cliente>> response = clienteController.darDeBajaCliente(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("EXITOSA", response.getBody().getMensaje().getEstado());
        assertTrue(response.getBody().getMensaje().getMensaje().contains("Se elimino el cliente"));
    }
}
