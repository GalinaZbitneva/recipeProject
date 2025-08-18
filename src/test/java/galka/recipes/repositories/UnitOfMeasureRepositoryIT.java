package galka.recipes.repositories;

import galka.recipes.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//эта аннотация позволит подгрузить данные из базы данных
@DataJpaTest
class UnitOfMeasureRepositoryIT {
    //spring сделает injection в наш тест
    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findByDescription() {
        Optional <UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");
        assertEquals("Tablespoon",uomOptional.get().getDescription());
    }
}