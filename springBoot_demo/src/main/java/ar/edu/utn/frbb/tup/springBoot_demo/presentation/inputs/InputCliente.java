package ar.edu.utn.frbb.tup.springBoot_demo.presentation.inputs;

import java.time.LocalDate;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.springBoot_demo.model.Banco;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cliente;
import ar.edu.utn.frbb.tup.springBoot_demo.model.Cuenta;
import ar.edu.utn.frbb.tup.springBoot_demo.presentation.shows.PrintsMenues;
import ar.edu.utn.frbb.tup.springBoot_demo.service.ServiceCliente;

@Component
public class InputCliente extends BaseInputProcessor {
    @Autowired
    PrintsMenues p_menu;
    @Autowired
    InputCuenta inputCuenta;
    @Autowired
    ServiceCliente serviceCliente;

    // metodo para acceder al menu del cliente
    public void menuSesionCliente(Cliente cliente, Banco banco){
        while (true) {
            p_menu.menuSesionCliente();
            int opcion = obtenerOpcionValida();
            switch (opcion) {
                case 0:
                    //opcion 0, salir del menu
                    return;
                case 1:
                    // opcion 1, ver datos del cliente
                    clearScreen();
                    cliente.getDatosCliente();
                    break;
                case 2:
                    // opcion 2, ver cuentas del cliente
                    clearScreen();
                    System.out.println("Cuentas del cliente");
                    cliente.mostrarCuentas();
                    break;
                case 3:
                    // opcion 3 , operar con una cuenta
                    inputCuenta.operarConCuenta(cliente, banco);
                    break;
                case 4:
                    // mostrar a que banco pertece el cliente
                    System.out.println("El cliente pertenece al banco: \n" + cliente.getBanco());
                    break;
                case 5:
                    // mostrar la fecha de alta
                    System.out.println("Fecha de alta del cliente: " + cliente.getFecha_de_alta());
                    break;
                case 6:
                    // modificar datos del cliente
                    modificarCliente(cliente, banco);
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    break;
            }
        }
    }
    // modificar datos del cliente
    public void modificarCliente(Cliente cliente , Banco banco){
        clearScreen();
        while (true) {
            try {
                p_menu.menuClienteModificacion();
                int opcion = obtenerOpcionValida();
                switch(opcion) {
                    case 0:
                        //opcion 0, salir del menu
                        clearScreen();
                        return;
                    case 1:
                        // Modificar banco
                        cambiarBanco(cliente);
                        break;
                    case 2:
                        // agregar cuenta
                        inputCuenta.crearCuenta(cliente, banco);
                        break;
                    case 3:
                        // eliminar cuenta, solo le vamos a dar de baja
                        eliminarCuenta(cliente);
                        break;
                    case 4:
                        // modificar nombre y apellido
                        modificarNombreApellido(cliente);
                        break;
                    case 5:
                        // modificar fecha de nacimiento
                        modificarFechaNacimiento(cliente);
                        break;
                    case 6:
                        // modificar direccion
                        modificarDireccion(cliente);
                        break;
                    case 7:
                        // modificar telefono
                        modificarTelefono(cliente);
                        break;
                    case 8:
                        // modificar email
                        modificarEmail(cliente);
                        break;
                    case 9:
                        // modificar dni
                        modificarDni(cliente);
                        break;
                    default:
                        System.out.println("Opcion invalida");
                        break;
                }
                serviceCliente.save(cliente);
            } catch (Exception e) {
                System.out.println("Error al mostrar el menu: " + e.getMessage());
            }
        }
    }

    // modificar dni
    private void modificarDni(Cliente cliente) {
        clearScreen();
        while (true) {
            System.out.println("Ingrese su DNI:");
            Long dni = obtenerOpcionValidaLong();
            if (String.valueOf(dni).length() != 8) {
                System.out.println("Ingrese un DNI valido.");
                continue;
            }
            if (serviceCliente.buscarClientePorDni(dni) != null) {
                System.out.println("El DNI ingresado ya esta asocioado a una cuenta.");
                System.out.println("Quieres asociar otro DNI? si/no");
                if (scanner.nextLine().equalsIgnoreCase("no")) {
                    break;
                }
            }else{
                cliente.setDni(dni);
                break;
            }
        }
    }

    // modificar email
    private void modificarEmail(Cliente cliente){
        clearScreen();
        while (true) {
            System.out.println("Ingrese su email:");
            String email = scanner.nextLine().trim();
            if (serviceCliente.validarEmail(email)) {
                cliente.setEmail(email);
                break;
            }
            else {
                System.out.println("Error: El email no es válido");
            }
        }
    }

    // modificar telefono
    private void modificarTelefono(Cliente cliente){
        clearScreen();
        while (true) {
            System.out.println("Ingrese un número de telefono:");
            Long numeroT = obtenerOpcionValidaLong();
            if (String.valueOf(numeroT).length() == 10 && numeroT > 0) {
                cliente.setTelefono(numeroT);
                break;
            }else{
                System.out.println("Error: El número de teléfono debe tener 10 dígitos y ser positivo");
            }
        }
    }

    // modificar direccion
    private void modificarDireccion(Cliente cliente){
        clearScreen();
        while (true) {
            System.out.println("Ingrese la direccion: ");
            String direccion = scanner.nextLine().trim();
            if (!direccion.equals("")) {
                cliente.setDireccion(direccion);
                break;
            }
            else {
                System.out.println("La direccion no puede ser vacia");
            }
        }
    }

    // modificar fecha de nacimiento
    private void modificarFechaNacimiento(Cliente cliente){
        clearScreen();
        String fecha = "";
        do {
            System.out.println("Ingrese su fecha de nacimiento (aaaa-mm-dd):");
            fecha = scanner.nextLine().trim();
            if (!serviceCliente.validarFechaNacimiento(fecha)) {
                System.out.println("Error: La fecha no es válida o está en el futuro.");
            }
        } while (!serviceCliente.validarFechaNacimiento(fecha));

        cliente.setFecha_de_nacimiento(LocalDate.parse(fecha));
    }

    // modificae nombre y apellido
    private void modificarNombreApellido(Cliente cliente){
        clearScreen();
        while (true) {
            System.out.println("Ingrese nombre y apellido: ");
            String nombre_apellido = scanner.nextLine().trim();
            if (!nombre_apellido.equals("")) {
                cliente.setNombre_y_apellido(nombre_apellido);
                break;
            } else {
                System.out.println("El nombre y apellido no puede ser vacio");
            }
        }
    }

    // eliminar cuenta, solo le vamos a dar de baja
    private void eliminarCuenta(Cliente cliente){
        clearScreen();
        scanner.nextLine(); // lipio el búffer
        while (true) {
            try {
                System.out.println("Ingrese el id de la cuenta que desea dar de baja, '0' para salir: ");
                String id_cuenta = scanner.nextLine().trim();
                if (id_cuenta.equals("0")) return;
                // Buscar la cuenta
                Cuenta cuenta = cliente.buscarCuentaPorId(id_cuenta);
                if (cuenta != null) {
                    // Confirmación adicional antes de dar de baja la cuenta
                    do {
                        System.out.println("¿Está seguro que desea dar de baja la cuenta con id " + id_cuenta + "? (s/n)");
                        String respuesta = obtenerCadenaValida().trim();
                        if (respuesta.equalsIgnoreCase("n")) {
                            System.out.println("Operación cancelada.");
                            return;
                        }else if (!respuesta.equalsIgnoreCase("s")) {
                            System.out.println("debe ingresar (s/n)");
                        }else{
                            // Dar de baja la cuenta
                            System.out.println("Cuenta dada de baja exitosamente.");
                            cuenta.setEstaA(false);
                            break;
                        }
                    } while (true);
                }
            } catch (AccountNotFoundException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    // cambiar banco
    private void cambiarBanco(Cliente cliente){
        clearScreen();
        String nombre_del_banco = "";
        do {
            System.out.println("Ingrese el nombre del banco al que pertenece (nacion o provincia):");
            nombre_del_banco = obtenerCadenaValida();
            if (!serviceCliente.nombre_del_banco(nombre_del_banco)) {
                System.out.println("El nombre del banco no es valido.");
            }
        } while (!serviceCliente.nombre_del_banco(nombre_del_banco));

        cliente.setBanco(nombre_del_banco);
    }

    // metodo para registrar un nuevo cliente
    public void registrarCliente(Banco banco){
        clearScreen();
        Cliente cliente = new Cliente();
        // ingresar dni
        do {
            System.out.println("Ingrese su DNI:");
            Long dni = obtenerOpcionValidaLong();
            scanner.nextLine(); // Consume la nueva línea restante

            if (dni < 0 || String.valueOf(dni).length() != 8) {
                System.out.println("Un dni debe tener 8 digitos y ser un numero entero positivo");
                continue;
            }else if (String.valueOf(dni).length() == 8) {
                if (banco.buscarClientePorDni(dni) != null) {
                    System.out.println("El DNI ingresado ya está asociado a una cuenta.");
                    do {
                        System.out.println("¿Quieres asociar otro DNI? (s/n)");
                        String respuesta = obtenerCadenaValida().trim();
                        if (respuesta.equalsIgnoreCase("n")) {
                            return;
                        }else if (!respuesta.equalsIgnoreCase("s")) {
                            System.out.println("debe ingresar (s/n)");
                        }else{
                            break;
                        }
                    } while (true);
                } else {
                    cliente.setDni(dni);
                    break;
                }
            }
        } while (true);
        // ingresar banco
        cambiarBanco(cliente);
        // ingresar nombre y apellido
        modificarNombreApellido(cliente);
        // ingresar fecha actual
        cliente.setFecha_de_alta(LocalDate.now());
        // ingresar direccion
        modificarDireccion(cliente);
        // ingresar numero de telefono
        modificarTelefono(cliente);
        // ingresar email
        modificarEmail(cliente);
        // ingresar fecha de nacimiento
        modificarFechaNacimiento(cliente);
        // registrar cliente
        banco.setClientes(cliente);
    }
}