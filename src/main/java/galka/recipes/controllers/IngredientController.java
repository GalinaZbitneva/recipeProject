package galka.recipes.controllers;

import galka.recipes.commands.IngredientCommand;
import galka.recipes.commands.RecipeCommand;
import galka.recipes.commands.UnitOfMeasureCommand;
import galka.recipes.domain.Ingredient;
import galka.recipes.services.IngredientService;
import galka.recipes.services.RecipeService;
import galka.recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
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

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model ){
        Long longRecipeId = Long.valueOf(recipeId);
        RecipeCommand recipeCommand = recipeService.findCommandById(longRecipeId);
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(longRecipeId);
        model.addAttribute("ingredient", ingredientCommand);
        ingredientCommand.setUom(new UnitOfMeasureCommand());
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
        //в результате переходим на страницу редактирования ингридиента

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable String recipeId,
                                   @PathVariable String ingredientId, Model model){
        Long longRecipeId = Long.valueOf(recipeId);
        Long longIngredientId = Long.valueOf(ingredientId);
        model.addAttribute("ingredient",ingredientService.findByRecipeIdAndIngredientId(longRecipeId,longIngredientId) );
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms() );
//в результате переходим на страницу редактирования ингридиента
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command){
        IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        log.debug("saved receipe id:" + savedCommand.getRecipeId());
        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public  String deleteIngredientInRecipe(@PathVariable String recipeId, @PathVariable String ingredientId){
        Long longRecipeId = Long.valueOf(recipeId);
        Long longIngredientId = Long.valueOf(ingredientId);
        ingredientService.deleteIngredientInRecipe(longRecipeId,longIngredientId);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }

}
