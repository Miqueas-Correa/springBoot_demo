package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.TransferirValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaValidator cuentaValidator;
    @Autowired
    private TransferirValidator transferirValidator;

    @Autowired
    private ServiceCuenta cuentaService;

    // GET /cuenta
    @GetMapping
    public ResponseEntity<List<Cuenta>> darCuentas() {
        // 204 operacion exitosa pero sin contenido
        if (cuentaService.darCuentas().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(cuentaService.darCuentas(), HttpStatus.OK); // 200
    }

    // GET /cuenta/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> darCliente(@PathVariable Long numeroCuenta) {
        if (cuentaService.buscarCuenta(numeroCuenta) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        return new ResponseEntity<>(cuentaService.buscarCuenta(numeroCuenta), HttpStatus.OK); // 200
    }

    // POST /cuenta
    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody CuentaDto cuentaDto) {
        try {
            cuentaValidator.validate(cuentaDto); // si no se lanza una excepcion, el cliente es valido
            return new ResponseEntity<>(cuentaService.darDeAltaCuenta(cuentaDto), HttpStatus.CREATED); // 201
        } catch (CuentaAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }
    }

    // PUT /cuenta/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> modificarCliente(@RequestBody CuentaDto cuentaDto, @PathVariable Long id) {
        if (cuentaService.buscarCuenta(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        try {
            cuentaValidator.validate(cuentaDto); // si no se lanza una excepcion, el cliente es valido
            return new ResponseEntity<>(cuentaService.update(cuentaDto, id), HttpStatus.OK); // 200
        } catch (CuentaNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }
    }

    // depositar
    @PutMapping("/depositar/{id}")
    public MsjResponce depositar(@RequestBody Double monto, @PathVariable Long id) {
        if (cuentaService.buscarCuenta(id) == null) return new MsjResponce("FALLIDA", "Error al depositar. Verifique los datos."); // 404
        try {
            return cuentaService.depositar(cuentaService.buscarCuenta(id), monto); // 200
        }catch (CuentaNotFoundException e) {
            return new MsjResponce("FALLIDA", "Error al depositar. Verifique los datos."); // 409
        }
    }

    // retirar
    @PutMapping("/retirar/{id}")
    public MsjResponce retirar(@RequestBody Double monto, @PathVariable Long id) {
        if (cuentaService.buscarCuenta(id) == null) return new MsjResponce("FALLIDA", "Error al retirar. Verifique los datos."); // 404
        try {
            return cuentaService.retirar(cuentaService.buscarCuenta(id), monto); // 200
        }catch (CuentaNotFoundException e) {
            return new MsjResponce("FALLIDA", "Error al retirar. Verifique los datos."); // 409
        }
    }

    // transferencia de dinero
    @PutMapping("/transferir")
    public MsjResponce transferir(@RequestBody TransferirDto transferirDto) {
        try {
            transferirValidator.validate(transferirDto); // si no se lanza una excepcion, el cliente es valido
            return cuentaService.transferir(transferirDto); // 200
        } catch (CuentaNotFoundException e) {
            return new MsjResponce("FALLIDA", "Error en la transferencia. Verifique los datos."); // 409
        }
    }
}