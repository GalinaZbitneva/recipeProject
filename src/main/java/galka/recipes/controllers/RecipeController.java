package galka.recipes.controllers;

import galka.recipes.commands.RecipeCommand;
import galka.recipes.exceptions.NotFoundException;
import galka.recipes.services.RecipeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Controller
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("/recipe/show/{id}")
    public String showById(@PathVariable String id, Model model){
        Long longId = Long.valueOf(id);
        model.addAttribute("recipe",recipeService.getRecipeById(longId));
        return "recipe/show";
    }


    //в браузере мы перейдем на вкладку с формой для создания нового рецепта, с присвоением  атрибута recipe типа RecipeCommand
    @GetMapping
    @RequestMapping({"recipe/new"})
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return "recipe/recipeform";
    }

    //теперь эту форму (пост ) нужно сохранить и запостить как страницу браузера
    @PostMapping("recipe/")
    public String saveOrUpdate(@Valid  @ModelAttribute("recipe") RecipeCommand command, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {log.debug(objectError.toString());});
            return "recipe/recipeform";
        }

        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        //специальной командой redirect возвращает страницу с новым рецептом
        return "redirect:/recipe/show/" + savedCommand.getId();
    }

    @GetMapping
    @RequestMapping("recipe/update/{id}")
    public String updateRecipe(@PathVariable String id, Model model){
        Long londId = Long.valueOf(id);
        model.addAttribute("recipe", recipeService.findCommandById(londId));
        return "recipe/recipeform";
    }

   @GetMapping
    @RequestMapping("recipe/delete/{id}")
    public  String deleteRecipe(@PathVariable String id){
        Long londId = Long.valueOf(id);
        log.debug("Deleting id: " + id);
        recipeService.deleteRecipeById(londId);
        return "redirect:/";

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception){

        log.error("Handling not found exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        //имя файла html
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }






}
