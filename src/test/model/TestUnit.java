package model;

import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

// Tests all functionality of the Unit class
public class TestUnit {

    Unit playerUnit;
    Unit enemyUnit;
    Unit allyUnit;

    EventLog el = EventLog.getInstance();

    @BeforeEach
    void setup() {
        playerUnit = new Unit(Faction.PLAYER, BattleClass.LORD, 0, 0);
        enemyUnit = new Unit(Faction.ENEMY, BattleClass.SOLDIER, 1, 2);
        allyUnit = new Unit(Faction.ALLY, BattleClass.CAVALIER, 7, 3);
        el.clear();
    }

    @Test
    void testConstructor() {
        assertEquals(Faction.PLAYER, playerUnit.getFaction());
        assertEquals(BattleClass.LORD, playerUnit.getBattleClass());
        assertEquals(0, playerUnit.getX());
        assertEquals(0, playerUnit.getY());

        assertEquals(Faction.ENEMY, enemyUnit.getFaction());
        assertEquals(BattleClass.SOLDIER, enemyUnit.getBattleClass());
        assertEquals(1, enemyUnit.getX());
        assertEquals(2, enemyUnit.getY());
    }

    @Test
    void testEventLogInitialization() {
        allyUnit = new Unit(Faction.ALLY, BattleClass.ARCHER, 7, 3);
        Iterator<Event> eli = el.iterator();
        assertEquals(EventUtility.getClearLogMessage(), eli.next().getDescription());
        assertEquals(EventUtility.getUnitInstantiationMessage(allyUnit), eli.next().getDescription());
        assertFalse(eli.hasNext());
    }

    @Test
    void testSetFaction() {
        Faction newFaction = Faction.ENEMY;
        // Model tests
        playerUnit.setFaction(newFaction);
        assertEquals(newFaction, playerUnit.getFaction());
        // Event log tests
        Iterator<Event> eli = el.iterator();
        assertEquals(EventUtility.getClearLogMessage(), eli.next().getDescription());
        assertEquals(EventUtility.getFactionChangeMessage(playerUnit), eli.next().getDescription());
    }

    @Test
    void testSetFactionMultiple() {
        playerUnit.setFaction(Faction.ENEMY);
        playerUnit.setFaction(Faction.ALLY);
        assertEquals(Faction.ALLY, playerUnit.getFaction());
    }

    @Test
    void testSetClass() {
        BattleClass newBattleClass = BattleClass.MYRMIDON;
        //Model tests
        playerUnit.setBattleClass(newBattleClass);
        assertEquals(newBattleClass, playerUnit.getBattleClass());
        //Event log tests
        Iterator<Event> eli = el.iterator();
        assertEquals(EventUtility.getClearLogMessage(), eli.next().getDescription());
        assertEquals(EventUtility.getBattleClassChangeMessage(playerUnit), eli.next().getDescription());
    }

    @Test
    void testSetClassMultiple() {
        enemyUnit.setBattleClass(BattleClass.FIGHTER);
        assertEquals(BattleClass.FIGHTER, enemyUnit.getBattleClass());
        enemyUnit.setBattleClass(BattleClass.ARCHER);
        enemyUnit.setBattleClass(BattleClass.MAGE);
        enemyUnit.setBattleClass(BattleClass.HEALER);
        enemyUnit.setBattleClass(BattleClass.NINJA);
        enemyUnit.setBattleClass(BattleClass.THIEF);
        allyUnit.setBattleClass(BattleClass.PEGASUS_KNIGHT);
        enemyUnit.setBattleClass(BattleClass.WYVERN_RIDER);
        assertEquals(BattleClass.WYVERN_RIDER, enemyUnit.getBattleClass());
        assertEquals(BattleClass.PEGASUS_KNIGHT, allyUnit.getBattleClass());
    }

    @Test
    void testMove() {

        int x = 4;
        int y = 10;
        //Model tests
        allyUnit.move(x, y);
        assertEquals(x, allyUnit.getX());
        assertEquals(y, allyUnit.getY());
        //Event log tests
        Iterator<Event> eli = el.iterator();
        assertEquals(EventUtility.getClearLogMessage(), eli.next().getDescription());
        assertEquals(EventUtility.getMoveUnitMessage(x, y), eli.next().getDescription());
    }

    @Test
    void testMoveMultiple() {
        allyUnit.move(5, 11);
        allyUnit.move(3, 6);
        assertEquals(3, allyUnit.getX());
        assertEquals(6, allyUnit.getY());
    }

    @Test
    void testEqualsBothEqualUnits() {
        assertEquals(playerUnit,
                new Unit(playerUnit.getFaction(), playerUnit.getBattleClass(), playerUnit.getX(), playerUnit.getY()));
    }

    @Test
    void testEqualsNonEqualUnit() {
        assertNotEquals(playerUnit, enemyUnit);
        assertNotEquals(allyUnit, enemyUnit);
        assertNotEquals(playerUnit, allyUnit);

        assertNotEquals(playerUnit,
                new Unit(playerUnit.getFaction(), BattleClass.WYVERN_RIDER, playerUnit.getX(), playerUnit.getY()));
        assertNotEquals(playerUnit,
                new Unit(Faction.ENEMY, playerUnit.getBattleClass(), playerUnit.getX(), playerUnit.getY()));
        assertNotEquals(playerUnit,
                new Unit(playerUnit.getFaction(),
                        playerUnit.getBattleClass(),
                        playerUnit.getX() + 2,
                        playerUnit.getY()));
    }

    @Test
    void testEqualsWrongObjects() {
        assertNotEquals(playerUnit, null);
        assertNotEquals(playerUnit, "A very cute cat");
    }

    @Test
    void testHashCode() {
        assertEquals(Objects.hash(playerUnit.getFaction(),
                        playerUnit.getBattleClass(),
                        new Point(playerUnit.getX(),
                                playerUnit.getY())),
                playerUnit.hashCode());
    }

}

