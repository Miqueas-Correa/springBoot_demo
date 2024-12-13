package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
}
