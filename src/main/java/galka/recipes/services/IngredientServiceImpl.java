package galka.recipes.services;

import galka.recipes.commands.IngredientCommand;
import galka.recipes.commands.RecipeCommand;
import galka.recipes.converters.IngredientCommandToIngredient;
import galka.recipes.converters.IngredientToIngredientCommand;
import galka.recipes.domain.Ingredient;
import galka.recipes.domain.Recipe;
import galka.recipes.repositories.RecipeRepository;
import galka.recipes.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
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

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        //метод нужен чтобы уже существующему ингридиенту задать новые параметры из command или добавить новый ингридиент в рецепт
        Optional<Recipe> optionalRecipe = recipeRepository.findById(command.getRecipeId());
         if (!optionalRecipe.isPresent()){
             //todo toss error if not found!
             log.error("Recipe not found for id: " + command.getRecipeId());
             return new IngredientCommand();
         } else {
             Recipe recipe = optionalRecipe.get();
             Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                     .stream()
                     .filter(ingredient -> ingredient.getId().equals(command.getId()))
                     .findFirst();

             if (optionalIngredient.isPresent()){
                 Ingredient searchingIngredient = optionalIngredient.get();
                 //теперь задаем новые значения из command
                 searchingIngredient.setDescription(command.getDescription());
                 searchingIngredient.setAmount(command.getAmount());
                 searchingIngredient.setUom(unitOfMeasureRepository.findById(command.getUom().getId()).orElseThrow(() -> new RuntimeException("UOM NOT FOUND")));


             } else {
                 //add new Ingredient
                 recipe.addIngredient(ingredientCommandToIngredient.convert(command));
             }
             Recipe savedRecipe = recipeRepository.save(recipe);
             //теперь возвращаем сохраненный ингридиент в формате ingredientCommand
             return ingredientToIngredientCommand.convert(savedRecipe.getIngredients()
                     .stream().filter(ingredient -> ingredient.getId().equals(command.getId()))
                     .findFirst()
                     .get());
          }


    }


}
