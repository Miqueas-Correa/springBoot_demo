package ar.edu.utn.frbb.tup.springBoot_demo.service;

import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Banco;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.parsistence.DaoCuenta;

@Service
public class ServiceCuenta {

    public boolean validarCbu(Long cbu){
        if (String.valueOf(cbu).length() == 16 && cbu > 0) return true;
        return false;
    }

    public static boolean validarAlias(String alias, Banco banco){
        return banco.buscarAlias(alias);
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
        if (cuentaDto == null) throw new IllegalArgumentException("Error: no se pudo dar de alta la cuenta.");
        if (cuentaDto.getNombre() == null || cuentaDto.getNombre_y_apellido().isEmpty()) throw new IllegalArgumentException("Error: el nombre no puede ser nulo o vacio.");
        if (cuentaDto.getAlias() == null || cuentaDto.getAlias().isEmpty()) throw new IllegalArgumentException("Error: el alias no puede ser nulo o vacio.");
        if (cuentaDto.getTipo() == null || cuentaDto.getTipo().isEmpty()) throw new IllegalArgumentException("Error: el tipo no puede ser nulo o vacio.");
        if (cuentaDto.getFechaNacimiento() == null) throw new IllegalArgumentException("Error: la fecha de nacimiento no puede ser nula.");
        if (cuentaDto.getFechaApertura() == null) throw new IllegalArgumentException("Error: la fecha de apertura no puede ser nula.");
        if (cuentaDto.getSaldo() == null) throw new IllegalArgumentException("Error: el saldo no puede ser nulo.");
        if (cuentaDto.getSaldo() < 0) throw new IllegalArgumentException("Error: el saldo debe ser mayor a 0.");
        if (cuentaDto.getCvu() == null) throw new IllegalArgumentException("Error: el cvu no puede ser nulo.");
        if (cuentaDto.getCvu() < 0 && cuentaDto.getCvu().toString().length() !) throw new IllegalArgumentException("Error: el cvu debe ser mayor a 0.");


    }
}