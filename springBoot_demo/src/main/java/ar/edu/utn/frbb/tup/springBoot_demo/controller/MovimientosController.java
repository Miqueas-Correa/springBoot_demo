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
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.MovimientosValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.MovimientoAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceMovimientos;

@RestController
@RequestMapping("cuenta/movimientos")
public class MovimientosController {

    @Autowired
    private ServiceMovimientos serviceMovimientos;

    @Autowired
    private MovimientosValidator movimientosValidator;

    // GET /cuenta/movimientos
    @GetMapping
    public ResponseEntity<List<Movimientos>> darMovimientos() {
        // 204 operacion exitosa pero sin contenido
        if (serviceMovimientos.darMovimientos().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(serviceMovimientos.darMovimientos(), HttpStatus.OK); // 200
    }

    // GET /cuenta/movimientos/{id} movimientos asociados a una cuenta
    @GetMapping("/{id}")
    public ResponseEntity<List<Movimientos>> darMovimientos(@PathVariable Long id) {
        if (serviceMovimientos.buscar(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        return new ResponseEntity<>(serviceMovimientos.buscar(id), HttpStatus.OK); // 200
    }

    // POST /cuenta/movimientos
    @PostMapping
    public ResponseEntity<Movimientos> crearMovimientos(@RequestBody MovimientosDto movimientosDto) {
        try {
            movimientosValidator.validate(movimientosDto);
            serviceMovimientos.save(movimientosDto);
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        } catch (MovimientoAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }
    }

    // PUT /cuenta/movimientos/{id} (NO CREO QUE SE DEBA PODER MODIFICAR UN MOVIMIENTO)

    // para mi no tendria sentido borrar un movimiento porque es un registros de la actividad de la cuenta
    // DELETE /cuenta/movimientos/{id}
}
