package ar.edu.utn.frbb.tup.springBoot_demo.persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteNotFoundException;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;
import java.util.List;

public class DaoClienteTest {

    private DaoCliente daoCliente;

    @BeforeEach
    void setUp() {
        daoCliente = new DaoCliente();
    }

    @Test
    void testDaoClienteInheritsFromAbstractBaseDao() {
        assertTrue(daoCliente instanceof AbstractBaseDao);
    }

    @Test
    void testDaoClienteInstantiationSucceeds() {
        assertNotNull(daoCliente);
    }

    @Test
    void testListarClientesEmptyFile() {
        DaoCliente mockDao = Mockito.spy(new DaoCliente());
        Mockito.doReturn(Collections.emptyList()).when(mockDao).listarClientes();
        List<Cliente> clientes = mockDao.listarClientes();
        assertTrue(clientes.isEmpty());
    }


    @Test
    void testBuscarClientePorDniNoExistente() {
        Cliente cliente = daoCliente.buscarClientePorDni(99999999L);
        assertNull(cliente);
    }

    @Test
    void testUpdateClienteInexistente() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(99999999L);

        assertThrows(ClienteNotFoundException.class, 
            () -> daoCliente.updateCliente(clienteDto, 99999999L));
    }

    @Test
    void testGetEntityName() {
        assertEquals("Cliente", daoCliente.getEntityName());
    }
}