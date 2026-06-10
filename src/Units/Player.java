package Units;

import jdk.jshell.spi.ExecutionControl;

import javax.swing.text.Position;

public abstract class Player extends Unit {
    Integer experience;
    Integer playerLevel;
    SpecialAbility specialAbility;
    protected CombatSystem combatUtiles;

    public Player(String name, int healthPool, int attack, int defence, Position pos, SpecialAbility specialAbility, CombatSystem combat){
        super(name, healthPool, healthPool, attack, defence, pos);
        experience = 0;
        playerLevel = 1;
        this.specialAbility = specialAbilty;
        this.combatUtiles = combat;
    }
    public abstract int Cast();


    public abstract String Description();


    public boolean Accept(Unit unit) {
        unit.Visit(this);
    }

    public  boolean  Visit(Enemy enemy) {
        CombatUtiles.Combat(this,enemy);
    }

}
