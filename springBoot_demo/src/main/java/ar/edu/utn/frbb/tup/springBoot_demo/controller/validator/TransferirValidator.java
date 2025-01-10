package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;

@Component
public class TransferirValidator {
    @Autowired
    private ServiceCuenta serviceCuenta;
    public void validate(TransferirDto transferirDto){
        if (transferirDto.getMonto() <= 0) throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        if (transferirDto.getCuentaOrigen() == transferirDto.getCuentaDestino()) throw new IllegalArgumentException("Las cuentas no pueden ser iguales.");
        if (serviceCuenta.buscarCuenta(transferirDto.getCuentaOrigen()) == null) throw new IllegalArgumentException("La cuenta de origen no existe.");
        if (serviceCuenta.buscarCuenta(transferirDto.getCuentaDestino()) == null) throw new IllegalArgumentException("La cuenta de destino no existe.");
        validateSaldo(transferirDto.getCuentaOrigen(), transferirDto.getMonto());
        validateActiva(transferirDto.getCuentaOrigen(), transferirDto.getCuentaDestino());
        validateMoneda(serviceCuenta.buscarCuenta(transferirDto.getCuentaOrigen()).getDatos().getMoneda(),
        serviceCuenta.buscarCuenta(transferirDto.getCuentaDestino()).getDatos().getMoneda(),
        transferirDto.getMoneda());
    }

    private void validateSaldo(Long cuentaOrigen, double monto){
        if (serviceCuenta.buscarCuenta(cuentaOrigen).getDatos().getSaldo() < 0) throw new IllegalArgumentException("La cuenta no tiene saldo suficiente.");
        if (serviceCuenta.buscarCuenta(cuentaOrigen).getDatos().getSaldo() < monto) throw new IllegalArgumentException("La cuenta no tiene saldo suficiente.");
    }

    private void validateActiva(Long cuentaOrigen, Long cuentaDestino){
        if (!serviceCuenta.buscarCuenta(cuentaOrigen).getDatos().getEstaA() || !serviceCuenta.buscarCuenta(cuentaDestino).getDatos().getEstaA()) throw new IllegalArgumentException("Ambas cuentas deben estar activas.");
    }

    private void validateMoneda(String cuentaOrigenM, String cuentaDestinoM, String moneda){
        if (!(cuentaOrigenM.equals(cuentaDestinoM) && (cuentaOrigenM.equals("dolares") || cuentaOrigenM.equals("pesos")))) {
            throw new IllegalArgumentException("Las cuentas deben tener la misma moneda, junto con la moneda de la operacion.");
        }
        if (!moneda.equals("dolares") && !moneda.equals("pesos")) {
            throw new IllegalArgumentException("La moneda de la operación no es válida.");
        }
    }
}