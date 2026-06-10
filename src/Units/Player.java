package Units;

public abstract class Player extends Unit {
    Integer experience;
    Integer playerLevel;
    SpecialAbility specialAbility;
    public Player(String name){
        super();
        this.Health = 100;
    }
    public boolean cast() {
        specialAbility.cast();
    }
    public boolean accept(Unit other) {
        other.visit(this);
    }

    public abstract boolean  visit(Unit);
}
