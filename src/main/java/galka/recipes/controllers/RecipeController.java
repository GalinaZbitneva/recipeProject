package galka.recipes.controllers;

import galka.recipes.commands.RecipeCommand;
import galka.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @RequestMapping("/recipe/show/{id}")
    public String showById(@PathVariable String id, Model model){
        Long longId = Long.valueOf(id);
        model.addAttribute("recipe",recipeService.getRecipeById(longId));
        return "recipe/show";
    }


    //в браузере мы перейдем на вкладку с формой для создания нового рецепта, с присвоением  атрибута recipe типа RecipeCommand
    @RequestMapping({"recipe/new"})
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    //теперь эту форму (пост ) нужно сохранить и запостить как страницу браузера
    @PostMapping("recipe/")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command){
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        //специальной командой redirect возвращает страницу с новым рецептом
        return "redirect:/recipe/show/" + savedCommand.getId();
    }

    @RequestMapping("recipe/update/{id}")
    public String updateRecipe(@PathVariable String id, Model model){
        Long londId = Long.valueOf(id);
        model.addAttribute("recipe", recipeService.findCommandById(londId));
        return "recipe/recipeform";
    }

}
