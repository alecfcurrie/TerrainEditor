package model;

// Represents a unit faction
public enum Faction {
    PLAYER("Player"),
    ENEMY("Enemy"),
    ALLY("Ally");

    private String name;

    // EFFECTS: Constructs a Faction where the toString() method will return the given name
    Faction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
