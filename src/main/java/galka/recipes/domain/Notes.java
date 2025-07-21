package galka.recipes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
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


}
