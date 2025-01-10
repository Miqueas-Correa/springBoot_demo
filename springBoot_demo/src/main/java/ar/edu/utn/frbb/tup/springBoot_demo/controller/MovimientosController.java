package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceMovimientos;

@RestController
@RequestMapping("cuenta")
public class MovimientosController {
    @Autowired
    private ServiceMovimientos serviceMovimientos;

    // GET /cuenta/{id}/movimientos movimientos asociados a una cuenta
    @GetMapping("/{id}/movimientos")
    public ResponseEntity<Movimientos> darMovimientos(@PathVariable Long id) {
        Movimientos movimientos = serviceMovimientos.buscar(id);
        if (movimientos.getTransacciones() == null || movimientos.getTransacciones().isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        return new ResponseEntity<>(movimientos, HttpStatus.OK); // 200
    }
}