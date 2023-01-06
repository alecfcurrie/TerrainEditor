package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Represents a unit, with a position, class, and faction.
 */
public class Unit implements Writable {

    private static final Logger log = Logger.getGlobal();
    private Faction faction;
    private BattleClass battleClass;
    private Point position;

    /**
     * Constructs a unit with the given specifications
     *
     * @param faction the unit's Faction
     * @param battleClass the unit's BattleClass
     * @param x the unit's x coordinate
     * @param y the unit's y coordinate
     */
    public Unit(Faction faction, BattleClass battleClass, int x, int y) {
        this.faction = faction;
        this.battleClass = battleClass;
        this.position = new Point(x, y);
        log.fine(EventUtility.getUnitInstantiationMessage(this));
    }

    /**
     * Sets unit's faction to the given Faction
     *
     * @param faction the unit's new Faction
     */
    public void setFaction(Faction faction) {
        this.faction = faction;
        log.fine(EventUtility.getFactionChangeMessage(this));
    }

    /**
     * Sets unit's BattleClass to the given class
     *
     * @param battleClass the unit's new BattleClass
     */
    public void setBattleClass(BattleClass battleClass) {
        this.battleClass = battleClass;
        log.fine(EventUtility.getBattleClassChangeMessage(this));
    }

    /**
     * Returns the unit's Faction
     *
     * @return the unit's Faction
     */
    public Faction getFaction() {
        return faction;
    }

    /**
     * Returns the unit's BattleClass
     *
     * @return the unit's BattleClass
     */
    public BattleClass getBattleClass() {
        return battleClass;
    }

    /**
     * Returns the unit's x coordinate
     *
     * @return the unit's x coordinate
     */
    public int getX() {
        return position.x;
    }

    /**
     * Returns the unit's y coordinate
     *
     * @return the unit's y coordinate
     */
    public int getY() {
        return position.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Unit unit = (Unit) o;
        return faction == unit.faction && battleClass == unit.battleClass && position.equals(unit.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faction, battleClass, position);
    }

    @Override
    public JSONObject toJson() {
        JSONObject unitJson = new JSONObject();
        unitJson.put("faction", faction);
        unitJson.put("class", battleClass);
        unitJson.put("x", position.x);
        unitJson.put("y", position.y);
        return unitJson;
    }
}