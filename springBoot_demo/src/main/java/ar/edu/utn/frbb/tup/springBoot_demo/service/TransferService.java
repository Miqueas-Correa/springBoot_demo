package ar.edu.utn.frbb.tup.springBoot_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoMovimientos;

@Service
public class TransferService {
    @Autowired
    private DaoCuenta daoCuenta;
    @Autowired
    private DaoMovimientos daoMovimientos;
    @Autowired
    private ServiceCuenta serviceCuenta;

    // metodo para transferencia
    public MsjResponce transferir(TransferirDto transferirDto){
        Cuenta cuentaOrigen = serviceCuenta.buscarCuenta(transferirDto.getCuentaOrigen());
        Cuenta cuentaDestino = serviceCuenta.buscarCuenta(transferirDto.getCuentaDestino());

        // Sedebe verificar que la moneda de ambas cuentas sea la misma, y en caso de ser
        // transferencia en pesos, el banco cobrará un cargo del 2% de la transferencia si
        // supera $1000000 (o caso de ser dólares cobra un 0.5% si son más de U$S5000).

        Double monto = transferirDto.getMonto();

        if (transferirDto.getMoneda().equals("pesos")){
            if (monto > 1000000) {
                monto = monto - (monto * 0.02);
            }
        } else if (transferirDto.getMoneda().equals("dolares")){
            if (monto > 5000) {
                monto = monto - (monto * 0.005);
            }
        }
        // realizar la transferencia
        cuentaOrigen.transferencia(cuentaDestino, monto);

        // guardar el moviimiento y actualizar las cuentas
        CuentaDto cuentaDtoOrigen = new CuentaDto();
        CuentaDto cuentaDtoDestino = new CuentaDto();

        cuentaDtoOrigen.setTipoCuenta(cuentaOrigen.getTipoCuenta());
        cuentaDtoOrigen.setMoneda(cuentaOrigen.getMoneda());
        cuentaDtoOrigen.setTitular(cuentaOrigen.getTitular());
        cuentaDtoOrigen.setEstaA(cuentaOrigen.getEstaA());
        cuentaDtoOrigen.setSaldo(cuentaOrigen.getSaldo());
        cuentaDtoDestino.setTipoCuenta(cuentaDestino.getTipoCuenta());
        cuentaDtoDestino.setMoneda(cuentaDestino.getMoneda());
        cuentaDtoDestino.setTitular(cuentaDestino.getTitular());
        cuentaDtoDestino.setEstaA(cuentaDestino.getEstaA());
        cuentaDtoDestino.setSaldo(cuentaDestino.getSaldo());

        daoCuenta.update(cuentaDtoOrigen, cuentaOrigen.getNumeroCuenta());
        daoCuenta.update(cuentaDtoDestino, cuentaDestino.getNumeroCuenta());
        // crear el movimiento
        MovimientosDto movimientoDto1 = new MovimientosDto();
        MovimientosDto movimientoDto2 = new MovimientosDto();

        movimientoDto1.setNumeroCuenta(cuentaOrigen.getNumeroCuenta());
        movimientoDto1.setDescripcion("Transferencia a: " + cuentaDestino.getNumeroCuenta() + " - Monto: " + transferirDto.getMonto() + "$ " + transferirDto.getMoneda());
        movimientoDto2.setNumeroCuenta(cuentaDestino.getNumeroCuenta());
        movimientoDto2.setDescripcion("Transferencia de: " + cuentaOrigen.getNumeroCuenta() + " - Monto: " + transferirDto.getMonto() + "$ " + transferirDto.getMoneda());

        daoMovimientos.save(movimientoDto1);
        daoMovimientos.save(movimientoDto2);

        // si llego hasta aca sin lanzar una excepcion es que se realizo la transferencia
        return new MsjResponce("EXITOSA", "La transferencia se realizó correctamente.");
    }
}
