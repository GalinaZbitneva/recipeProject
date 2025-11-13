package galka.recipes.services;

import galka.recipes.converters.RecipeCommandToRecipe;
import galka.recipes.converters.RecipeToRecipeCommand;
import galka.recipes.domain.Recipe;
import galka.recipes.exceptions.NotFoundException;
import galka.recipes.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;


    @BeforeEach
    public void setUp(){
        //обращаемся к Mockito чтобы заработал @Mock
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository,recipeCommandToRecipe,recipeToRecipeCommand);

    }

    @Test
    void getRecipes() throws Exception{
        Recipe recipe = new Recipe();
        Recipe recipe2 = new Recipe();
        //второй рецепт придется добавлять с указанием индекса, иначе он не добавится в сет
        recipe2.setId(2L);
        Set<Recipe> recipesData =new HashSet<>();
        recipesData.add(recipe);
        recipesData.add(recipe2);

        //строчка говорит: если я вызываю метод findAll() у мок объекта recipeRepository, то надо вернуть recipesData
        when(recipeRepository.findAll()).thenReturn(recipesData);

        //
        Set<Recipe> recipes = recipeService.getRecipes();
        assertEquals(2,recipes.size());
        //этой строчкой говорим что у мока recipeRepository метод findAll был вызван только один раз
        verify(recipeRepository,times(1)).findAll();
    }
    @Test
    void getRecipeByIdTest(){
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> optionalRecipe = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);

        Recipe returnRecipe = recipeService.getRecipeById(1L);

        assertNotNull(returnRecipe);
        verify(recipeRepository,times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getRecipeByIdTestNotFound() throws Exception{
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        NotFoundException notFoundException = assertThrows(NotFoundException.class, ()->recipeService.getRecipeById(1L), "Expected exception to throw an error. But it didn't ");

        assertTrue(notFoundException.getMessage().contains("Recipe is not found"));
        //"Recipe is not found" именно такой текст в случае ошибки  в методе getRecipeById(Long id) class RecipeServiceImpl
    }


    @Test
    void deleteRecipeById() {
        Long idForDelete = 2L;

        //no 'when', since method has void return type
        recipeRepository.deleteById(idForDelete);

        verify(recipeRepository, times(1)).deleteById(anyLong());
    }



}