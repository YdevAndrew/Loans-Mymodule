package org.jala.university.domain.entity;

import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class SampleEntityTest {

    @Test
    void testNoArgsConstructor() {
        SampleEntity entity = new SampleEntity();
        assertNotNull(entity);
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        String name = "Test Name";
        Date createdDate = new Date();
        Date updatedDate = new Date();

        SampleEntity entity = new SampleEntity(id, name, createdDate, updatedDate);

        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(createdDate, entity.getCreated());
        assertEquals(updatedDate, entity.getUpdated());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        String name = "Sample";
        Date createdDate = new Date();
        Date updatedDate = new Date();

        SampleEntity entity = SampleEntity.builder()
                .id(id)
                .name(name)
                .created(createdDate)
                .updated(updatedDate)
                .build();

        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(createdDate, entity.getCreated());
        assertEquals(updatedDate, entity.getUpdated());
    }

    @Test
    void testSettersAndGetters() {
        SampleEntity entity = new SampleEntity();

        UUID id = UUID.randomUUID();
        String name = "Test Name";
        Date createdDate = new Date();
        Date updatedDate = new Date();

        entity.setId(id);
        entity.setName(name);
        entity.setCreated(createdDate);
        entity.setUpdated(updatedDate);

        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(createdDate, entity.getCreated());
        assertEquals(updatedDate, entity.getUpdated());
    }
}
