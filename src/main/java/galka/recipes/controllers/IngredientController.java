package galka.recipes.controllers;

import galka.recipes.services.IngredientService;
import galka.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model){
        Long longRecipeId = Long.valueOf(recipeId);

        model.addAttribute("recipe",recipeService.findCommandById(longRecipeId));
        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable String recipeId,
                                 @PathVariable String ingredientId, Model model){
        Long longRecipeId = Long.valueOf(recipeId);
        Long longIngredientId = Long.valueOf(ingredientId);
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(longRecipeId,longIngredientId) );

        return "recipe/ingredient/show";
    }

}
