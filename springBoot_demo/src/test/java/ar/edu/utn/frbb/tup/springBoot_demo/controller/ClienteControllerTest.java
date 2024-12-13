package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    void darClientes_WhenEmpty_ReturnsNoContent() {
        when(serviceCliente.darClientes()).thenReturn(new ArrayList<>());
        ResponseEntity<List<Cliente>> response = clienteController.darClientes();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void darClientes_WhenNotEmpty_ReturnsOkWithClientes() {
        List<Cliente> clientes = Arrays.asList(new Cliente(), new Cliente());
        when(serviceCliente.darClientes()).thenReturn(clientes);
        ResponseEntity<List<Cliente>> response = clienteController.darClientes();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientes, response.getBody());
    }

    @Test
    void darCliente_WhenNotFound_ReturnsNotFound() {
        when(serviceCliente.buscarClientePorDni(anyLong())).thenReturn(null);
        ResponseEntity<Cliente> response = clienteController.darCliente(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void darCliente_WhenFound_ReturnsOkWithCliente() {
        Cliente cliente = new Cliente();
        when(serviceCliente.buscarClientePorDni(1L)).thenReturn(cliente);
        ResponseEntity<Cliente> response = clienteController.darCliente(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void crearCliente_WhenValid_ReturnsCreated() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        Cliente cliente = new Cliente();
        doNothing().when(clienteValidator).validate(any(ClienteDto.class));
        when(serviceCliente.altaCliente(any(ClienteDto.class))).thenReturn(cliente);
        ResponseEntity<Cliente> response = clienteController.crearCliente(clienteDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void crearCliente_WhenAlreadyExists_ReturnsConflict() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        doThrow(ClienteAlreadyExistsException.class).when(clienteValidator).validate(any(ClienteDto.class));
        ResponseEntity<Cliente> response = clienteController.crearCliente(clienteDto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void modificarCliente_WhenNotFound_ReturnsNotFound() {
        when(serviceCliente.buscarClientePorDni(anyLong())).thenReturn(null);
        ResponseEntity<Cliente> response = clienteController.modificarCliente(new ClienteDto(), 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void modificarCliente_WhenValidUpdate_ReturnsOk() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        Cliente cliente = new Cliente();
        when(serviceCliente.buscarClientePorDni(1L)).thenReturn(cliente);
        when(serviceCliente.update(any(ClienteDto.class), anyLong())).thenReturn(cliente);
        ResponseEntity<Cliente> response = clienteController.modificarCliente(clienteDto, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
    }

    @Test
    void darDeBajaCliente_WhenNotFound_ReturnsNotFound() {
        when(serviceCliente.buscarClientePorDni(anyLong())).thenReturn(null);
        ResponseEntity<Cliente> response = clienteController.darDeBajaCliente(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void darDeBajaCliente_WhenSuccessful_ReturnsOk() {
        Cliente cliente = new Cliente();
        when(serviceCliente.buscarClientePorDni(1L)).thenReturn(cliente);
        doNothing().when(serviceCliente).bajaCliente(1L);
        ResponseEntity<Cliente> response = clienteController.darDeBajaCliente(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void darDeBajaCliente_WhenError_ReturnsBadRequest() {
        Cliente cliente = new Cliente();
        when(serviceCliente.buscarClientePorDni(1L)).thenReturn(cliente);
        doThrow(RuntimeException.class).when(serviceCliente).bajaCliente(1L);
        ResponseEntity<Cliente> response = clienteController.darDeBajaCliente(1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
