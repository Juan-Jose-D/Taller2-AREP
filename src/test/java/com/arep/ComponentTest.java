package com.arep;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ComponentTest {

    @Test
    public void testComponentCreation() {
        Component component = new Component("El Padrino", "MOVIE", "Clásico del cine", 5);
        assertEquals("El Padrino", component.getName());
        assertEquals("MOVIE", component.getType());
        assertEquals("Clásico del cine", component.getDescription());
        assertEquals(5, component.getRating());
    }
    
    @Test
    public void testToString() {
        Component component = new Component("Cien años de soledad", "BOOK", "Novela de García Márquez", 4);
        String expected = "{\"name\":\"Cien años de soledad\", \"type\":\"BOOK\", \"description\":\"Novela de García Márquez\", \"rating\":4}";
        assertEquals(expected, component.toString());
    }
}