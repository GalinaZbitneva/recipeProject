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
                 Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                 ingredient.setRecipe(recipe);
                 recipe.addIngredient(ingredient);
             }
             Recipe savedRecipe = recipeRepository.save(recipe);

             //если ингридиент уже существует
             Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(command.getId())).findFirst();

             if (!savedIngredientOptional.isPresent()){
                 //тот случай когда ингридиент command  новый и айди у него еще нет
                 savedIngredientOptional = savedRecipe.getIngredients().stream()
                         .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                         .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                         .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                         .findFirst();
             }
             //теперь возвращаем сохраненный ингридиент в формате ingredientCommand
             return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
          }


    }

    @Override
    @Transactional
    public void deleteIngredientInRecipe(Long recipeId, Long ingredientId){

        log.debug("Deleting ingredient: " + recipeId + ":" + ingredientId);

        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isPresent()){
            Recipe recipe = recipeOptional.get();
            log.debug("found recipe");

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientId))
                    .findFirst();

            if(ingredientOptional.isPresent()){
                log.debug("found Ingredient");
                Ingredient ingredientToDelete = ingredientOptional.get();
                ingredientToDelete.setRecipe(null);
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }
    }


}
