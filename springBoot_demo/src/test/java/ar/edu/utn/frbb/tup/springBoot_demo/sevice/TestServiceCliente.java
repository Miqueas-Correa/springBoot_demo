package ar.utn.frbb.tup.sevice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.utn.frbb.tup.model.Cliente;
import ar.utn.frbb.tup.parsistence.DaoCliente;
import ar.utn.frbb.tup.service.ServiceCliente;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestServiceCliente {
    @Mock
    private DaoCliente daoCliente;

    @InjectMocks
    private ServiceCliente serviceCliente;

    // setUp
    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNombreDelBanco() {
        assertTrue(serviceCliente.nombre_del_banco("nacion"));
        assertTrue(serviceCliente.nombre_del_banco("provincia"));
        assertFalse(serviceCliente.nombre_del_banco("otro"));
    }

    @Test
    public void testValidarEmail() {
        assertTrue(serviceCliente.validarEmail("usuario@example.com"));
        assertFalse(serviceCliente.validarEmail("usuario@"));
    }

    @Test
    public void testValidarFechaNacimiento() {
        assertTrue(serviceCliente.validarFechaNacimiento("2000-01-01"));
        assertFalse(serviceCliente.validarFechaNacimiento("2030-01-01"));
    }

    @Test
    public void testBuscarClientePorDni() {
        Long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        when(daoCliente.BuscarClientePorDni(dni)).thenReturn(cliente);

        assertEquals(cliente, serviceCliente.buscarClientePorDni(dni));
        verify(daoCliente).BuscarClientePorDni(dni);
    }
}
