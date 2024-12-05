package ar.edu.utn.frbb.tup.springBoot_demo.service;

import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServiceClienteTest {

    @Mock
    private DaoCliente daoCliente;

    private ServiceCliente serviceCliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceCliente = new ServiceCliente(daoCliente);
    }

    @Test
    void testUpdateCliente() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setDni(12345678L);
        
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
        clienteDto.setNombre("Ana");
        clienteDto.setApellido("Garcia");
        clienteDto.setDni(87654321L);
        
        serviceCliente.save(clienteDto);
        
        verify(daoCliente).save(any(Cliente.class));
    }

    @Test
    void testAltaClienteMenorEdad() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Pedro");
        clienteDto.setApellido("Lopez");
        clienteDto.setDni(11223344L);
        clienteDto.setEdad(16);
        
        assertThrows(IllegalArgumentException.class, () -> serviceCliente.altaCliente(clienteDto));
    }

    @Test
    void testAltaClienteExistente() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Maria");
        clienteDto.setApellido("Martinez");
        clienteDto.setDni(99887766L);
        clienteDto.setEdad(25);
        
        when(daoCliente.buscarClientePorDni(99887766L)).thenReturn(new Cliente(clienteDto));
        
        assertThrows(ClienteAlreadyExistsException.class, () -> serviceCliente.altaCliente(clienteDto));
    }

    @Test
    void testDarClientes() {
        List<Cliente> expectedClientes = Arrays.asList(
            new Cliente(new ClienteDto()),
            new Cliente(new ClienteDto())
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
        Cliente expectedCliente = new Cliente(new ClienteDto());
        when(daoCliente.buscarClientePorDni(dni)).thenReturn(expectedCliente);
        
        Cliente result = serviceCliente.buscarClientePorDni(dni);
        
        assertEquals(expectedCliente, result);
        verify(daoCliente).buscarClientePorDni(dni);
    }
}
