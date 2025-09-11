package galka.recipes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;

    //@OneToOne(fetch = FetchType.EAGER)
    @ManyToOne
    private UnitOfMeasure uom;

    //Recipe к которому относится данный Ingredient и здесь будет создана связь таблиц
    //relation ingredient to recipe @ManyToOne
    @ManyToOne
    private Recipe recipe;

   public Ingredient(String string, BigDecimal bigDecimal, UnitOfMeasure tableSpoonUom) {
   }

    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom, Recipe recipe) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
        this.recipe = recipe;
    }


}
