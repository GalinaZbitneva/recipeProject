package galka.recipes.controllers;

import galka.recipes.domain.Recipe;
import galka.recipes.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {
   @Mock
    RecipeService recipeService;
   @Mock
   Model model;

   IndexController indexController;

    @BeforeEach
    void setUp() {
        //обращаемся к Mockito чтобы заработал @Mock
        MockitoAnnotations.openMocks(this);
        indexController = new IndexController(recipeService);
    }
// такой тип теста используется для тестирования именно контроллера типа springMVC controller
    @Test
    void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void getIndexPage() throws Exception {
        //given
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());
        // пришлось ввести второй рецепт таким образом иначе он не добавлялся в сет
       Recipe recipe1 = new Recipe();
       recipe1.setId(1L);
       recipes.add(recipe1);

        when(recipeService.getRecipes()).thenReturn(recipes);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String view = indexController.getIndexPage(model);



        //then
        //проверяем, что возвращаемое значение соответвует странице с именем index
        assertEquals("index",view);
        //проверяем, что метод getRecipes вызывается 1 раз  моком recipeService
        verify(recipeService,times(1)).getRecipes();
        //проверяем что в моке model метод addAttribute вызывается 1 раз, в блоке given создали Set который будет проверяться
        //"recipes"  берем из attributed name
        verify(model,times(1)).addAttribute(eq("recipes"),argumentCaptor.capture());
        Set<Recipe> setIndController = argumentCaptor.getValue();

        assertEquals(2,setIndController.size());
    }
}