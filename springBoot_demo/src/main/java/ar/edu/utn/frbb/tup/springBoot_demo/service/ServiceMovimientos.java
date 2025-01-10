package ar.edu.utn.frbb.tup.springBoot_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoMovimientos;

@Service
public class ServiceMovimientos {
    @Autowired
    private DaoMovimientos daoMovimientos;

    // guardar movimientos
    public void save(Movimientos movimientos) {
        if (movimientos == null) throw new IllegalArgumentException("El movimientos no puede ser nulo");
        daoMovimientos.save(movimientos);
    }

    // buscar movimientos por cuenta asociada (numeroCuenta)
    public Movimientos buscar(Long numeroCuenta) {
        return daoMovimientos.buscar(numeroCuenta);
    }
}
