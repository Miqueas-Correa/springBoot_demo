# RUTAS:

## - Cliente:
  * /cliente
  * /cliente/{dni}
  * Cuenta con:
    
     - GET: (dni "opcional")
        {
        "dni": 12345678,
        "nombre_y_apellido": "Juan Perez",
        "telefono": 1234567890,
        "email": "juan.perez@example.com",
        "fecha_de_nacimiento": "1990-05-15",
        "banco": "nacion",
        "fecha_de_alta": "2024-01-01",
        "edad": 34
        }
       
     - POST:
        DISPONIBLE PARA PRUEBAS
        {
        "dni": 12345600,
        "nombre_y_apellido": "Test2025",
        "telefono": 1234567890,
        "email": "juan.perez@example.com",
        "fechaNacimiento": "1990-05-15"
        }
       
     - PUT: (dni obligatorio)
        DISPONIBLE PARA PRUEBAS
        {
        "dni": 12345679,
        "nombre_y_apellido": "Juan Perez Test2025 edit",
        "telefono": 1234567890,
        "email": "juan.perez@example.com",
        "fechaNacimiento": "1990-05-15"
        }
       
     - DELETE: (dni "obligatorio") "dni": 12345600, primero crear y luego borrar.

## - Cuenta:
  * /cuenta
  * /cuenta/{numeroCuenta}
  * /cuenta/depositar/{numeroCuenta}
     - 100.75
  * /cuenta/retirar/{numeroCuenta}
     - 100.75
  * Cuenta con:
     - GET: (numero de cuenta "opcional")
        {
        "numeroCuenta": 123456790,
        "estaA": true,
        "titular": 12247172,
        "saldo": 500500.75,
        "tipoCuenta": "a",
        "moneda": "dolares",
        "fechaCreacion": "2024-12-12"
        }
     - POST:
        {
        "numeroCuenta": 987654333,
        "estaA": true,
        "titular": 12247172,
        "saldo": 500500.75,
        "tipoCuenta": "c",
        "moneda": "dolares"
        }
     - PUT:  (numeroCuenta "obligatorio")
        {
        "estaA": true,
        "titular": "12247172",
        "saldo": 500500.75,
        "tipoCuenta": "c",
        "moneda": "dolares"
        }
     - DELETE: (numeroCuenta "obligatorio"), solo le da de baja la cuenta
   
## - Transferir:
  * /cuenta/transferir/{numeroCuenta}
     - {
       "cuentaOrigen": 123456791,
       "cuentaDestino": 123456790,
       "monto": 5000.0,
       "moneda": "dolares"
       }

## - Movimientos:
  * /cuenta/movimientos
  * /cuenta/movimientos/{numeroCuenta}
  * Cuenta con: 
     - GET: (numero de cuenta "opcional") trae todos los movimmientos de esa cuenta.
        {
        "id": 1,
        "descripcion": "Compra en supermercado",
        "fecha_y_hs": "2024-12-05",
        "numeroCuenta": 123456789
        }
     - POST:
        {
        "descripcion": "Creacion de prueba",
        "numeroCuenta": 1
        }
