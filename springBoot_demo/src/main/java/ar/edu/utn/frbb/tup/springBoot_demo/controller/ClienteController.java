package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Respuesta;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.ClienteDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCliente;
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

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ServiceCliente serviceCliente;

    @Autowired
    private ClienteValidator clienteValidator;

    // GET /cliente
    @GetMapping
    public ResponseEntity<Respuesta<List<Cliente>>> darClientes() {
        return new ResponseEntity<>(serviceCliente.darClientes(), HttpStatus.OK); // 200
    }

    // GET /cliente/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Respuesta<Cliente>> darCliente(@PathVariable Long id) {
        if (serviceCliente.buscarClientePorDni(id) == null) return new ResponseEntity<>(
            new Respuesta<>(
                new MsjResponce("FALLIDA", "Cliente con dni: " + id + " no encontrado."), null, HttpStatus.NOT_FOUND), 
                HttpStatus.NOT_FOUND); // 404
        return new ResponseEntity<>(serviceCliente.buscarClientePorDni(id), HttpStatus.OK); // 200
    }

    // POST /cliente
    @PostMapping
    public ResponseEntity<Respuesta<Cliente>> crearCliente(@RequestBody ClienteDto clienteDto) {
        try {
            clienteValidator.validate(clienteDto); // si no se lanza una excepcion, el cliente es valido
            return new ResponseEntity<>(serviceCliente.altaCliente(clienteDto), HttpStatus.CREATED); // 201
        } catch (ClienteAlreadyExistsException e) {
            System.err.println("Conflicto: " + e.getMessage());
            return new ResponseEntity<>(
                new Respuesta<>(
                    new MsjResponce("FALLIDA", "Conflicto."), null, HttpStatus.CONFLICT), 
                    HttpStatus.CONFLICT); // 409
        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            return new ResponseEntity<>(
                new Respuesta<>(
                    new MsjResponce("FALLIDA", "Error."), null, HttpStatus.BAD_REQUEST), 
                    HttpStatus.BAD_REQUEST); // 400
        }
    }

    // PUT /cliente/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Respuesta<Cliente>> modificarCliente(@RequestBody ClienteDto clienteDto, @PathVariable Long id) {
        if (serviceCliente.buscarClientePorDni(id) == null) return new ResponseEntity<>(
            new Respuesta<>(new MsjResponce("FALLIDA", "No hay cliente con el dni: " + id), 
            null, HttpStatus.NOT_FOUND), 
            HttpStatus.NOT_FOUND); // 404
        try {
            clienteValidator.validate(clienteDto); // si no se lanza una excepcion, el cliente es valido
            return new ResponseEntity<>(serviceCliente.update(clienteDto, id), HttpStatus.OK); // 200
        } catch (ClienteAlreadyExistsException e) {
            return new ResponseEntity<>(
                new Respuesta<>(
                    new MsjResponce("FALLIDA", "Conflicto."), null, HttpStatus.CONFLICT), 
                    HttpStatus.CONFLICT); // 409
        }
    }

    // DELETE /cliente/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Respuesta<Cliente>> darDeBajaCliente(@PathVariable Long id) {
        if (serviceCliente.buscarClientePorDni(id) == null) return new ResponseEntity<>(
            new Respuesta<>(
            new MsjResponce("FALLIDA", "No hay cliente con el dni: " + id), null, HttpStatus.NOT_FOUND), 
            HttpStatus.NOT_FOUND); // 404
        try {
            serviceCliente.bajaCliente(id);
            return new ResponseEntity<>(
                new Respuesta<>(
                new MsjResponce("EXITOSA", "Se elimino el cliente con el dni: " + id), 
                null, 
                HttpStatus.OK), 
                HttpStatus.OK); // 200
        } catch (Exception e) {
            return new ResponseEntity<>(
                new Respuesta<>(
                new MsjResponce("FALLIDA", "Error."), 
                null, 
                HttpStatus.BAD_REQUEST), 
                HttpStatus.BAD_REQUEST); // 400
        }
    }
}