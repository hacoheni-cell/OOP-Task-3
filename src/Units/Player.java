package Units;
import Game.Position;
import jdk.jshell.spi.ExecutionControl;

public abstract class Player extends Unit {
    Integer experience;
    Integer playerLevel;
    SpecialAbility specialAbility;
    protected CombatSystem combatUtiles;

    public Player(String name, int healthPool, int attack, int defence, Position pos, SpecialAbility specialAbility, CombatSystem combat){
        super(name, healthPool, healthPool, attack, defence, pos);
        experience = 0;
        playerLevel = 1;
        this.specialAbility = specialAbility;
        this.combatUtiles = combat;
    }



    public abstract int Cast();


    public abstract String Description();


    public boolean Accept(Unit unit) {
        return unit.Visit(this);
    }

    public  boolean Visit(Enemy enemy) {
        int res = this.combatUtiles.Combat(this, enemy);
        if (res == -1) {
            System.out.println("Place holder for player is dead.");
            return false;
        }
        System.out.println("place holder for player is alive and gained points? or 0 points");
        return true;
    }
    public void LevelUp(){}

}
