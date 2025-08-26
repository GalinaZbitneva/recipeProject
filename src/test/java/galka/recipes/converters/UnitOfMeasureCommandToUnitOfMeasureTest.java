package galka.recipes.converters;

import galka.recipes.commands.UnitOfMeasureCommand;
import galka.recipes.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureCommandToUnitOfMeasureTest {

    UnitOfMeasureCommandToUnitOfMeasure converter;
    public final Long VALUE_ID = 1L;
    public final String DESCRIPTION = "description";

    @BeforeEach
    void setUp() {
        converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }
    @Test
    public void testNullObject(){
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject(){
        assertNotNull(converter.convert(new UnitOfMeasureCommand()));
    }

    @Test
    void convert() {
        UnitOfMeasureCommand uomCom = new UnitOfMeasureCommand();
        uomCom.setId(VALUE_ID);
        uomCom.setDescription(DESCRIPTION);

        UnitOfMeasure uom = converter.convert(uomCom);

        assertEquals(VALUE_ID, uom.getId());
        assertEquals(DESCRIPTION, uom.getDescription());

    }
}