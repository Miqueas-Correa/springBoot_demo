package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DaoCuentaTest {

    private DaoCuenta daoCuenta;

    @BeforeEach
    void setUp() {
        daoCuenta = new DaoCuenta();
    }

    @Test
    void testDaoCuentaInheritsFromAbstractBaseDao() {
        assertTrue(daoCuenta instanceof AbstractBaseDao, 
            "DaoCuenta should inherit from AbstractBaseDao");
    }

    @Test
    void testDaoCuentaInstantiation() {
        assertNotNull(daoCuenta, "DaoCuenta should be instantiated successfully");
    }
}
