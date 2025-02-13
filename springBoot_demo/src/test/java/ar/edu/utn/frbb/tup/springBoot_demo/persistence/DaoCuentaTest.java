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
import java.time.LocalDate;

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
            "DaoCuenta debería heredar de AbstractBaseDao");
    }

    @Test
    void testDaoCuentaInstantiation() {
        assertNotNull(daoCuenta, "DaoCuenta debería ser instanciado correctamente");
    }

    @Test
    void testUpdateThrowsCuentaNotFoundException() {
        CuentaDto cuentaDto = new CuentaDto();
        assertThrows(CuentaNotFoundException.class, () -> {
            daoCuenta.update(cuentaDto, 999999L);
        }, "Se esperaba una CuentaNotFoundException al intentar actualizar una cuenta inexistente");
    }

    @Test
    void testParseCuentaThrowsIOException() {
        String invalidData = "invalid;data;format";
        assertThrows(DataAccessException.class, () -> {
            daoCuenta.parseCuenta(invalidData);
        }, "Se esperaba una DataAccessException al intentar parsear datos inválidos");
    }

    @Test
    void testParseCuentaThrowsDataAccessException() {
        String invalidNumberFormat = "abc;123;true;100.0;AHORRO;ARS;2023-01-01";
        assertThrows(DataAccessException.class, () -> {
            daoCuenta.parseCuenta(invalidNumberFormat);
        }, "Se esperaba una DataAccessException cuando el formato del número sea inválido");
    }

    @Test
    void testGetEntityNameReturnsCorrectValue() {
        assertEquals("Cuenta", daoCuenta.getEntityName(), "El nombre de la entidad debería ser 'Cuenta'");
    }

    @Test
    void testSaveThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            daoCuenta.save(null);
        }, "Se esperaba una IllegalArgumentException al intentar guardar una cuenta nula");
    }

    @Test
    void testSaveGeneratesUniqueId() {
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setTitular(123L);
        cuenta1.setMoneda("pesos");
        cuenta1.setFechaCreacion(LocalDate.of(2023, 1, 1));
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setTitular(456L);
        cuenta2.setMoneda("pesos");
        cuenta2.setFechaCreacion(LocalDate.of(2023, 1, 1));

        Cuenta cuentaGuardada1 = daoCuenta.save(cuenta1);
        Cuenta cuentaGuardada2 = daoCuenta.save(cuenta2);

        assertNotEquals(cuentaGuardada1.getNumeroCuenta(), cuentaGuardada2.getNumeroCuenta(),
            "Se esperaba que se generaran números de cuenta diferentes para cuentas distintas");
    }

    @Test
    void testParseCuentaWithValidData() throws IOException {
        String validData = "1;123456;true;1000.50;CORRIENTE;USD;2023-12-31;BANCO1";
        Cuenta cuenta = daoCuenta.parseCuenta(validData);

        assertEquals(1L, cuenta.getNumeroCuenta(), "El número de cuenta debe ser parseado correctamente");
        assertEquals(123456L, cuenta.getTitular(), "El titular de la cuenta debe ser parseado correctamente");
        assertTrue(cuenta.getEstaA(), "El estado de la cuenta debe ser verdadero");
        assertEquals(1000.50, cuenta.getSaldo(), "El saldo debe ser parseado correctamente");
        assertEquals("CORRIENTE", cuenta.getTipoCuenta(), "El tipo de cuenta debe ser parseado correctamente");
        assertEquals("USD", cuenta.getMoneda(), "La moneda debe ser parseada correctamente");
        assertEquals(LocalDate.parse("2023-12-31"), cuenta.getFechaCreacion(), "La fecha de creación debe ser parseada correctamente");
        assertEquals("BANCO1", cuenta.getBanco(), "El nombre del banco debe ser parseado correctamente");
    }

    @Test
    void testUpdateWithInvalidNumeroCuentaThrowsCuentaNotFoundException() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setEstaA(true);
        cuentaDto.setSaldo(1000.0);
        cuentaDto.setTipoCuenta("AHORRO");
        cuentaDto.setMoneda("ARS");

        assertThrows(CuentaNotFoundException.class, () -> {
            daoCuenta.update(cuentaDto, -1L);
        }, "Se esperaba una CuentaNotFoundException al intentar actualizar con un número de cuenta inválido");
    }
}