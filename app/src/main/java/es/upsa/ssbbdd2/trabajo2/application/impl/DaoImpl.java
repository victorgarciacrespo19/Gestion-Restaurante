package es.upsa.ssbbdd2.trabajo2.application.impl;

import es.upsa.ssbbdd2.trabajo2.application.Dao;
import es.upsa.ssbbdd2.trabajo2.domain.entities.*;
import es.upsa.ssbbdd2.trabajo2.domain.exceptions.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaoImpl implements Dao {
    private Connection connection;

    public DaoImpl(String url, String user, String password) throws RestauranteException
    {
        try {
            Driver driver = new org.postgresql.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException sqlException) {
            throw manageException(sqlException);
        }
    }

    @Override
    public void close() throws Exception
    {
        if (connection != null)
        {
            connection.close();
            connection = null;
        }
    }

    private RestauranteException manageException(SQLException sqlException)
    {
        String message = sqlException.getMessage();
        // Menús
        if (message.contains("NN_MENUS.NOMBRE")) return new MenuNombreRequiredException();
        if (message.contains("NN_MENUS.DESDE")) return new MenuDesdeRequiredException();
        if (message.contains("NN_MENUS.HASTA")) return new MenuHastaRequiredException();
        // Platos
        if (message.contains("NN_PLATOS.NOMBRE")) return new PlatoNombreRequiredException();
        if (message.contains("NN_PLATOS.DESCRIPCION")) return new PlatoDescripcionRequiredException();
        if (message.contains("NN_PLATOS.TIPO")) return new PlatoTipoRequiredException();
        if (message.contains("CH_PLATOS.TIPO")) return new PlatoTipoInvalidException();
        if(message.contains("UK_PLATOS.NOMBRE")) return new PlatoNombreRepeatedException();
        // Ingredientes
        if (message.contains("UK_INGREDIENTES.NOMBRE")) return new IngredienteNombreRepeatedException();
        if (message.contains("NN_INGREDIENTES.NOMBRE")) return new IngredienteNombreRequiredException();
        // Menú-Platos
        if (message.contains("FK_MENU_PLATO_MENU")) return new MenuNotFoundException();
        if (message.contains("FK_MENU_PLATO_PLATO")) return new PlatoNotFoundException();
        // Cantidad-Ingredientes
        if (message.contains("FK_CANTIDAD_INGREDIENTES_PLATO")) return new PlatoNotFoundException();
        if (message.contains("FK_CANTIDAD_INGREDIENTES_INGREDIENTES")) return new IngredienteNotFoundException();
        if (message.contains("CH_cantidad.UNIDAD")) return new UnidadInvalidException();

        return new NonControlledSqlException(sqlException);
    }

    private void buscarYRegistrarIngredientes(List<Ingrediente> ingredientes) throws RestauranteException {
        final String SQL_BUSCAR = """
                SELECT nombre 
                FROM ingredientes 
                WHERE nombre = ?
                """;

        final String SQL_INSERTAR = """
                INSERT INTO ingredientes(id                         ,nombre)
                                 VALUES (nextval('seq_ingredientes'), ?    )
                """;

        try (PreparedStatement buscarIngrediente = connection.prepareStatement(SQL_BUSCAR);
             PreparedStatement insertarIngrediente = connection.prepareStatement(SQL_INSERTAR)) {

            for (Ingrediente ingrediente : ingredientes) {
                String nombreIngrediente = ingrediente.getNombre();

                // Verificar si el ingrediente ya existe
                buscarIngrediente.setString(1, nombreIngrediente);
                try (ResultSet resultSet = buscarIngrediente.executeQuery()) {
                    if (!resultSet.next()) {
                        // Si no existe, insertarlo
                        insertarIngrediente.setString(1, nombreIngrediente);
                        insertarIngrediente.executeUpdate();
                        System.out.println("Ingrediente: " + nombreIngrediente +" añadido a la tabla Ingredientes");
                    }
                }
            }
        } catch (SQLException sqlException) {
            throw manageException(sqlException);
        }
    }

    @Override
    public Plato insertarPlato(String nombre, String descripcion, double precio, Tipo tipo, List<CantidadIngrediente> cantidadesIngredientes) throws RestauranteException
    {
        final String SQL_PLATO = """
                                  INSERT INTO platos(id                   ,nombre, descripcion, precio, tipo)
                                             VALUES (nextval('seq_platos'),  ?   ,     ?      ,   ?   ,   ? )
                                 """;

        final String SQL_RELACION = """
                                    INSERT INTO cantidad_ingredientes(id_plato, id_ingrediente, cantidad, unidad)
                                    SELECT ?, i.id, ?, ?
                                    FROM ingredientes i
                                    WHERE i.nombre = ?
                                    """;

        String[] fields = {"id"};

        try (PreparedStatement preparedStatementPlato = connection.prepareStatement(SQL_PLATO, fields))
        {
            // plato
            preparedStatementPlato.setString(1, nombre);
            preparedStatementPlato.setString(2, descripcion);
            preparedStatementPlato.setDouble(3, precio);
            preparedStatementPlato.setString(4, tipo.toString());
            preparedStatementPlato.executeUpdate();


            try (ResultSet generatedKeys = preparedStatementPlato.getGeneratedKeys())
            {
                if (!generatedKeys.next())
                {
                    throw new RestauranteException("No se pudo generar el ID del plato.");
                }
                String idPlato = generatedKeys.getString(1);

                List<Ingrediente> ingredientes = new ArrayList<>();
                for (CantidadIngrediente cantidadIngrediente : cantidadesIngredientes)
                {
                    ingredientes.add(cantidadIngrediente.getIngrediente());
                }

                // busca los ingredientes, y los que no estan creados los crea
                buscarYRegistrarIngredientes(ingredientes);

                // añadir al ingrediente-plato
                try (PreparedStatement preparedStatementRelacion = connection.prepareStatement(SQL_RELACION))
                {
                    for (CantidadIngrediente cantidadIngrediente : cantidadesIngredientes)
                    {
                        preparedStatementRelacion.setString(1, idPlato);
                        preparedStatementRelacion.setInt(2, cantidadIngrediente.getCantidad());
                        preparedStatementRelacion.setString(3, cantidadIngrediente.getUnidadMedida().toString());
                        preparedStatementRelacion.setString(4, cantidadIngrediente.getIngrediente().getNombre());
                        preparedStatementRelacion.executeUpdate();
                    }
                }
                System.out.println("Plato:" +nombre+" añadido a la tabla Platos");
                return Plato.builder()
                            .withId(idPlato)
                            .withNombre(nombre)
                            .withDescripcion(descripcion)
                            .withPrecio(precio)
                            .withTipo(tipo)
                            .withCantidadesIngredientes(cantidadesIngredientes)
                            .build();


            }
        } catch (SQLException sqlException) {
            throw manageException(sqlException);
        }
    }

    private Plato buscarPlatoPorNombre(String nombrePlato) throws RestauranteException
    {
        final String SQL_VALIDAR_PLATOS = """
                                           SELECT id, nombre, descripcion, precio, tipo
                                           FROM platos
                                           WHERE nombre = ?
                                          """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_VALIDAR_PLATOS))
        {
            preparedStatement.setString(1, nombrePlato);
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (!resultSet.next()) {
                    throw new PlatoNotFoundException();
                }

                // Construir y devolver el objeto Plato
                return Plato.builder()
                            .withId(resultSet.getString("id"))
                            .withNombre(resultSet.getString("nombre"))
                            .withDescripcion(resultSet.getString("descripcion"))
                            .withPrecio(resultSet.getDouble("precio"))
                            .withTipo(Tipo.valueOf(resultSet.getString("tipo")))
                            .build();
            }
        } catch (SQLException sqlException) {
            throw manageException(sqlException);
        }
    }

    @Override
    public Menu insertarMenu(String nombre, LocalDate desde, LocalDate hasta, List<String> platos) throws RestauranteException
    {
        final String SQL_INSERTAR_MENU = """
                INSERT INTO menus(id                  , nombre, precio, desde, hasta)
                          VALUES (nextval('seq_menus'),   ?   ,    ?  ,   ?  ,   ?  )
                """;
        final String SQL_RELACION_MENU_PLATO = """
                INSERT INTO menu_platos(id_menu, id_plato)
                                VALUES (   ?   ,    ?    )
                """;

        String[] fields = {"id"};
        Map<Tipo, List<Plato>> mapaPlatos = new HashMap<>();
        double precioTotal = 0.0;

        try (PreparedStatement insertarMenu = connection.prepareStatement(SQL_INSERTAR_MENU, fields);
             PreparedStatement insertarRelacion = connection.prepareStatement(SQL_RELACION_MENU_PLATO))
        {

            // Validar platos y obtener info
            List<Plato> listaPlatos = new ArrayList<>();
            for (String platoNombre : platos) {
                Plato plato = buscarPlatoPorNombre(platoNombre);//busca plato
                listaPlatos.add(plato);
                // Agrupar por tipo
                mapaPlatos.computeIfAbsent(plato.getTipo(), k -> new ArrayList<>()).add(plato);

                precioTotal += plato.getPrecio();
            }

            double precioDescontado = precioTotal * 0.85;

            // Insertar el menú
            insertarMenu.setString(1, nombre);
            insertarMenu.setDouble(2, precioDescontado);
            insertarMenu.setDate(3, Date.valueOf(desde));
            insertarMenu.setDate(4, Date.valueOf(hasta));
            insertarMenu.executeUpdate();


            try (ResultSet generatedKeys = insertarMenu.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new RestauranteException("No se pudo generar el ID del menú.");
                }
                String idMenu = generatedKeys.getString(1);

                // insertar en la tabla de relacion de menu-plato
                for (Plato plato : listaPlatos)
                {
                    insertarRelacion.setString(1, idMenu);
                    insertarRelacion.setString(2, plato.getId());
                    insertarRelacion.executeUpdate();
                }

                System.out.println("Menu: "+ nombre+" añadido a la tabla Menus");
                return Menu.builder()
                           .withId(idMenu)
                           .withNombre(nombre)
                           .withPrecio(precioDescontado)
                           .withDesde(desde)
                           .withHasta(hasta)
                           .withPlatos(mapaPlatos)
                           .build();
            }

        } catch (SQLException sqlException) {
            throw manageException(sqlException);
        }
    }

    @Override
    public List<Menu> buscarMenu(LocalDate fecha) throws RestauranteException
    {
        final String SQL = """
                             SELECT m.id AS menu_id, m.nombre AS menu_nombre, m.precio AS menu_precio,
                                    p.nombre AS plato_nombre, p.tipo AS plato_tipo
                             FROM menus m
                             JOIN menu_platos mp ON m.id = mp.id_menu
                             JOIN platos p ON mp.id_plato = p.id
                             WHERE m.desde <= ? AND m.hasta >= ?
                             ORDER BY m.nombre, p.tipo ;
                           """;

        List<Menu> menus = new ArrayList<>();
        boolean hayNext;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setDate(1, Date.valueOf(fecha));
            preparedStatement.setDate(2, Date.valueOf(fecha));

            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                hayNext = resultSet.next();
                while (hayNext)
                {
                    // Obtener información básica del menú
                    String menuId = resultSet.getString("menu_id");
                    String menuNombre = resultSet.getString("menu_nombre");
                    double menuPrecio = resultSet.getDouble("menu_precio");

                    // Agrupar los platos del menú por tipo
                    Map<Tipo, List<Plato>> platosPorTipo = new HashMap<>();

                    do{
                        // Crear un objeto Plato solo con nombre y tipo
                        String platoNombre = resultSet.getString("plato_nombre");
                        Tipo tipo = Tipo.valueOf(resultSet.getString("plato_tipo"));

                        Plato plato = Plato.builder()
                                           .withNombre(platoNombre)
                                           .withTipo(tipo)
                                           .build();

                        platosPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(plato);

                        hayNext = resultSet.next();
                    } while (hayNext && menuId.equals(resultSet.getString("menu_id")));

                    // Construir el menú al final
                    Menu menu = Menu.builder()
                                    .withId(menuId)
                                    .withNombre(menuNombre)
                                    .withPrecio(menuPrecio)
                                    .withPlatos(platosPorTipo)
                                    .build();

                    menus.add(menu);
                }
                return menus;
            }
        } catch (SQLException sqlException) {
            throw manageException(sqlException);
        }
    }

    @Override
    public List<Plato> buscarPlato(Tipo tipo, List<String> ingredientes) throws RestauranteException
    {
        final String SQL = """       
                            SELECT p.id AS plato_id, p.nombre AS plato_nombre,
                                              p.descripcion AS descripcion_plato,
                                              p.precio AS precio, p.tipo AS plato_tipo
                            FROM platos p
                            WHERE p.tipo = ?
                               AND NOT EXISTS (
                                               SELECT 1
                                               FROM cantidad_ingredientes ci
                                               JOIN ingredientes i ON ci.id_ingrediente = i.id
                                               WHERE ci.id_plato = p.id
                                                   AND i.nombre = ANY(?)
                                               )
                            GROUP BY p.id, p.nombre, p.descripcion, p.precio, p.tipo
                            ORDER BY p.nombre;
                           """;

        List<Plato> platos = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, tipo.name());

            // Convertir la lista de ingredientes en un arreglo SQL
            Array ingredientesArray = connection.createArrayOf("VARCHAR", ingredientes.toArray());
            preparedStatement.setArray(2, ingredientesArray);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    platos.add(Plato.builder()
                                    .withId(resultSet.getString("plato_id"))
                                    .withNombre(resultSet.getString("plato_nombre"))
                                    .withDescripcion(resultSet.getString("descripcion_plato"))
                                    .withPrecio(resultSet.getDouble("precio"))
                                    .withTipo(Tipo.valueOf(resultSet.getString("plato_tipo")))
                                    .build());
                }
                return platos;
            }
        }catch (SQLException sqlException)
        {
            throw manageException(sqlException);
        }
    }

    @Override
    public void actualizarPrecioPlato(String nombre, double porcentaje) throws RestauranteException {
        final String SQL_ACTUALIZAR_PLATO= """
                                            UPDATE platos
                                            SET precio = precio* ( 1 + ? )
                                            WHERE nombre = ?
                                           """;
        final String SQL_RECALCULAR_MENU = """
                                            UPDATE menus m
                                            SET precio = (
                                                            SELECT SUM(p.precio)
                                                            FROM menu_platos mp
                                                            JOIN platos p ON mp.id_plato = p.id
                                                            WHERE mp.id_menu = m.id
                                                          ) * 0.85
                                            WHERE m.id IN (
                                                            SELECT mp.id_menu
                                                            FROM menu_platos mp
                                                            JOIN platos p ON mp.id_plato = p.id
                                                            WHERE p.nombre = ?
                                                           );
                                           """;

        try (PreparedStatement actualizarPlato = connection.prepareStatement(SQL_ACTUALIZAR_PLATO);
             PreparedStatement recalcularMenus = connection.prepareStatement(SQL_RECALCULAR_MENU))
        {

            if (porcentaje < 0 || porcentaje > 1) throw new ValorNoValidoException();
            //actualizar platos
            actualizarPlato.setDouble(1, porcentaje);
            actualizarPlato.setString(2, nombre);
            int count = actualizarPlato.executeUpdate();
            if (count== 0) throw new PlatoNotFoundException();
            System.out.println("Precio de "+nombre+" actualizado");
            // actualizar menus
            recalcularMenus.setString(1, nombre);
            recalcularMenus.executeUpdate();
            System.out.println("Precio de Menus con: "+nombre+" Recalculados");

        } catch (SQLException sqlException)
        {
            throw manageException(sqlException);
        }
    }

}
