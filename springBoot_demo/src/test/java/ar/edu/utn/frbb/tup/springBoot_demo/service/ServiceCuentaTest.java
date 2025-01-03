package ar.edu.utn.frbb.tup.springBoot_demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaAlreadyExistsException;
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
        cuenta.setTitular(12345678L);

        cuentaDto = new CuentaDto();
        cuentaDto.setSaldo(1000.0);
        cuentaDto.setEstaA(true);
        cuentaDto.setTipoCuenta("CUENTA CORRIENTE");
        cuentaDto.setMoneda("USD");
        cuentaDto.setTitular(12345678L);
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
        MsjResponce resultado = serviceCuenta.depositar(cuenta, 500.0);
        assertEquals("FALLIDA.", resultado.getEstado());
        assertEquals("La cuenta debe estar activa.", resultado.getMensaje());
    }

    @Test
    void testRetirarExitoso() {
        when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);

        var response = serviceCuenta.retirar(cuenta, 500.0);

        assertEquals("EXITOSA", response.getEstado());
        assertEquals(500.0, cuenta.getSaldo());
        verify(daoCuenta).update(any(CuentaDto.class), eq(1L));
    }

    // @Test
    // void testTransferirExitoso() {
    //     Cuenta cuentaDestino = new Cuenta();
    //     cuentaDestino.setNumeroCuenta(2L);
    //     cuentaDestino.setSaldo(500.0);
    //     cuentaDestino.setEstaA(true);
    //     cuentaDestino.setTipoCuenta("CUENTA CORRIENTE");

    //     TransferirDto transferirDto = new TransferirDto();
    //     transferirDto.setCuentaOrigen(1L);
    //     transferirDto.setCuentaDestino(2L);
    //     transferirDto.setMonto(300.0);
    //     transferirDto.setMoneda("USD");

    //     when(daoCuenta.buscarCuenta(1L)).thenReturn(cuenta);
    //     when(daoCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);
    //     when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);

    //     var response = serviceCuenta.transferir(transferirDto);

    //     assertEquals("EXITOSA", response.getEstado());
    //     assertEquals(700.0, cuenta.getSaldo());
    //     assertEquals(800.0, cuentaDestino.getSaldo());
    //     verify(daoMovimientos, times(2)).save(any(MovimientosDto.class));
    // }

    @Test
    void testDarDeAltaCuenta() throws CuentaAlreadyExistsException {
        // Simula el comportamiento del método save (void)
        doNothing().when(daoCuenta).save(any(Cuenta.class));

        // Ejecuta el método que estás probando
        Cuenta nuevaCuenta = serviceCuenta.darDeAltaCuenta(cuentaDto);

        // Asegúrate de que la cuenta no es null
        assertNotNull(nuevaCuenta);

        // Verifica que save fue llamado con cualquier objeto Cuenta
        verify(daoCuenta).save(any(Cuenta.class));
    }


    @Test
    void testRetirarMontoMayorAlSaldo() {
        cuenta.setEstaA(true);
        cuenta.setSaldo(1000.0);
        MsjResponce resultado = serviceCuenta.retirar(cuenta, 2000.0);
        assertEquals("FALLIDA.", resultado.getEstado());
        assertEquals("No tiene saldo suficiente.", resultado.getMensaje());
    }

    @Test
    void testRetirarDeCajaAhorro() {
        cuenta.setEstaA(true);
        cuenta.setSaldo(1000.0);
        cuenta.setTipoCuenta("CAJA DE AHORRO");
        MsjResponce resultado = serviceCuenta.retirar(cuenta, 500.0);
        assertEquals("FALLIDA.", resultado.getEstado());
        assertEquals("No se puede retirar de una caja de ahorro.", resultado.getMensaje());
    }

    // @Test
    // void testTransferirMonedaDiferente() {
    //     Cuenta cuentaDestino = new Cuenta();
    //     cuentaDestino.setNumeroCuenta(2L);
    //     cuentaDestino.setSaldo(500.0);
    //     cuentaDestino.setEstaA(true);
    //     cuentaDestino.setTipoCuenta("CUENTA CORRIENTE");

    //     TransferirDto transferirDto = new TransferirDto();
    //     transferirDto.setCuentaOrigen(1L);
    //     transferirDto.setCuentaDestino(2L);
    //     transferirDto.setMonto(300.0);
    //     transferirDto.setMoneda("PESOS");

    //     when(daoCuenta.buscarCuenta(1L)).thenReturn(cuenta);
    //     when(daoCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);
    //     when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);

    //     var response = serviceCuenta.transferir(transferirDto);

    //     assertEquals("EXITOSA", response.getEstado());
    //     verify(daoMovimientos, times(2)).save(any(MovimientosDto.class));
    // }

    // @Test
    // void testTransferirMontoAltoPesos() {
    //     Cuenta cuentaDestino = new Cuenta();
    //     cuentaDestino.setNumeroCuenta(2L);
    //     cuentaDestino.setSaldo(500.0);
    //     cuentaDestino.setEstaA(true);

    //     TransferirDto transferirDto = new TransferirDto();
    //     transferirDto.setCuentaOrigen(1L);
    //     transferirDto.setCuentaDestino(2L);
    //     transferirDto.setMonto(1500000.0);
    //     transferirDto.setMoneda("PESOS");

    //     when(daoCuenta.buscarCuenta(1L)).thenReturn(cuenta);
    //     when(daoCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);
    //     when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);

    //     var response = serviceCuenta.transferir(transferirDto);

    //     assertEquals("EXITOSA", response.getEstado());
    //     verify(daoMovimientos, times(2)).save(any(MovimientosDto.class));
    // }

    // @Test
    // void testTransferirMontoAltoDolares() {
    //     Cuenta cuentaDestino = new Cuenta();
    //     cuentaDestino.setNumeroCuenta(2L);
    //     cuentaDestino.setSaldo(10000.0);
    //     cuentaDestino.setEstaA(true);

    //     TransferirDto transferirDto = new TransferirDto();
    //     transferirDto.setCuentaOrigen(1L);
    //     transferirDto.setCuentaDestino(2L);
    //     transferirDto.setMonto(6000.0);
    //     transferirDto.setMoneda("DOLARES");

    //     when(daoCuenta.buscarCuenta(1L)).thenReturn(cuenta);
    //     when(daoCuenta.buscarCuenta(2L)).thenReturn(cuentaDestino);
    //     when(daoCuenta.update(any(CuentaDto.class), anyLong())).thenReturn(cuenta);

    //     var response = serviceCuenta.transferir(transferirDto);

    //     assertEquals("EXITOSA", response.getEstado());
    //     verify(daoMovimientos, times(2)).save(any(MovimientosDto.class));
    // }

    @Test
    void testDarDeBajaCuenta() {
        when(daoCuenta.buscarCuenta(1L)).thenReturn(cuenta);
        when(daoCuenta.update(any(CuentaDto.class), eq(1L))).thenReturn(cuenta);

        serviceCuenta.darDeBajaCuenta(1L);

        assertFalse(cuenta.getEstaA());
        verify(daoCuenta).update(any(CuentaDto.class), eq(1L));
    }

    @Test
    void testDepositarMontoNegativo() {
        cuenta.setEstaA(true);
        MsjResponce resultado = serviceCuenta.depositar(cuenta, -100.0);
        assertEquals("FALLIDA.", resultado.getEstado());
        assertEquals("No puede ingresar un monto menor a 0.", resultado.getMensaje());
    }

    @Test
    void testRetirarMontoNegativo() {
        cuenta.setEstaA(true);
        cuenta.setSaldo(1000.0);
        MsjResponce resultado = serviceCuenta.retirar(cuenta, -100.0);
        assertEquals("FALLIDA.", resultado.getEstado());
        assertEquals("No puede retirar un monto menor a 0.", resultado.getMensaje());
    }

    @Test
    void testSaveNull() {
        assertThrows(IllegalArgumentException.class, () ->
            serviceCuenta.save(null));
    }

    @Test
    void testUpdateNull() {
        assertThrows(IllegalArgumentException.class, () ->
            serviceCuenta.update(null, 1L));
    }
}
