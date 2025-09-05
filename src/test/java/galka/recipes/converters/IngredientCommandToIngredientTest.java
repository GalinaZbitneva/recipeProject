package galka.recipes.converters;

import galka.recipes.commands.IngredientCommand;
import galka.recipes.commands.UnitOfMeasureCommand;
import galka.recipes.domain.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

    IngredientCommandToIngredient converter;
    public static final BigDecimal AMOUNT = new BigDecimal("1");
    public static final String DESCRIPTION = "Cheeseburger";
    public static final Long ID_VALUE = 1L;
    public static final Long UOM_ID = 2L;
    public static final Long RECIPE_ID = 3L;


    @BeforeEach
    void setUp() {
        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new IngredientCommand()));
    }


    @Test
    void convert() {
        IngredientCommand ingredientCommand = new IngredientCommand();
        UnitOfMeasureCommand uom = new UnitOfMeasureCommand();
        uom.setId(UOM_ID);
        ingredientCommand.setId(ID_VALUE);
        ingredientCommand.setAmount(AMOUNT);
        ingredientCommand.setDescription(DESCRIPTION);
        ingredientCommand.setUom(uom);
        //ingredientCommand.setRecipeId(RECIPE_ID);

        Ingredient ingredient =  converter.convert(ingredientCommand);

        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(ID_VALUE, ingredient.getId());
        assertEquals(DESCRIPTION, ingredient.getDescription());
        assertEquals(UOM_ID, ingredient.getUom().getId());

        assertNotNull(ingredient);
        assertNotNull(ingredient.getUom());
    }

    @Test
    public void convertWithNullUOM(){
        IngredientCommand ingredientCommand = new IngredientCommand();

        ingredientCommand.setId(ID_VALUE);
        ingredientCommand.setAmount(AMOUNT);
        ingredientCommand.setDescription(DESCRIPTION);
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();

        Ingredient ingredient = converter.convert(ingredientCommand);

        assertNotNull(ingredient);
        assertNull(ingredient.getUom());
        assertEquals(ID_VALUE, ingredient.getId());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(DESCRIPTION, ingredient.getDescription());

    }
}