package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaValidator cuentaValidator;

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
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }
    }
}