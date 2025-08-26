package galka.recipes.converters;

import galka.recipes.commands.CategoryCommand;
import galka.recipes.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {

    CategoryCommandToCategory converter;
    public static Long ID_VALUE = 1L;
    public static final String DESCRIPTION = "description";

    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void testNullObject() throws Exception{
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception{
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    void convert() {
        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID_VALUE);
        categoryCommand.setDescription(DESCRIPTION);

        //конвертируем
        Category category = converter.convert(categoryCommand);

        //проверяем, действительно ли произошка конвертация с указанными ID_VALUE  DESCRIPTION
        assertEquals(ID_VALUE,category.getId());
        assertEquals(DESCRIPTION, category.getDescription());

    }
}