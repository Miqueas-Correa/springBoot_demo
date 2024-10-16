package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoCuenta;

@Service
public class ServiceCuenta {
    private DaoCuenta DaoCuenta;

    // traer todas las cuentas
    public List<Cuenta> darCuentas(){
        return DaoCuenta.listarCuentas();
    }

    // buscar cuenta por numero de cuenta
    public Cuenta buscarCuenta(Long numeroCuenta){
        return DaoCuenta.buscarCuenta(numeroCuenta);
    }

    // save
    public void save(Cuenta cuenta){
        if (cuenta == null) throw new IllegalArgumentException("Error: no se pudo guardar la cuenta.");
        DaoCuenta.save(cuenta);
    }

    // update
    public void update(Cuenta cuenta){
    }

    // metodo para depositar
    public boolean depositar(Cuenta cuenta, Double monto){
        if (!cuenta.getEstaA()) throw new IllegalArgumentException("La cuenta debe estar activa");
        if (monto > 0) {
            cuenta.deposito(monto);
            System.out.println("Se ingresó el dinero a la cuenta.");
            return false;
        }else{
            System.out.println("Error: El monto debe ser mayor a 0.");
            return true;
        }
    }

    // metodo para retirar
    public boolean retirar(Cuenta cuenta, Double monto){
        if (!cuenta.getEstaA()) throw new IllegalArgumentException("La cuenta debe estar activa");
        if (monto > 0) {
            cuenta.retiro(monto);
            System.out.println("Se retiró el dinero a la cuenta.");
            return false;
        }else{
            System.out.println("Error: El monto debe ser mayor a 0.");
            return true;
        }
    }

    // metodo para transferencia
    public boolean transferencia(Cuenta cuenta, Double monto, String alias, Long cbu_cvu, Banco banco){
        if (!cuenta.getEstaA()) throw new IllegalArgumentException("La cuenta debe estar activa");
        if (monto > 0) {
            cuenta.transferir(monto, alias, cbu_cvu, banco);
            System.out.println("Se transferió el dinero a la cuenta.");
            return false;
        }else{
            System.out.println("Error: El monto debe ser mayor a 0.");
            return true;
        }
    }

    // metodo para dar de alta la cuenta
    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto){
        Cuenta cuenta = new Cuenta(cuentaDto);
    }
}