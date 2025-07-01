package galka.recipes.controllers;

import galka.recipes.domain.Category;
import galka.recipes.domain.UnitOfMeasure;
import galka.recipes.repositories.CategoryRepository;
import galka.recipes.repositories.UnitOfMeasureRepository;
import galka.recipes.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {
   private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/","/index"})
    public String getIndexPage(Model model){
       model.addAttribute("recipes",recipeService.getRecipes());

        return "index";
    }
}
