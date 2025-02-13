package ar.edu.utn.frbb.tup.springBoot_demo.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;

@Component
public class TransferirValidator {
    @Autowired
    private ServiceCuenta serviceCuenta;

    public void validate(TransferirDto transferirDto) {
        if (transferirDto.getMonto() <= 0) 
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");

        if (transferirDto.getCuentaOrigen().equals(transferirDto.getCuentaDestino())) 
            throw new IllegalArgumentException("Las cuentas no pueden ser iguales.");

        Respuesta<Cuenta> respuestaOrigen = serviceCuenta.buscarCuenta(transferirDto.getCuentaOrigen());
        Respuesta<Cuenta> respuestaDestino = serviceCuenta.buscarCuenta(transferirDto.getCuentaDestino());

        Cuenta cuentaOrigen = validarCuentaExistente(respuestaOrigen, "La cuenta de origen no existe.");
        Cuenta cuentaDestino = validarCuentaExistente(respuestaDestino, "La cuenta de destino no existe.");
        validateSaldo(cuentaOrigen, transferirDto.getMonto());
        validateActiva(cuentaOrigen, cuentaDestino);
        validateMoneda(cuentaOrigen.getMoneda(), cuentaDestino.getMoneda(), transferirDto.getMoneda());
    }

    private Cuenta validarCuentaExistente(Respuesta<Cuenta> respuesta, String mensajeError) {
        if (respuesta == null || respuesta.getDatos() == null) {
            throw new IllegalArgumentException(mensajeError);
        }
        return respuesta.getDatos();
    }

    private void validateSaldo(Cuenta cuentaOrigen, double monto) {
        if (cuentaOrigen.getSaldo() < 0) 
            throw new IllegalArgumentException("La cuenta no tiene saldo suficiente.");
        if (cuentaOrigen.getSaldo() < monto) 
            throw new IllegalArgumentException("La cuenta no tiene saldo suficiente para esta operación.");
    }

    private void validateActiva(Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        if (!cuentaOrigen.getEstaA() || !cuentaDestino.getEstaA()) 
            throw new IllegalArgumentException("Ambas cuentas deben estar activas.");
    }

    private void validateMoneda(String cuentaOrigenM, String cuentaDestinoM, String moneda) {
        if (!cuentaOrigenM.equalsIgnoreCase(cuentaDestinoM))
            throw new IllegalArgumentException("Las cuentas deben tener la misma moneda.");
        if (!moneda.equalsIgnoreCase("dolares") && !moneda.equalsIgnoreCase("pesos"))
            throw new IllegalArgumentException("La moneda de la operación no es válida.");
        if (!moneda.equalsIgnoreCase(cuentaOrigenM))
            throw new IllegalArgumentException("La moneda de la transferencia debe coincidir con la de la cuenta.");
        if  (!cuentaOrigenM.equalsIgnoreCase(moneda))
            throw new IllegalArgumentException("La moneda de la transferencia debe ser la misma que la de la cuenta.");
    }
}