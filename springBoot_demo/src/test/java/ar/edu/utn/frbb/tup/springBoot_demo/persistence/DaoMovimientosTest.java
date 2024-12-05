package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DaoMovimientosTest {

    private DaoMovimientos daoMovimientos;

    @BeforeEach
    void setUp() {
        daoMovimientos = new DaoMovimientos();
    }

    @Test
    void testDaoMovimientosCreation() {
        assertNotNull(daoMovimientos);
    }

    @Test
    void testDaoMovimientosInheritsAbstractBaseDao() {
        assertTrue(daoMovimientos instanceof AbstractBaseDao);
    }
}
