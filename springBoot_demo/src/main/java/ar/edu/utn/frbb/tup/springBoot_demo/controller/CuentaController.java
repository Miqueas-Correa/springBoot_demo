package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.CuentaDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaNotFoundException;
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
    public ResponseEntity<Respuesta<List<Cuenta>>> darCuentas() {
        Respuesta<List<Cuenta>> respuesta = cuentaService.darCuentas();
        if (respuesta.getDatos() == null || respuesta.getDatos().isEmpty()) {
            respuesta.setDatos(new ArrayList<>());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        }
        return new ResponseEntity<>(respuesta, HttpStatus.OK); // 200
    }

    // GET /cuenta/{id}
    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Respuesta<Cuenta>> darCliente(@PathVariable Long numeroCuenta) {
        if (cuentaService.buscarCuenta(numeroCuenta) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        return new ResponseEntity<>(cuentaService.buscarCuenta(numeroCuenta), HttpStatus.OK); // 200
    }

    // POST /cuenta
    @PostMapping
    public ResponseEntity<Respuesta<Cuenta>> crearCuenta(@RequestBody CuentaDto cuentaDto) {
        try {
            cuentaValidator.validate(cuentaDto); // si no se lanza una excepcion, el cliente es valido
            return new ResponseEntity<>(cuentaService.darDeAltaCuenta(cuentaDto), HttpStatus.CREATED); // 201
        } catch (CuentaAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }
    }

    // PUT /cuenta/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Respuesta<Cuenta>> modificarCuenta(@RequestBody CuentaDto cuentaDto, @PathVariable Long id) {
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
    public ResponseEntity<Respuesta<Cuenta>> depositar(@RequestBody Double monto, @PathVariable Long id) {
        if (cuentaService.buscarCuenta(id) == null) return new ResponseEntity<>(
            new Respuesta<>(
                new MsjResponce("FALLIDA", "Error al depositar. Verifique los datos."), null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND); // 404
        try {
            return new ResponseEntity<>(cuentaService.depositar(cuentaService.buscarCuenta(id), monto), HttpStatus.OK); // 200
        }catch (CuentaNotFoundException e) {
            return new ResponseEntity<>(
                new Respuesta<>(
                    new MsjResponce("FALLIDA", "Error al depositar. Verifique los datos."), null, HttpStatus.CONFLICT), HttpStatus.CONFLICT); // 409
        }
    }

    // retirar
    @PutMapping("/retirar/{id}")
    public ResponseEntity<Respuesta<Cuenta>> retirar(@RequestBody Double monto, @PathVariable Long id) {
        if (cuentaService.buscarCuenta(id) == null) return new ResponseEntity<>(
            new Respuesta<>(
                new MsjResponce("FALLIDA", "Error al retirar. Verifique los datos."), null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND); // 404
        try {
            return new ResponseEntity<>(cuentaService.retirar(cuentaService.buscarCuenta(id), monto), HttpStatus.OK); // 200
        }catch (CuentaNotFoundException e) {
            return new ResponseEntity<>(
                new Respuesta<>(
                    new MsjResponce("FALLIDA", "Error al retirar. Verifique los datos."), null, HttpStatus.CONFLICT), HttpStatus.CONFLICT); // 409
        }
    }

    // DELETE /cuenta/{numeroCuenta}
    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<Respuesta<Cuenta>> borrarCuenta(@PathVariable Long numeroCuenta) {
        if (cuentaService.buscarCuenta(numeroCuenta) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        try {
            cuentaService.darDeBajaCuenta(numeroCuenta);
            return new ResponseEntity<>(HttpStatus.OK); // 204
        } catch (CuentaNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }
    }
}