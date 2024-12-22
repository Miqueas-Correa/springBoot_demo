package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
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
        if (!cuenta.getEstaA()) return new MsjResponce("FALLIDA.","La cuenta debe estar activa.");
        if (monto < 0) return new MsjResponce("FALLIDA.","No puede ingresar un monto menor a 0.");

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

        return new MsjResponce("EXITOSA", "Se depositó el dinero a la cuenta.");
    }

    // metodo para retirar
    public MsjResponce retirar(Cuenta cuenta, Double monto){
        if (!cuenta.getEstaA()) return new MsjResponce("FALLIDA.","La cuenta debe estar activa.");
        if (monto < 0) return new MsjResponce("FALLIDA.","No puede retirar un monto menor a 0.");
        if (cuenta.getSaldo() < monto) return new MsjResponce("FALLIDA.","No tiene saldo suficiente.");
        if (cuenta.getTipoCuenta().equals("CAJA DE AHORRO")) return new MsjResponce("FALLIDA.","No se puede retirar de una caja de ahorro.");

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

        return new MsjResponce("EXITOSA", "Se retiró el dinero de la cuenta.");
    }

    // metodo para dar de alta la cuenta
    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        daoCuenta.save(cuenta);
        return cuenta;
    }

    // metodo para dar de baja la cuenta
    public void darDeBajaCuenta(Long numeroCuenta) {
        Cuenta cuenta = buscarCuenta(numeroCuenta);
        cuenta.setEstaA(false);

        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDto.setMoneda(cuenta.getMoneda());
        cuentaDto.setTitular(cuenta.getTitular());
        cuentaDto.setEstaA(cuenta.getEstaA());
        cuentaDto.setSaldo(cuenta.getSaldo());
        daoCuenta.update(cuentaDto, numeroCuenta);
    }
}