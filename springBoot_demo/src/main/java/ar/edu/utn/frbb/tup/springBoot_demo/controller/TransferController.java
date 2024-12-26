package ar.edu.utn.frbb.tup.springBoot_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.utn.frbb.tup.springBoot_demo.controller.validator.TransferirValidator;
import ar.edu.utn.frbb.tup.springBoot_demo.service.TransferService;
import ar.edu.utn.frbb.tup.springBoot_demo.model.MsjResponce;
import ar.edu.utn.frbb.tup.springBoot_demo.model.dto.TransferirDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.CuentaNotFoundException;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    @Autowired
    private TransferirValidator transferValidator;
    @Autowired
    private TransferService TransferService;

    // transferencia de dinero
    @PutMapping
    public MsjResponce transferir(@RequestBody TransferirDto transferirDto) {
        try {
            transferValidator.validate(transferirDto); // si no se lanza una excepcion, el cliente es valido
            return TransferService.transferir(transferirDto); // 200
        } catch (CuentaNotFoundException e) {
            return new MsjResponce("FALLIDA", 
            "Error en la transferencia. Verifique los datos."); // 409
        }
    }
}
