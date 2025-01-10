// package ar.edu.utn.frbb.tup.springBoot_demo.service;

// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;
// import ar.edu.utn.frbb.tup.springBoot_demo.persistence.DaoMovimientos;
// import ar.edu.utn.frbb.tup.springBoot_demo.model.Movimientos;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import java.util.ArrayList;
// import java.util.List;

// @ExtendWith(MockitoExtension.class)
// public class ServiceMovimientosTest {

//     @Mock
//     private DaoMovimientos daoMovimientos;

//     private ServiceMovimientos serviceMovimientos;

//     @BeforeEach
//     void setUp() {
//         serviceMovimientos = new ServiceMovimientos(daoMovimientos);
//     }

//     @Test
//     void save_ValidMovimiento_ShouldCallDaoSave() {
//         MovimientosDto movimientoDto = new MovimientosDto();
//         serviceMovimientos.save(movimientoDto);
//         verify(daoMovimientos).save(movimientoDto);
//     }

//     @Test
//     void save_NullMovimiento_ShouldThrowException() {
//         assertThrows(IllegalArgumentException.class, () -> {
//             serviceMovimientos.save(null);
//         });
//     }

//     @Test
//     void darMovimientos_ShouldReturnListFromDao() {
//         List<Movimientos> expectedMovimientos = new ArrayList<>();
//         expectedMovimientos.add(new Movimientos());
//         when(daoMovimientos.darMovimientos()).thenReturn(expectedMovimientos);

//         List<Movimientos> result = serviceMovimientos.darMovimientos();
        
//         assertEquals(expectedMovimientos, result);
//         verify(daoMovimientos).darMovimientos();
//     }

//     @Test
//     void buscar_ValidNumeroCuenta_ShouldReturnFilteredList() {
//         Long numeroCuenta = 123456L;
//         List<Movimientos> expectedMovimientos = new ArrayList<>();
//         when(daoMovimientos.buscar(numeroCuenta)).thenReturn(expectedMovimientos);

//         List<Movimientos> result = serviceMovimientos.buscar(numeroCuenta);

//         assertEquals(expectedMovimientos, result);
//         verify(daoMovimientos).buscar(numeroCuenta);
//     }

//     @Test
//     void darMovimientos_EmptyList_ShouldReturnEmptyList() {
//         when(daoMovimientos.darMovimientos()).thenReturn(new ArrayList<>());
        
//         List<Movimientos> result = serviceMovimientos.darMovimientos();
        
//         assertTrue(result.isEmpty());
//         verify(daoMovimientos).darMovimientos();
//     }
// }
