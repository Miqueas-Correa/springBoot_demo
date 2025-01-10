package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Transaccion;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;

@Service
public class TransferService {
    @Autowired
    private ServiceMovimientos serviceMovimientos;
    @Autowired
    private ServiceCuenta serviceCuenta;

    // metodo para transferencia
    public MsjResponce transferir(TransferirDto transferirDto){
        try {
            Cuenta cuentaOrigen = serviceCuenta.buscarCuenta(transferirDto.getCuentaOrigen()).getDatos();
            Cuenta cuentaDestino = serviceCuenta.buscarCuenta(transferirDto.getCuentaDestino()).getDatos();

            // Sedebe verificar que la moneda de ambas cuentas sea la misma, y en caso de ser
            // transferencia en pesos, el banco cobrará un cargo del 2% de la transferencia si
            // supera $1000000 (o caso de ser dólares cobra un 0.5% si son más de U$S5000).

            Double monto = transferirDto.getMonto();
            Double comision = 0.0;

            if (transferirDto.getMoneda().equals("pesos")){
                if (monto > 1000000) {
                    comision = (monto * 0.02);
                }
            } else if (transferirDto.getMoneda().equals("dolares")){
                if (monto > 5000) {
                    comision = (monto * 0.005);
                }
            }
            // verificar que sean del mismo banco o no
            if (!cuentaOrigen.getBanco().equals(cuentaDestino.getBanco())){
                if (!BanelcoService.getRandomBoolean()) return new MsjResponce("FALLIDA.","No se puede realizar la transferencia entre bancos.");
            }
            return realizarTransferencia(cuentaOrigen, cuentaDestino, monto, comision);
        } catch (Exception e) {
            return new MsjResponce("FALLO", "La transferencia NO se realizó correctamente.");
        }
    }

    private void updateCuenta(Cuenta cuenta){
        CuentaDto cuentaDto = new CuentaDto();

        cuentaDto.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDto.setMoneda(cuenta.getMoneda());
        cuentaDto.setTitular(cuenta.getTitular());
        cuentaDto.setEstaA(cuenta.getEstaA());
        cuentaDto.setSaldo(cuenta.getSaldo());

        serviceCuenta.update(cuentaDto, cuenta.getNumeroCuenta());
    }

    private void crearMovimiento(Long numeroCuenta, String tipo, String descripcion, Double monto){
        List<Transaccion> transacciones = new ArrayList<>();
        transacciones.add(new Transaccion(tipo, descripcion, monto));
        Movimientos movimiento = new Movimientos(numeroCuenta, transacciones);

        serviceMovimientos.save(movimiento);
    }

    private MsjResponce realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, Double monto, Double comision){
        try {
            // realizar la transferencia del mismo bamco
            cuentaOrigen.transferencia(cuentaDestino, monto, comision);

            // actualizar las cuentas
            updateCuenta(cuentaOrigen);
            updateCuenta(cuentaDestino);

            // crear el movimiento
            crearMovimiento(cuentaOrigen.getNumeroCuenta(), "Transferencia",
            "Enviada a: " + cuentaDestino.getNumeroCuenta() +
            " - Banco: " + cuentaDestino.getBanco(),
            monto);
            crearMovimiento(cuentaDestino.getNumeroCuenta(), "Transferencia",
            "Resivida de: " + cuentaOrigen.getNumeroCuenta() +
            " - Banco: " + cuentaDestino.getBanco(),
            monto);

            return new MsjResponce("EXITOSA", "La transferencia se realizó correctamente.");
        } catch (Exception e) {
            return new MsjResponce("FALLO", "La transferencia NO se realizó correctamente.");
        }
    }
}