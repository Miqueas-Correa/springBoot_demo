package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.net.http.HttpResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoMovimientos;

@Service
public class ServiceCuenta {

    @Autowired
    private DaoCuenta daoCuenta;

    @Autowired
    private DaoMovimientos daoMovimientos;

    // traer todas las cuentas
    public Respuesta<List<Cuenta>> darCuentas(){
        return new Respuesta<>(new MsjResponce("EXITOSA", "Aqui la lista de cuentas:"), daoCuenta.listarCuentas(), HttpStatus.OK);
    }

    // buscar cuenta por numero de cuenta
    public Respuesta<Cuenta> buscarCuenta(Long numeroCuenta){
        return new Respuesta<>(new MsjResponce("EXITOSA", "Aqui la cuenta:"), daoCuenta.buscarCuenta(numeroCuenta), HttpStatus.OK);
    }

    // save
    public Respuesta<Cuenta> save(Cuenta cuenta){
        if (cuenta == null) throw new IllegalArgumentException("Error: no se pudo guardar la cuenta.");
        return new Respuesta<>(new MsjResponce("EXITOSA", "Se guardo la cuenta."), daoCuenta.save(cuenta), HttpStatus.CREATED);
    }

    // update
    public Respuesta<Cuenta> update(CuentaDto cuentaDto, long numeroCuenta){
        if (cuentaDto == null) throw new IllegalArgumentException("Error: no se pudo actualizar la cuenta.");
        return new Respuesta<>(new MsjResponce("EXITOSA", "Se guardo la cuenta."), daoCuenta.update(cuentaDto, numeroCuenta), HttpStatus.CREATED);
    }

    // metodo para depositar
    public Respuesta<Cuenta> depositar(Respuesta<Cuenta> respuesta, Double monto){
        Cuenta cuenta = respuesta.getDatos();
        if (!cuenta.getEstaA()) return new Respuesta<>(new MsjResponce("FALLIDA.","La cuenta debe estar activa."), null, HttpStatus.BAD_REQUEST);
        if (monto < 0) return new Respuesta<>(new MsjResponce("FALLIDA.","El monto debe ser mayor a 0."), null, HttpStatus.BAD_REQUEST);

        cuenta.depositar(monto);

        // actualizar en la base de datos
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDto.setMoneda(cuenta.getMoneda());
        cuentaDto.setTitular(cuenta.getTitular());
        cuentaDto.setEstaA(cuenta.getEstaA());
        cuentaDto.setSaldo(cuenta.getSaldo());

        daoCuenta.update(cuentaDto, cuenta.getNumeroCuenta());
        // crear el movimiento
        MovimientosDto movimientoDto1 = new MovimientosDto();

        movimientoDto1.setNumeroCuenta(cuenta.getNumeroCuenta());
        movimientoDto1.setDescripcion("Deposito : - Monto: " + monto + "$ " + cuenta.getMoneda());

        daoMovimientos.save(movimientoDto1);

        return new Respuesta<>(new MsjResponce("EXITOSA", "Se deposito el monto."), cuenta, HttpStatus.CREATED);
    }

    // metodo para retirar
    public Respuesta<Cuenta> retirar(Respuesta<Cuenta> respuesta, Double monto){
        Cuenta cuenta = respuesta.getDatos();
        if (!cuenta.getEstaA()) return new Respuesta<>(new MsjResponce("FALLIDA.","La cuenta debe estar activa."), null, HttpStatus.BAD_REQUEST);
        if (monto < 0) return new Respuesta<>(new MsjResponce("FALLIDA.","El monto debe ser mayor a 0."), null, HttpStatus.BAD_REQUEST);
        if (cuenta.getSaldo() < monto) return new Respuesta<>(new MsjResponce("FALLIDA.","Saldo insuficiente."), null, HttpStatus.BAD_REQUEST);
        if (cuenta.getTipoCuenta().equals("a")) return new Respuesta<>(new MsjResponce("FALLIDA.","No se puede retirar de una cuenta de caja de ahorro."), null, HttpStatus.BAD_REQUEST);

        cuenta.retirar(monto);

        // actualizar en la base de datos
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDto.setMoneda(cuenta.getMoneda());
        cuentaDto.setTitular(cuenta.getTitular());
        cuentaDto.setEstaA(cuenta.getEstaA());
        cuentaDto.setSaldo(cuenta.getSaldo());
        daoCuenta.update(cuentaDto, cuenta.getNumeroCuenta());
        // crear el movimiento
        MovimientosDto movimientoDto1 = new MovimientosDto();

        movimientoDto1.setNumeroCuenta(cuenta.getNumeroCuenta());
        movimientoDto1.setDescripcion("Retiro : - Monto: " + monto + "$ " + cuenta.getMoneda());

        daoMovimientos.save(movimientoDto1);

        return new Respuesta<>(new MsjResponce("EXITOSA", "Se retiro el monto."), cuenta, HttpStatus.OK);
    }

    // metodo para dar de alta la cuenta
    public Respuesta<Cuenta> darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        daoCuenta.save(cuenta);
        return new Respuesta<>(new MsjResponce("EXITOSA", "Se guardo la cuenta."), cuenta, HttpStatus.CREATED);
    }

    // metodo para dar de baja la cuenta
    public Respuesta<Cuenta> darDeBajaCuenta(Long numeroCuenta) {
        Respuesta<Cuenta> respuesta = buscarCuenta(numeroCuenta);
        Cuenta cuenta = respuesta.getDatos();
        cuenta.setEstaA(false);

        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDto.setMoneda(cuenta.getMoneda());
        cuentaDto.setTitular(cuenta.getTitular());
        cuentaDto.setEstaA(cuenta.getEstaA());
        cuentaDto.setSaldo(cuenta.getSaldo());
        daoCuenta.update(cuentaDto, numeroCuenta);

        return new Respuesta<>(new MsjResponce("EXITOSA", "Se dio de baja la cuenta."), null, HttpStatus.OK);
    }
}