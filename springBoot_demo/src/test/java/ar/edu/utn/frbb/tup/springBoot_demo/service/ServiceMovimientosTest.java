package ar.edu.utn.frbb.tup.springBoot_demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoMovimientos;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ServiceMovimientosTest {

    @Mock
    private DaoMovimientos daoMovimientos;

    @InjectMocks
    private ServiceMovimientos serviceMovimientos;

    @Test
    void save_ValidMovimiento_ShouldSaveSuccessfully() {
        Movimientos movimiento = new Movimientos();
        movimiento.setNumeroCuenta(1234L);

        serviceMovimientos.save(movimiento);

        verify(daoMovimientos, times(1)).save(movimiento);
    }

    @Test
    void save_NullMovimiento_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            serviceMovimientos.save(null);
        });
    }

    @Test
    void buscar_ExistingCuenta_ShouldReturnMovimiento() {
        Long numeroCuenta = 5678L;
        Movimientos expectedMovimiento = new Movimientos();
        expectedMovimiento.setNumeroCuenta(numeroCuenta);

        when(daoMovimientos.buscar(numeroCuenta)).thenReturn(expectedMovimiento);

        Movimientos result = serviceMovimientos.buscar(numeroCuenta);

        assertNotNull(result);
        assertEquals(numeroCuenta, result.getNumeroCuenta()); // Verificar que el número de cuenta coincide
        verify(daoMovimientos, times(1)).buscar(numeroCuenta); // Verificar que el dao fue llamado correctamente
    }

    @Test
    void buscar_NonExistingCuenta_ShouldReturnNull() {
        Long numeroCuenta = 9999L;

        when(daoMovimientos.buscar(numeroCuenta)).thenReturn(null);

        Movimientos result = serviceMovimientos.buscar(numeroCuenta);

        assertNull(result);
        verify(daoMovimientos, times(1)).buscar(numeroCuenta); // Verificar que el dao fue llamado correctamente
    }
}