package es.upsa.ssbbdd2.trabajo2.application;

import es.upsa.ssbbdd2.trabajo2.domain.entities.CantidadIngrediente;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Menu;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Plato;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Tipo;
import es.upsa.ssbbdd2.trabajo2.domain.exceptions.RestauranteException;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;


public interface UseCases
{
    Plato registrarPlato(String nombre, String descripcion, double precio, Tipo tipo, List<CantidadIngrediente> cantidadesIngredientes) throws RestauranteException;
    Menu registrarMenu(String nombre, LocalDate desde, LocalDate hasta, List<String> platos) throws RestauranteException;
    List<Menu> buscarMenu(LocalDate fecha) throws RestauranteException;
    List<Plato> buscarPlato(Tipo tipo, List<String> ingredientes) throws RestauranteException;
    void subirPrecioPlato(String nombre, double porcentaje) throws RestauranteException;
}
