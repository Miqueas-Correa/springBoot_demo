package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaValidator cuentaValidator;

    @Autowired
    private ServiceCuenta cuentaSer;

    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto) {
        cuentaValidator.validate(cuentaDto);
        return cuentaSer.darDeAltaCuenta(cuentaDto);
    }
}