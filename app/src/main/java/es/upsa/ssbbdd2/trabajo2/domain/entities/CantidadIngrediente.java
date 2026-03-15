package es.upsa.ssbbdd2.trabajo2.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class CantidadIngrediente
{
    private Ingrediente ingrediente;
    private int cantidad;
    private Unidad unidadMedida;
}
