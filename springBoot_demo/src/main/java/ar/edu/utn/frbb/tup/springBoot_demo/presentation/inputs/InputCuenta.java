package ar.edu.utn.frbb.tup.springBoot_demo.presentation.inputs;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Banco;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.presentation.shows.PrintsMenues;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCuenta;

@Component
public class InputCuenta extends BaseInputProcessor {
    @Autowired
    PrintsMenues p_menu;
    @Autowired
    ServiceCuenta serviceCuenta;

    // opcion 3 , operar con una cuenta
    public void operarConCuenta(Cliente cliente, Banco banco){
        clearScreen();
        while (true) {
            p_menu.menuCuentaInicio();// iniciar sesion o crear cuenta
            int opcion = obtenerOpcionValida();
            switch(opcion){
                case 0:
                    //opcion 0, salir del menu
                    clearScreen();
                    return;
                case 1:
                    //iniciar sesion
                    iniciarSesionCuenta(cliente, banco);
                    break;
                case 2:
                    //crear cuenta/registrarse
                    crearCuenta(cliente, banco);
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
        }
    }
    // metodos de accion cuenta
    public void crearCuenta(Cliente cliente, Banco banco) {
        clearScreen();
        Cuenta cuentaNueva = new Cuenta();

        // Ingresar alias
        String alias = obtenerAlias(cliente, banco);
        cuentaNueva.setAlias(alias);

        // Ingresar CVU/CBU
        Long cvu = obtenerCvuCbu(cliente);
        cuentaNueva.setCbu_cvu(cvu);

        // Ingresar ID, le asigno el dni del cliente como id para asociarlo con la cuenta
        Long id = cliente.getDni();
        cuentaNueva.setId_del_titular(id);

        // Ingresar saldo inicial
        cuentaNueva.deposito(0);

        // Ingresar tipo de cuenta
        String tipo = obtenerTipoCuenta();
        cuentaNueva.setTipo_de_cuenta(tipo);

        // Ingresar moneda, por defecto es peso argentino
        cuentaNueva.setMoneda("ARS");

        // Ingresar fecha de alta
        cuentaNueva.setFecha_de_apertura(LocalDateTime.now());

        // Agregar la cuenta al cliente
        cliente.setCuentas(cuentaNueva);

        // guardar en el archivo
        serviceCuenta.save(cuentaNueva);

        System.out.println("Cuenta creada con éxito!");
    }

    // método para obtener tipo de cuenta
    private String obtenerTipoCuenta(){
        // ingresar tipo de cuenta
        Set<String> tiposValidos = new HashSet<>(Arrays.asList("CAJA DE AHORRO", "CORRIENTE"));
        while (true) {
            System.out.println("Ingrese el nuevo tipo de cuenta (CAJA DE AHORRO/CORRIENTE):");
            String tipo = obtenerCadenaValida().toUpperCase();
            scanner.nextLine();
            if (tiposValidos.contains(tipo)) {
                return tipo;
            } else {
                System.out.println("Tipo de cuenta inválido. Por favor, ingrese CAJA DE AHORRO o CORRIENTE.");
            }
        }
    }

    // método para obtener alias
    private String obtenerAlias(Cliente cliente, Banco banco) {
        while (true) {
            System.out.println("Ingrese el alias de la cuenta:");
            String alias = scanner.nextLine().trim();
            if (alias.equals("")) {
                System.out.println("Error: no puede ser una cadena vacia.");
            }else{
                if (ServiceCuenta.validarAlias(alias, banco)) {
                    System.out.println("El alias ya está en uso. Ingrese otro:");
                } else {
                    cliente.agregarAlias(alias);
                    return alias;
                }
            }
        }
    }

    // método para obtener un cvu valido
    private Long obtenerCvuCbu(Cliente cliente) {
        while (true) {
            System.out.println("Ingrese el CVU/CBU de la cuenta:");
            Long cvu = obtenerOpcionValidaLong();
            if (serviceCuenta.validarCbu(cvu)) {
                if (cliente.estaCbuCvuUsado(cvu)) {
                    System.out.println("El CVU/CBU ya está en uso. Ingrese otro:");
                } else {
                    cliente.agregarCbuCvu(cvu);
                    return cvu;
                }
            }else{
                System.out.println("Error: Un cvu/cbu lleva 16 digitos y es un número positivo.");
            }
        }
    }
    //opcion 1, iniciar sesion en una cuenta
    private void iniciarSesionCuenta(Cliente cliente, Banco banco){
        clearScreen();
        while (true) {
            try {
                System.out.println("Ingrese el id de la cuenta o '0' para volver al menú anterior:");
                String idCuenta = scanner.nextLine().trim();

                if ("0".equalsIgnoreCase(idCuenta)) return; // Permite al usuario salir del proceso de inicio de sesión

                Cuenta cuenta = cliente.buscarCuentaPorId(idCuenta);

                if (cuenta != null) {
                    menuSCuenta(cuenta, banco);
                }
            } catch (AccountNotFoundException e) {
                System.out.println("Error: Cuenta no encontrada. Intente nuevamente." + e.getMessage());
            }
        }
    }

    // metodo para mostrar el menu de la cuenta
    private void menuSCuenta(Cuenta cuenta, Banco banco){
        clearScreen();
        while (true) {
            // menu de la cuenta
            p_menu.menuSesionCuenta();
            int opcion = obtenerOpcionValida();

            switch(opcion) {
                case 0:
                    //opcion 0, salir del menu
                    clearScreen();
                    return;
                case 1:
                    //opcion 1, ver datos de la cuenta
                    clearScreen();
                    cuenta.getDatosCuenta();
                    break;
                case 2:
                    //opcion 2, ver saldo de la cuenta
                    clearScreen();
                    System.out.println("Saldo: \n" + cuenta.getSaldo());
                    break;
                case 3:
                    // opcion 3, ver movimientos de la cuenta
                    clearScreen();
                    System.out.println("Movimientos: \n" + cuenta.getMovimientos());
                    break;
                case 4:
                    // opcion 4, ingresar dinero a la cuenta
                    ingresarDinero(cuenta);
                    break;
                case 5:
                    // opcion 5, retirar dinero de la cuenta
                    retirarDinero(cuenta);
                    break;
                case 6:
                    // opcion 6, transferir dinero de la cuenta
                    transferirDinero(cuenta, banco);
                    break;
                case 7:
                    // opcion 7, modificar datos de la cuenta
                    modificarDatosCuenta(cuenta);
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
            serviceCuenta.update(cuenta);
        }
    }

    // opcion 7, modificar datos de la cuenta
    private void modificarDatosCuenta(Cuenta cuenta) {
        clearScreen();
        while (true) {
            // menu para modificar datos de la cuenta
            p_menu.menuCuentaModificacion();
            int opcion = obtenerOpcionValida();
            switch(opcion) {
                case 0:
                    //opcion 0, salir del menu
                    clearScreen();
                    return;
                case 2:
                    //opcion 2, modificar alias
                    modificarAlias(cuenta);
                    break;
                case 1:
                    //opcion 1, cambiar moneda
                    cambiarMoneda(cuenta);
                    break;
                case 3:
                    //opcion 3, cambiar el tipo de cuenta
                    cambiarTipoCuenta(cuenta);
                    break;
                case 4:
                    //opcion 4, dar de baja la cuenta
                    darDeBajaCuenta(cuenta);
                    break;
                case 5:
                    //opcion 5, dar de alta la cuenta
                    darDeAltaCuenta(cuenta);
                    break;
                default:
                    System.out.println("Opcion invalida");
                    break;
            }
            serviceCuenta.update(cuenta);
        }
    }

    // opcion 5, dar de alta la cuenta
    private void darDeAltaCuenta(Cuenta cuenta){
        clearScreen();
        while (true) {
            System.out.println("¿Está seguro que desea dar de alta la cuenta? (s/n)");
            String confirmacion = obtenerCadenaValida();
            scanner.nextLine(); // Consume el salto de línea pendiente
            if ("s".equalsIgnoreCase(confirmacion)) {
                cuenta.setEstaA(true);
                System.out.println("La cuenta ha sido activada exitosamente.");
                break;
            } else if ("n".equalsIgnoreCase(confirmacion)) {
                break;
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese 's' para confirmar o 'n' para cancelar.");
            }
        }
    }

    // opcion 4, dar de baja la cuenta
    private void darDeBajaCuenta(Cuenta cuenta){
        clearScreen();
        while (true) {
            System.out.println("¿Está seguro que desea dar de baja la cuenta? (s/n)");
            String confirmacion = obtenerCadenaValida();
            if ("s".equalsIgnoreCase(confirmacion)) {
                cuenta.setEstaA(false);
                System.out.println("La cuenta ha sido dada de baja exitosamente.");
                break;
            } else if ("n".equalsIgnoreCase(confirmacion)) {
                break;
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese 's' para confirmar o 'n' para cancelar.");
            }
        }
    }

    // opcion 3, cambiar el tipo de cuenta
    private void cambiarTipoCuenta(Cuenta cuenta) {
        clearScreen();
        String tipo = obtenerTipoCuenta();
        cuenta.setTipo_de_cuenta(tipo);
        System.out.println("Tipo de cuenta actualizado con éxito a " + tipo + ".");
    }

    // opcion 1, cambiar moneda
    private void cambiarMoneda(Cuenta cuenta) {
        clearScreen();
        Set<String> monedasValidas = new HashSet<>(Arrays.asList("ARS", "USD", "EUR"));
        while (true) {
            System.out.println("Ingrese la nueva moneda (ARS/USD/EUR):");
            String moneda = obtenerCadenaValida().toUpperCase().trim();
            if (monedasValidas.contains(moneda)) {
                cuenta.setMoneda(moneda);
                System.out.println("Moneda actualizada con éxito a " + moneda + ".");
                break;
            } else {
                System.out.println("Moneda inválida. Por favor, ingrese ARS, USD o EUR.");
            }
        }
    }

    // Opción 2: Cambiar alias
    private void modificarAlias(Cuenta cuenta) {
        clearScreen();
        while (true) {
            System.out.println("Ingrese el nuevo alias:");
            String nuevoAlias = scanner.nextLine().trim();
            if (nuevoAlias.isEmpty()) {
                System.out.println("El alias no puede estar vacío. Intente con otro.");
                continue;
            }
            if (!cuenta.buscarAlias(nuevoAlias)) {
                cuenta.setAlias(nuevoAlias);
                System.out.println("Alias actualizado con éxito.");
                break;
            } else {
                System.out.println("El alias ya está ocupado. Intente con otro.");
            }
        }
    }

    // opcion 6, transferir dinero de la cuenta
    private void transferirDinero(Cuenta cuenta, Banco banco){
        clearScreen();
        while (true) {
            System.out.println("Ingrese 1 para alias o 2 para cbu/cvu del cliente a transferir dinero, '0' para salir:");
            int tipoTransferencia = obtenerOpcionValida();
            scanner.nextLine(); // Consume el salto de línea pendiente

            if (tipoTransferencia == 0) return; // Salir

            String alias = null;
            Long cbu_cvu = null;
            if (tipoTransferencia == 1) {
                System.out.println("Ingrese el alias:");
                alias = scanner.nextLine().trim();
            } else if (tipoTransferencia == 2) {
                System.out.println("Ingrese el cbu/cvu:");
                cbu_cvu = obtenerOpcionValidaLong();
                scanner.nextLine(); // Consume el salto de línea pendiente
            }

            boolean vf = true;
            while (vf) {
                System.out.println("Ingrese el monto a transferir, '0' para salir:");
                double monto = obtenerOpcionValidaDouble();
                scanner.nextLine(); // Consume el salto de línea pendiente

                if (monto == 0) return; // Salir
                try {
                    vf = serviceCuenta.transferencia(cuenta, monto, alias, cbu_cvu, banco);
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    // opcion 5, retirar dinero de la cuenta
    private void retirarDinero(Cuenta cuenta){
        clearScreen();
        boolean vf = true;
        while (vf) {
            System.out.println("Ingrese el monto a retirar, '0' para salir:");
            double monto = obtenerOpcionValidaDouble();

            if (monto == 0) return; // salir
            try {
                vf = serviceCuenta.retirar(cuenta, monto);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // opcion 4, ingresar dinero a la cuenta
    private void ingresarDinero(Cuenta cuenta){
        clearScreen();
        boolean VF = true;
        while (VF) {
            System.out.println("Ingrese el monto a ingresar, '0' para salir:");
            double monto = obtenerOpcionValidaDouble();

            if (monto == 0) return;
            try {
                VF = serviceCuenta.depositar(cuenta, monto);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}