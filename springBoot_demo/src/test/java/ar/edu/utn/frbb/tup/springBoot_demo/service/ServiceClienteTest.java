package ar.edu.utn.frbb.tup.springBoot_demo.service;

import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre_y_apellido("Juan Perez");
        clienteDto.setDni(12345678L);
        clienteDto.setTelefono(1234567890L);

        Cliente clienteExpected = new Cliente(clienteDto);
        when(daoCliente.updateCliente(clienteDto, 12345678L)).thenReturn(clienteExpected);

        Cliente result = serviceCliente.update(clienteDto, 12345678L);

        assertEquals(clienteExpected, result);
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
        clienteDto1.setBanco("nacion");
        clienteDto1.setEmail("juan.perez@example.com");

        ClienteDto clienteDto2 = new ClienteDto();
        clienteDto2.setNombre_y_apellido("Ana Garcia");
        clienteDto2.setDni(87654321L);
        clienteDto2.setTelefono(9876543210L);
        clienteDto2.setFechaNacimiento(LocalDate.of(1985, 5, 20));
        clienteDto2.setBanco("provincia");
        clienteDto2.setEmail("ana.garcia@example.com");

        List<Cliente> expectedClientes = Arrays.asList(
            new Cliente(clienteDto1),
            new Cliente(clienteDto2)
        );

        when(daoCliente.listarClientes()).thenReturn(expectedClientes);

        List<Cliente> result = serviceCliente.darClientes();

        assertEquals(expectedClientes, result);
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
        clienteDto.setBanco("nacion");
        clienteDto.setEmail("juan.perez@example.com");
        Cliente expectedCliente = new Cliente(clienteDto);

        when(daoCliente.buscarClientePorDni(dni)).thenReturn(expectedCliente);

        Cliente result = serviceCliente.buscarClientePorDni(dni);

        // Verifica que los resultados sean los esperados
        assertEquals(expectedCliente, result);
        verify(daoCliente).buscarClientePorDni(dni);
    }
}
