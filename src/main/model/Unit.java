package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;
import java.util.Objects;

// Represents a unit, with a position, class, and faction.
public class Unit implements Writable {

    private Faction faction;
    private BattleClass battleClass;
    private Point position;

    // EFFECTS: Constructs a unit with the given specifications
    public Unit(Faction faction, BattleClass battleClass, int x, int y) {
        this.faction = faction;
        this.battleClass = battleClass;
        this.position = new Point(x, y);
        EventLog.getInstance().logEvent(new Event(EventUtility.getUnitInstantiationMessage(this)));
    }

    //MODIFIES: this
    //EFFECTS: sets unit's faction to the given faction
    public void setFaction(Faction faction) {
        this.faction = faction;
        EventLog.getInstance().logEvent(new Event(EventUtility.getFactionChangeMessage(this)));
    }

    //MODIFIES: this
    //EFFECTS: sets unit's battle class to the given class
    public void setBattleClass(BattleClass battleClass) {
        this.battleClass = battleClass;
        EventLog.getInstance().logEvent(new Event(EventUtility.getBattleClassChangeMessage(this)));
    }

    //MODIFIES: this
    //EFFECTS : sets unit's position to the new coordinates
    public void move(int x, int y) {
        position.setLocation(x, y);
        EventLog.getInstance().logEvent(new Event(EventUtility.getMoveUnitMessage(x, y)));
    }

    public Faction getFaction() {
        return faction;
    }

    public BattleClass getBattleClass() {
        return battleClass;
    }

    //EFFECTS: Returns the unit's x coordinate
    public int getX() {
        return position.x;
    }

    //EFFECTS: Returns the unit's y coordinate
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

    // EFFECTS: Returns a JSONObject representation of this
    // Method modeled after methods from the WorkRoom class
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
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