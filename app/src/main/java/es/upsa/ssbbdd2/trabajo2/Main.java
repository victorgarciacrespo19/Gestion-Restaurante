package es.upsa.ssbbdd2.trabajo2;

import es.upsa.ssbbdd2.trabajo2.application.Dao;
import es.upsa.ssbbdd2.trabajo2.application.UseCases;
import es.upsa.ssbbdd2.trabajo2.application.impl.DaoImpl;
import es.upsa.ssbbdd2.trabajo2.application.impl.UseCasesImpl;
import es.upsa.ssbbdd2.trabajo2.domain.entities.*;


import java.time.LocalDate;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        try (Dao dao = new DaoImpl("jdbc:postgresql://localhost:5432/upsa", "system", "manager"))
        {
            UseCases useCases = UseCasesImpl.builder()
                    .withDao(dao)
                    .build();



            useCases.registrarPlato("Sopa de Verduras", "Sopa con variedad de verduras frescas", 10.99, Tipo.ENTRANTE,
                    List.of(
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Zanahoria").build())
                                    .withCantidad(100)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Apio").build())
                                    .withCantidad(50)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Patata").build())
                                    .withCantidad(100)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build()
                    ));

            useCases.registrarPlato("Pollo al Curry", "Pollo cocido con especias curry", 18.99, Tipo.PRINCIPAL,
                    List.of(
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Pollo").build())
                                    .withCantidad(250)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Curry").build())
                                    .withCantidad(20)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build()
                    ));

            useCases.registrarPlato("Helado de Vainilla", "Helado cremoso con sabor a vainilla natural", 6.99, Tipo.POSTRE,
                    List.of(
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Leche").build())
                                    .withCantidad(200)
                                    .withUnidadMedida(Unidad.CENTILITROS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Vainilla").build())
                                    .withCantidad(5)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build()
                    ));

            useCases.registrarPlato("Arroz con Pollo", "Clásico arroz con pollo y vegetales", 15.49, Tipo.PRINCIPAL,
                    List.of(
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Arroz").build())
                                    .withCantidad(150)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Pollo").build())
                                    .withCantidad(200)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build()
                    ));
            useCases.registrarPlato("Tarta de Chocolate", "Tarta con cobertura de chocolate", 11.99, Tipo.POSTRE,
                    List.of(
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Chocolate").build())
                                    .withCantidad(300)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Harina").build())
                                    .withCantidad(150)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build()
                    ));

            useCases.registrarPlato("Ensalada Mixta", "Ensalada variada con aliño", 8.99, Tipo.ENTRANTE,
                    List.of(
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Lechuga").build())
                                    .withCantidad(100)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Tomate").build())
                                    .withCantidad(50)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build()
                    ));

            useCases.registrarPlato("Pizza Margarita", "Pizza clásica con queso y tomate", 14.99, Tipo.PRINCIPAL,
                    List.of(
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Queso Mozzarella").build())
                                    .withCantidad(200)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Salsa de Tomate").build())
                                    .withCantidad(100)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build()
                    ));

            useCases.registrarPlato("Filete de Pavo", "Filete de pavo con patatas", 21.99, Tipo.INFANTIL,
                    List.of(
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Carne de Res").build())
                                    .withCantidad(250)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build(),
                            CantidadIngrediente.builder()
                                    .withIngrediente(Ingrediente.builder().withNombre("Patata").build())
                                    .withCantidad(150)
                                    .withUnidadMedida(Unidad.GRAMOS)
                                    .build()
                    ));

            // Registrar nuevos menús
            useCases.registrarMenu("Menú de Invierno",
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 2, 28),
                    List.of("Sopa de Verduras", "Pollo al Curry", "Helado de Vainilla"));

            useCases.registrarMenu("Menú Clásico",
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 2, 28),
                    List.of("Ensalada Mixta", "Pizza Margarita", "Tarta de Chocolate"));

            useCases.registrarMenu("Menú del dia",
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 2, 28),
                    List.of("Filete de Pavo", "Arroz con Pollo", "Tarta de Chocolate"));

            List<Menu> menus = useCases.buscarMenu(LocalDate.of(2025, 1, 11));

            List<Tipo> ordenTipos = List.of(Tipo.ENTRANTE, Tipo.PRINCIPAL, Tipo.POSTRE, Tipo.INFANTIL);

            for (Menu menu : menus) {
                System.out.println("\n\nMenú: " + menu.getNombre());
                System.out.println("Precio: " + menu.getPrecio());
                System.out.println("Platos:");
                for (Tipo tipo : ordenTipos) {
                    if (menu.getPlatos().containsKey(tipo)) {
                        System.out.println("  Tipo: " + tipo.name());
                        List<Plato> platos = menu.getPlatos().get(tipo);
                        for (Plato plato : platos) {
                            System.out.println("    - " + plato.getNombre());
                        }
                    }
                }
            }




            System.out.println("---------------------------");
            List<String> sinIngrediente = List.of("Tomate");
            List<Plato> platos = useCases.buscarPlato(Tipo.PRINCIPAL, sinIngrediente);
            System.out.println("\n\nPlatos tipo: "+ Tipo.PRINCIPAL+ ", sin:" + sinIngrediente);

            for (Plato platosMostrar : platos)
            {

                System.out.println(platosMostrar.getNombre());
                System.out.println("    "+ platosMostrar.getDescripcion());
                System.out.println("    "+ platosMostrar.getPrecio());
            }
            System.out.println("---------------------------");
            useCases.subirPrecioPlato("Filete de Pavo", 0.5);

        }

    }
}
