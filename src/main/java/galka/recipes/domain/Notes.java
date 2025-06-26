package galka.recipes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;

@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //здесь не нужно в скобках указывать взаимоотношения. Главным является Recipe. Если мы удаляем его
    //то мы хотим чтобы и удалились все заметки связанные с ним
    //если удаляем заметку, то нам не нужно удалялять рецепт с ней связанный
    @OneToOne
    private Recipe recipe;

    //@Lob исользуется для больших объектов. создается облачное поле для хранения данных
    @Lob
    private String recipeNotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getRecipeNotes() {
        return recipeNotes;
    }

    public void setRecipeNotes(String recipeNotes) {
        this.recipeNotes = recipeNotes;
    }
}
