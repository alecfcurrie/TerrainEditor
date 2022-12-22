package model;

// Represents a medieval-era battle class.
// All methods modeled after https://stackoverflow.com/questions/2497521/implementing-tostring-on-java-enums
public enum BattleClass {
    LORD("Lord"),
    SOLDIER("Soldier"),
    FIGHTER("Fighter"),
    MYRMIDON("Myrmidon"),
    ARCHER("Archer"),
    HEALER("Healer"),
    NINJA("Ninja"),
    THIEF("Thief"),
    CAVALIER("Cavalier"),
    PEGASUS_KNIGHT("Pegasus Knight"),
    WYVERN_RIDER("Wyvern Rider"),
    MAGE("Mage");

    private String name;

    // EFFECTS: Constructs a BattleClass where the toString() method will return the given name
    BattleClass(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
