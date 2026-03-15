package es.upsa.ssbbdd2.trabajo2.application;

import es.upsa.ssbbdd2.trabajo2.domain.entities.CantidadIngrediente;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Menu;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Plato;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Tipo;
import es.upsa.ssbbdd2.trabajo2.domain.exceptions.RestauranteException;

import java.time.LocalDate;
import java.util.List;

public interface Dao extends AutoCloseable
{
    Plato insertarPlato(String nombre, String descripcion, double precio, Tipo tipo, List<CantidadIngrediente> cantidadesIngredientes)throws RestauranteException;
    Menu insertarMenu(String nombre, LocalDate desde, LocalDate hasta, List<String> platos) throws RestauranteException;
    List<Menu> buscarMenu(LocalDate fecha) throws RestauranteException;
    List<Plato> buscarPlato(Tipo tipo, List<String> ingredientes) throws RestauranteException;
    void actualizarPrecioPlato(String nombre, double porcentaje)throws RestauranteException;
}
