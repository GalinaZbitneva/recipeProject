package galka.recipes.services;

import galka.recipes.commands.IngredientCommand;
import galka.recipes.commands.RecipeCommand;
import galka.recipes.converters.IngredientToIngredientCommand;
import galka.recipes.domain.Ingredient;
import galka.recipes.domain.Recipe;
import galka.recipes.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
    }


    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

        if(!optionalRecipe.isPresent()){
            log.error("Recipe id not found" + recipeId);
        }

        Recipe recipe = optionalRecipe.get();

        Optional<Ingredient> optionalIngredient = recipe.getIngredients().stream().filter(x->x.getId().equals(ingredientId)).findFirst();

        if (!optionalIngredient.isPresent()){
            log.error("Ingredient Id not found");
        }
        IngredientCommand ingredientCommand = ingredientToIngredientCommand.convert(optionalIngredient.get());

        return ingredientCommand;

    }
}
