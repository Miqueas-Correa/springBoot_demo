package ar.edu.utn.frbb.tup.springBoot_demo.presentation.shows;

public class PrintsMenues {
    // Cliente
    // menu de inicio del banco
    public void menuInicio() {
        System.out.println("---------------------------------");
        System.out.println("Bienvenido al banco");
        System.out.println("1. iniciar sesión");
        System.out.println("2. registrarse");
        System.out.println("---------------------------------");
        System.out.println("0. cerrar programa");
        System.out.println("---------------------------------");
    }

    public void menuSesionCliente() {
        System.out.println("---------------------------------");
        System.out.println("CLIENTE");
        System.out.println("1. ver mis datos personales");
        System.out.println("2. ver mis cuentas");
        System.out.println("3. ir a menu de cuentas");
        System.out.println("4. mostrar a que banco pertenece");
        System.out.println("5. mostrar la fecha de alta");
        System.out.println("---------------------------------");
        System.out.println("6. modificar datos del cliente");
        System.out.println("---------------------------------");
        System.out.println("0. cerrar sesión");
        System.out.println("---------------------------------");
    }

    public void menuClienteModificacion(){
        System.out.println("---------------------------------");
        System.out.println("1. cambiar banco");
        System.out.println("2. agregar cuenta");
        System.out.println("3. eliminar cuenta");
        System.out.println("4. modificar nombre y apellido");
        System.out.println("5. modificar fecha de nacimiento");
        System.out.println("6. modificar domicilio");
        System.out.println("7. modificar número de teléfono");
        System.out.println("8. modificar correo electrónico");
        System.out.println("9. modificar DNI");
        System.out.println("---------------------------------");
        System.out.println("0. volver al menú anterior");
        System.out.println("---------------------------------");
    }

    // Cuenta
    public void menuCuentaInicio() {
        System.out.println("---------------------------------");
        System.out.println("Bienvenido al banco");
        System.out.println("CUENTA");
        System.out.println("1. iniciar sesión");
        System.out.println("2. crear cuenta");
        System.out.println("---------------------------------");
        System.out.println("0. volver al menú anterior");
        System.out.println("---------------------------------");
    }

    public void menuSesionCuenta() {
        System.out.println("---------------------------------");
        System.out.println("CUENTA");
        System.out.println("1. ver mis datos de la cuenta");
        System.out.println("2. ver saldo");
        System.out.println("3. ver mis movimientos");
        System.out.println("---------------------------------");
        System.out.println("4. hacer un deposito");
        System.out.println("5. hacer un retiro");
        System.out.println("6. hacer una transferencia");
        System.out.println("---------------------------------");
        System.out.println("7. modificar datos de la cuenta");
        System.out.println("---------------------------------");
        System.out.println("0. cerrar sesión");
        System.out.println("---------------------------------");
    }

    public void menuCuentaModificacion() {
        System.out.println("---------------------------------");
        System.out.println("1. cambiar de moneda");
        System.out.println("2. cambiar el alias");
        System.out.println("3. cambiar el tipo de cuenta");
        System.out.println("4. dar de baja la cuenta");
        System.out.println("5. dar de alta la cuenta");
        System.out.println("---------------------------------");
        System.out.println("0. volver al menu anterior");
        System.out.println("---------------------------------");
    }
}