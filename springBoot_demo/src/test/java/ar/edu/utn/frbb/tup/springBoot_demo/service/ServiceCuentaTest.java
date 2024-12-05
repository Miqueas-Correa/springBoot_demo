package ar.edu.utn.frbb.tup.springBoot_demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoMovimientos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceCuentaTest {

    @Mock
    private DaoCuenta daoCuenta;

    @Mock
    private DaoMovimientos daoMovimientos;

    @InjectMocks
    private ServiceCuenta serviceCuenta;

    private Cuenta cuenta;
    private CuentaDto cuentaDto;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setSaldo(1000.0);
        cuenta.setEstaA(true);
        cuenta.setTipoCuenta("CUENTA CORRIENTE");
        cuenta.setMoneda("USD");
        cuenta.setTitular("John Doe");

        cuentaDto = new CuentaDto();
        cuentaDto.setSaldo(1000.0);
        cuentaDto.setEstaA(true);
        cuentaDto.setTipoCuenta("CUENTA CORRIENTE");
        cuentaDto.setMoneda("USD");
        cuentaDto.setTitular("John Doe");
    }

    @Test
    void testDepositarExitoso() {
        when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);
        
        var response = serviceCuenta.depositar(cuenta, 500.0);
        
        assertEquals("EXITOSA", response.getEstado());
        assertEquals(1500.0, cuenta.getSaldo());
        verify(daoCuenta).update(any(CuentaDto.class), eq(1L));
    }

    @Test
    void testDepositarCuentaInactiva() {
        cuenta.setEstaA(false);
        
        assertThrows(IllegalArgumentException.class, () -> 
            serviceCuenta.depositar(cuenta, 500.0));
    }

    @Test
    void testRetirarExitoso() {
        when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);
        
        var response = serviceCuenta.retirar(cuenta, 500.0);
        
        assertEquals("EXITOSA", response.getEstado());
        assertEquals(500.0, cuenta.getSaldo());
        verify(daoCuenta).update(any(CuentaDto.class), eq(1L));
    }

    @Test
    void testTransferirExitoso() {
        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setSaldo(500.0);
        cuentaDestino.setEstaA(true);
        cuentaDestino.setTipoCuenta("CUENTA CORRIENTE");

        TransferirDto transferirDto = new TransferirDto();
        transferirDto.setCuentaOrigen(1L);
        transferirDto.setCuentaDestino(2L);
        transferirDto.setMonto(300.0);
        transferirDto.setMoneda("USD");

        when(daoCuenta.buscarCuenta(1L)).thenReturn(cuenta);
        when(daoCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);
        when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);

        var response = serviceCuenta.transferir(transferirDto);

        assertEquals("EXITOSA", response.getEstado());
        assertEquals(700.0, cuenta.getSaldo());
        assertEquals(800.0, cuentaDestino.getSaldo());
        verify(daoMovimientos, times(2)).save(any(MovimientosDto.class));
    }

    @Test
    void testDarDeAltaCuenta() throws Exception {
        when(daoCuenta.save(any(Cuenta.class))).thenReturn(cuenta);
        
        Cuenta nuevaCuenta = serviceCuenta.darDeAltaCuenta(cuentaDto);
        
        assertNotNull(nuevaCuenta);
        verify(daoCuenta).save(any(Cuenta.class));
    }

    @Test
    void testRetirarMontoMayorAlSaldo() {
        assertThrows(IllegalArgumentException.class, () -> 
            serviceCuenta.retirar(cuenta, 2000.0));
    }

    @Test
    void testRetirarDeCajaAhorro() {
        cuenta.setTipoCuenta("CAJA DE AHORRO");
        
        assertThrows(IllegalArgumentException.class, () -> 
            serviceCuenta.retirar(cuenta, 500.0));
    }
}
