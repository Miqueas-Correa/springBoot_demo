package ar.edu.utn.frbb.tup.springBoot_demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.springBoot_demo.controller.dto.MovimientosDto;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoMovimientos;

@Service
public class ServiceMovimientos {
    private DaoMovimientos daoMovimientos;

    public ServiceMovimientos(DaoMovimientos daoMovimientos) {
        this.daoMovimientos = daoMovimientos;
    }

    // guardar movimientos
    public void save(MovimientosDto movimientos) {
        if (movimientos == null) throw new IllegalArgumentException("El movimientos no puede ser nulo");
        daoMovimientos.save(movimientos);
    }

    // dar moviemientos
    public List<Movimientos> darMovimientos() {
        return daoMovimientos.darMovimientos();
    }

    // buscar movimientos por cuenta asociada (numeroCuenta)
    public List<Movimientos> buscar(Long numeroCuenta) {
        return daoMovimientos.buscar(numeroCuenta);
    }
}
