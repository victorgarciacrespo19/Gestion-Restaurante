package es.upsa.ssbbdd2.trabajo2.application.impl;

import es.upsa.ssbbdd2.trabajo2.application.Dao;
import es.upsa.ssbbdd2.trabajo2.application.UseCases;
import es.upsa.ssbbdd2.trabajo2.domain.entities.CantidadIngrediente;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Menu;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Plato;
import es.upsa.ssbbdd2.trabajo2.domain.entities.Tipo;
import es.upsa.ssbbdd2.trabajo2.domain.exceptions.RestauranteException;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder(setterPrefix = "with")
public class UseCasesImpl implements UseCases
{
    private Dao dao;

    @Override
    public Plato registrarPlato(String nombre, String descripcion, double precio, Tipo tipo, List<CantidadIngrediente> cantidadesIngredientes) throws RestauranteException
    {
        return dao.insertarPlato(nombre, descripcion, precio, tipo, cantidadesIngredientes);
    }

    @Override
    public Menu registrarMenu(String nombre, LocalDate desde, LocalDate hasta, List<String> platos) throws RestauranteException
    {
        return dao.insertarMenu(nombre, desde, hasta, platos);
    }

    @Override
    public List<Menu> buscarMenu(LocalDate fecha) throws RestauranteException
    {
        return dao.buscarMenu(fecha);
    }

    @Override
    public List<Plato> buscarPlato(Tipo tipo, List<String> ingredientes) throws RestauranteException
    {
        return dao.buscarPlato(tipo, ingredientes);
    }

    @Override
    public void subirPrecioPlato(String nombre, double porcentaje) throws RestauranteException
    {
        dao.actualizarPrecioPlato(nombre, porcentaje);
    }
}
