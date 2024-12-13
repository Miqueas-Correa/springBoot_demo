package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
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
    public ResponseEntity<List<Cliente>> darClientes() {
        // 204 operacion exitosa pero sin contenido
        if (serviceCliente.darClientes().isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(serviceCliente.darClientes(), HttpStatus.OK); // 200
    }

    // GET /cliente/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> darCliente(@PathVariable Long id) {
        if (serviceCliente.buscarClientePorDni(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        return new ResponseEntity<>(serviceCliente.buscarClientePorDni(id), HttpStatus.OK); // 200
    }

    // POST /cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody ClienteDto clienteDto) {
        try {
            clienteValidator.validate(clienteDto); // si no se lanza una excepcion, el cliente es valido
            return new ResponseEntity<>(serviceCliente.altaCliente(clienteDto), HttpStatus.CREATED); // 201
        } catch (ClienteAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }
    }

    // PUT /cliente/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> modificarCliente(@RequestBody ClienteDto clienteDto, @PathVariable Long id) {
        if (serviceCliente.buscarClientePorDni(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        try {
            clienteValidator.validate(clienteDto); // si no se lanza una excepcion, el cliente es valido
            return new ResponseEntity<>(serviceCliente.update(clienteDto, id), HttpStatus.OK); // 200
        } catch (ClienteAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }
    }

    // DELETE /cliente/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Cliente> darDeBajaCliente(@PathVariable Long id) {
        if (serviceCliente.buscarClientePorDni(id) == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        try {
            serviceCliente.bajaCliente(id);
            return new ResponseEntity<>(HttpStatus.OK); // 200
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400
        }
    }
}