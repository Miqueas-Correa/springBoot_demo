# RUTAS:

- Cliente:
  * /cliente
  * /cliente/{dni}
  * Cuenta con:
     - GET: (dni "opcional")
     - POST:
        
     - PUT:
     - DELETE: (dni "obligatorio")

- Cuenta:
  * /cuenta
  * /cuenta/{numeroCuenta}
  * /cuenta/transferir/{numeroCuenta}
     - {
       "cuentaOrigen": 123456791,
       "cuentaDestino": 123456790,
       "monto": 5000.0,
       "moneda": "dolares"
       }
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
     - PUT:
        {
        "estaA": true,
        "titular": "12247172",
        "saldo": 500500.75,
        "tipoCuenta": "c",
        "moneda": "dolares"
        }
     - DELETE: (dni "obligatorio"), solo le da de baja la cuenta

- Movimientos:
  * /cuenta/movimientos
  * /cuenta/movimientos/{id}
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
     - PUT:
        {
        "estaA": true,
        "titular": "12247172",
        "saldo": 500500.75,
        "tipoCuenta": "c",
        "moneda": "dolares"
        }
     - DELETE: (dni "obligatorio"), solo le da de baja la cuenta
