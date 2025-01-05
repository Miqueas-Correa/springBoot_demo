package ar.edu.utn.frbb.tup.springBoot_demo.service;

import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServiceClienteTest {

    @Mock
    private DaoCliente daoCliente;
    @InjectMocks
    private ServiceCliente serviceCliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateCliente() {
        // Datos de prueba
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre_y_apellido("Juan Perez");
        clienteDto.setDni(12345678L);
        clienteDto.setTelefono(1234567890L);
        Cliente clienteExpected = new Cliente(clienteDto);
        Respuesta<Cliente> respuestaExpected = new Respuesta<>(
            new MsjResponce("EXITOSA", "Se actualizo el cliente con DNI 12345678"), 
            clienteExpected, 
            HttpStatus.OK
        );
        when(daoCliente.updateCliente(clienteDto, 12345678L)).thenReturn(clienteExpected);
        Respuesta<Cliente> respuestaResult = serviceCliente.update(clienteDto, 12345678L);
        assertEquals(respuestaExpected.getMensaje().getEstado(), respuestaResult.getMensaje().getEstado());
        assertEquals(respuestaExpected.getMensaje().getMensaje(), respuestaResult.getMensaje().getMensaje());
        assertEquals(respuestaExpected.getDatos(), respuestaResult.getDatos());
        assertEquals(respuestaExpected.getHttpStatus(), respuestaResult.getHttpStatus());
        verify(daoCliente).updateCliente(clienteDto, 12345678L);
    }

    @Test
    void testSaveClienteNull() {
        assertThrows(IllegalArgumentException.class, () -> serviceCliente.save(null));
    }

    @Test
    void testSaveClienteSuccess() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre_y_apellido("Ana Garcia");
        clienteDto.setDni(87654321L);
        clienteDto.setTelefono(1234567890L);

        serviceCliente.save(clienteDto);

        // Verificar que se llamo al método save en daoCliente con cualquier objeto Cliente
        verify(daoCliente).save(any(Cliente.class));
    }

    @Test
    void testAltaClienteMenorEdad() {
        // Configurar todos los campos obligatorios
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre_y_apellido("Pedro Martinez");
        clienteDto.setDni(11223344L);
        clienteDto.setFechaNacimiento(java.time.LocalDate.of(2010, 1, 1));
        clienteDto.setTelefono(1234567890L);

        // Verificar que se lanza la excepción esperada para un cliente menor de edad
        assertThrows(IllegalArgumentException.class, () -> serviceCliente.altaCliente(clienteDto));
    }

    @Test
    void testAltaClienteExistente() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre_y_apellido("Maria Rodriguez");
        clienteDto.setDni(99887766L);
        clienteDto.setFechaNacimiento(java.time.LocalDate.of(1990, 1, 1));
        clienteDto.setTelefono(1234567890L);

        // Simular que el cliente ya existe en el sistema
        when(daoCliente.buscarClientePorDni(99887766L)).thenReturn(new Cliente(clienteDto));

        // Verificar que se lanza la excepción para un cliente ya existente
        assertThrows(ClienteAlreadyExistsException.class, () -> serviceCliente.altaCliente(clienteDto));
    }

    @Test
    void testDarClientes() {
        ClienteDto clienteDto1 = new ClienteDto();
        clienteDto1.setNombre_y_apellido("Juan Perez");
        clienteDto1.setDni(12345678L);
        clienteDto1.setTelefono(1234567890L);
        clienteDto1.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        clienteDto1.setEmail("juan.perez@example.com");
        ClienteDto clienteDto2 = new ClienteDto();
        clienteDto2.setNombre_y_apellido("Ana Garcia");
        clienteDto2.setDni(87654321L);
        clienteDto2.setTelefono(9876543210L);
        clienteDto2.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        clienteDto2.setEmail("ana.garcia@example.com");
        List<Cliente> expectedClientes = Arrays.asList(
            new Cliente(clienteDto1),
            new Cliente(clienteDto2)
        );
        Respuesta<List<Cliente>> expectedResponse = new Respuesta<>(
            new MsjResponce("EXITOSA", "Aqui la lista de clientes."),
            expectedClientes,
            HttpStatus.OK
        );
        when(daoCliente.listarClientes()).thenReturn(expectedClientes);
        Respuesta<List<Cliente>> result = serviceCliente.darClientes();
        assertEquals(expectedResponse.getMensaje().getEstado(), result.getMensaje().getEstado());
        assertEquals(expectedResponse.getMensaje().getMensaje(), result.getMensaje().getMensaje());
        assertEquals(expectedResponse.getDatos(), result.getDatos());
        assertEquals(expectedResponse.getHttpStatus(), result.getHttpStatus());
        verify(daoCliente).listarClientes();
    }

    @Test
    void testBajaCliente() {
        Long dni = 12345678L;
        serviceCliente.bajaCliente(dni);
        verify(daoCliente).bajaCliente(dni);
    }

    @Test
    void testBuscarClientePorDni() {
        Long dni = 12345678L;
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(dni);
        clienteDto.setTelefono(1234567890L);
        clienteDto.setNombre_y_apellido("Juan Perez");
        clienteDto.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        clienteDto.setEmail("juan.perez@example.com");

        Cliente expectedCliente = new Cliente(clienteDto);
        Respuesta<Cliente> expectedResponse = new Respuesta<>(
            new MsjResponce("EXITOSA", "Aqui el cliente con el dni: " + dni),
            expectedCliente,
            HttpStatus.OK
        );

        when(daoCliente.buscarClientePorDni(dni)).thenReturn(expectedCliente);

        Respuesta<Cliente> result = serviceCliente.buscarClientePorDni(dni);

        assertEquals(expectedResponse.getMensaje().getEstado(), result.getMensaje().getEstado());
        assertEquals(expectedResponse.getMensaje().getMensaje(), result.getMensaje().getMensaje());
        assertEquals(expectedResponse.getDatos(), result.getDatos());
        assertEquals(expectedResponse.getHttpStatus(), result.getHttpStatus());

        verify(daoCliente).buscarClientePorDni(dni);
    }

    @Test
    void testUpdateClienteWithInvalidDni() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre_y_apellido("Test User");
        clienteDto.setDni(0L);
        clienteDto.setTelefono(1234567890L);

        when(daoCliente.updateCliente(clienteDto, 0L)).thenThrow(new IllegalArgumentException("DNI inválido"));

        assertThrows(IllegalArgumentException.class, () -> serviceCliente.update(clienteDto, 0L));
        verify(daoCliente).updateCliente(clienteDto, 0L);
    }

    @Test
    void testAltaClienteEdadLimite() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre_y_apellido("Test User");
        clienteDto.setDni(12345678L);
        clienteDto.setTelefono(1234567890L);
        // Cliente con 17 años y 364 días
        clienteDto.setFechaNacimiento(LocalDate.now().minusYears(18).plusDays(1));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> serviceCliente.altaCliente(clienteDto)
        );

        assertEquals("El cliente debe ser mayor a 18 años", exception.getMessage());
    }

    @Test
    void testBuscarClientePorDniInexistente() {
        Long dni = 99999999L;
        when(daoCliente.buscarClientePorDni(dni)).thenReturn(null);
        Cliente result = serviceCliente.buscarClientePorDni(dni).getDatos();
        assertNull(result);
        verify(daoCliente).buscarClientePorDni(dni);
    }

    @Test
    void testDarClientesListaVacia() {
        when(daoCliente.listarClientes()).thenReturn(Arrays.asList());
        List<Cliente> result = serviceCliente.darClientes().getDatos();
        assertTrue(result.isEmpty());
        verify(daoCliente).listarClientes();
    }

    @Test
    void testBajaClienteInexistente() {
        Long dni = 99999999L;
        doThrow(new IllegalArgumentException("Cliente no encontrado")).when(daoCliente).bajaCliente(dni);
        assertThrows(IllegalArgumentException.class, () -> serviceCliente.bajaCliente(dni));
        verify(daoCliente).bajaCliente(dni);
    }

    @Test
    void testAltaClienteConDatosCompletos() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre_y_apellido("Test Complete");
        clienteDto.setDni(55555555L);
        clienteDto.setTelefono(1234567890L);
        clienteDto.setFechaNacimiento(LocalDate.of(1980, 1, 1));
        clienteDto.setEmail("test@example.com");
        when(daoCliente.buscarClientePorDni(55555555L)).thenReturn(null);
        assertDoesNotThrow(() -> serviceCliente.altaCliente(clienteDto));
        verify(daoCliente).save(any(Cliente.class));
    }
}