package ar.edu.utn.frbb.tup.springBoot_demo.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

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

    @Test
    void testListarCuentasByTitularReturnsEmptyListWhenNoMatches() {
        List<Cuenta> result = daoCuenta.listarCuentas(999999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateThrowsCuentaNotFoundException() {
        CuentaDto cuentaDto = new CuentaDto();
        assertThrows(CuentaNotFoundException.class, () -> {
            daoCuenta.update(cuentaDto, 999999L);
        });
    }

    @Test
    void testParseCuentaThrowsIOException() {
        String invalidData = "invalid;data;format";
        assertThrows(IOException.class, () -> {
            daoCuenta.parseCuenta(invalidData);
        });
    }

    @Test
    void testParseCuentaThrowsDataAccessException() {
        String invalidNumberFormat = "abc;123;true;100.0;AHORRO;ARS;2023-01-01";
        assertThrows(DataAccessException.class, () -> {
            daoCuenta.parseCuenta(invalidNumberFormat);
        });
    }

    @Test
    void testBuscarCuentaPorTitularReturnsNull() {
        Cuenta result = daoCuenta.buscarCuentaPorTitular(999999L);
        assertNull(result);
    }

    @Test
    void testGetEntityNameReturnsCorrectValue() {
        assertEquals("Cuenta", daoCuenta.getEntityName());
    }
}
