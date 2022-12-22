package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBattleClass {

    @Test
    void testToString() {
        assertEquals("Pegasus Knight", BattleClass.PEGASUS_KNIGHT.toString());
    }

}
