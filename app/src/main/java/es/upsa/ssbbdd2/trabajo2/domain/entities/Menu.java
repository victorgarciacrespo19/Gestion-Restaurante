package es.upsa.ssbbdd2.trabajo2.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class Menu
{
    private String id;
    private String nombre;
    private LocalDate desde;
    private LocalDate hasta;
    private double precio;
    private Map<Tipo, List<Plato>> platos;
}
