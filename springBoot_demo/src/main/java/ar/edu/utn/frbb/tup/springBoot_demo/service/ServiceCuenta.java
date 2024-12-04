package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoMovimientos;

@Service
public class ServiceCuenta {
    private DaoCuenta daoCuenta;
    private DaoMovimientos daoMovimientos;

    // traer todas las cuentas
    public List<Cuenta> darCuentas(){
        return daoCuenta.listarCuentas();
    }

    // buscar cuenta por numero de cuenta
    public Cuenta buscarCuenta(Long numeroCuenta){
        return daoCuenta.buscarCuenta(numeroCuenta);
    }

    // save
    public void save(Cuenta cuenta){
        if (cuenta == null) throw new IllegalArgumentException("Error: no se pudo guardar la cuenta.");
        daoCuenta.save(cuenta);
    }

    // update
    public Cuenta update(CuentaDto cuentaDto, long numeroCuenta){
        if (cuentaDto == null) throw new IllegalArgumentException("Error: no se pudo actualizar la cuenta.");
        return daoCuenta.update(cuentaDto, numeroCuenta);
    }

    // metodo para depositar
    public MsjResponce depositar(Cuenta cuenta, Double monto){
        if (!cuenta.getEstaA()) throw new IllegalArgumentException("La cuenta debe estar activa.");
        if (monto < 0) throw new IllegalArgumentException("No puede ingresar un monto menor a 0.");

        cuenta.depositar(monto);

        // actualizar en la base de datos
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDto.setMoneda(cuenta.getMoneda());
        cuentaDto.setTitular(cuenta.getTitular());
        cuentaDto.setEstaA(cuenta.getEstaA());
        cuentaDto.setSaldo(cuenta.getSaldo());

        daoCuenta.update(cuentaDto, cuenta.getNumeroCuenta());

        return new MsjResponce("EXITOSA", "Se depositó el dinero a la cuenta.");
    }

    // metodo para retirar
    public MsjResponce retirar(Cuenta cuenta, Double monto){
        if (!cuenta.getEstaA()) throw new IllegalArgumentException("La cuenta debe estar activa.");
        if (monto < 0) throw new IllegalArgumentException("No puede retirar un monto menor a 0.");
        if (cuenta.getSaldo() < monto) throw new IllegalArgumentException("No tiene saldo suficiente.");
        if (cuenta.getTipoCuenta().equals("CAJA DE AHORRO")) throw new IllegalArgumentException("No se puede retirar de una caja de ahorro.");

        cuenta.retirar(monto);

        // actualizar en la base de datos
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDto.setMoneda(cuenta.getMoneda());
        cuentaDto.setTitular(cuenta.getTitular());
        cuentaDto.setEstaA(cuenta.getEstaA());
        cuentaDto.setSaldo(cuenta.getSaldo());
        daoCuenta.update(cuentaDto, cuenta.getNumeroCuenta());

        return new MsjResponce("EXITOSA", "Se retiró el dinero de la cuenta.");
    }

    // metodo para transferencia
    public MsjResponce transferir(TransferirDto transferirDto){
        Cuenta cuentaOrigen = buscarCuenta(transferirDto.getCuentaOrigen());
        Cuenta cuentaDestino = buscarCuenta(transferirDto.getCuentaDestino());

        // realizar la transferencia
        cuentaOrigen.transferencia(cuentaDestino, transferirDto.getMonto());

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
        movimientoDto1.setDescripcion("Transferencia a: " + cuentaDestino.getNumeroCuenta() + " - Monto: " + transferirDto.getMonto() + "$" + transferirDto.getMoneda());
        movimientoDto2.setNumeroCuenta(cuentaDestino.getNumeroCuenta());
        movimientoDto2.setDescripcion("Transferencia de: " + cuentaOrigen.getNumeroCuenta() + " - Monto: " + transferirDto.getMonto() + "$" + transferirDto.getMoneda());

        daoMovimientos.save(movimientoDto1);
        daoMovimientos.save(movimientoDto2);

        // si llego hasta aca sin lanzar una excepcion es que se realizo la transferencia
        return new MsjResponce("EXITOSA", "La transferencia se realizó correctamente.");
    }

    // metodo para dar de alta la cuenta
    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException {
        Cuenta cuenta = new Cuenta(cuentaDto);

        daoCuenta.save(cuenta);
        return cuenta;
    }
}