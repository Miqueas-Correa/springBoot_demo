package ar.edu.utn.frbb.tup.springBoot_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;
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
            // verificar que sean del mismo banco o no
            if (!cuentaOrigen.getBanco().equals(cuentaDestino.getBanco())){
                if (!BanelcoService.getRandomBoolean()) return new MsjResponce("FALLIDA.","No se puede realizar la transferencia entre bancos.");
            }
            return realizarTransferencia(cuentaOrigen, cuentaDestino, monto);
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

    private void crearMovimiento(Long numeroCuenta, String descripcion){
        MovimientosDto movimientoDto = new MovimientosDto();

        movimientoDto.setNumeroCuenta(numeroCuenta);
        movimientoDto.setDescripcion(descripcion);

        serviceMovimientos.save(movimientoDto);
    }

    private MsjResponce realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, Double monto){
        try {
            // realizar la transferencia del mismo bamco
            cuentaOrigen.transferencia(cuentaDestino, monto);

            // actualizar las cuentas
            updateCuenta(cuentaOrigen);
            updateCuenta(cuentaDestino);

            // crear el movimiento
            crearMovimiento(cuentaOrigen.getNumeroCuenta(),
            "Transferencia a: " + cuentaDestino.getNumeroCuenta() +
            " - Monto: " + monto + "$ " + cuentaDestino.getMoneda() +
            " - Banco: " + cuentaDestino.getBanco());
            crearMovimiento(cuentaOrigen.getNumeroCuenta(),
            "Transferencia a: " + cuentaDestino.getNumeroCuenta() +
            " - Monto: " + monto + "$ " + cuentaDestino.getMoneda() +
            " - Banco: " + cuentaDestino.getBanco());

            return new MsjResponce("EXITOSA", "La transferencia se realizó correctamente.");
        } catch (Exception e) {
            return new MsjResponce("FALLO", "La transferencia NO se realizó correctamente.");
        }
    }
}
