package ar.edu.utn.frbb.tup.springBoot_demo.parsistence;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Banco;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.exception.DataAccessException;

public class DaoBanco extends AbstractBaseDao {
    @Autowired
    DaoCliente daoCliente;

    @Override
    protected String getEntityName() {
        return "Banco";
    }

    // guardar el banco en el archivo
    public void save(Banco banco) {
        for (Cliente cliente : banco.getClientes()) {
            this.daoCliente.save(cliente);
        }
    }

    // parseo el archivo y lo guardo en un banco
    public Banco inicioBanco() {
        try {
            DaoCliente daoCliente = new DaoCliente();
            Banco banco = daoCliente.parseBanco();
            return banco;
        } catch (Exception e) {
            throw new DataAccessException("No se pudo iniciar el banco", e);
        }
    }
}