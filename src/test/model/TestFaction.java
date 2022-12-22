package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFaction {

    @Test
    void testToString() {
        assertEquals("Player", Faction.PLAYER.toString());
    }
}
