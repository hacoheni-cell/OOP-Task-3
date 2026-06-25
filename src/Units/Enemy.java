package Units;

import Combat_System.CombatSystem;
import Game.GameContext;
import Game.Position;
import Game.MessageCallback;

public abstract class Enemy extends Unit {
    protected int experience;

    public Enemy(int experience, String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, Position position, String tileString, CombatSystem combatUtiles, MessageCallback messageCallback) {
        super(name, healthPool, healthAmount, attackPoints, defencePoints, position, tileString, combatUtiles, messageCallback);
        this.experience = experience;
    }

    public boolean AdvanceAccept(Unit unit) {
        return unit.AdvanceVisit(this);
    }

    public boolean AttackAccept(Unit unit) {
        return unit.AttackVisit(this);
    }

    public boolean AdvanceVisit(Player p) {
        combatUtiles.Combat(this, p);
        return true;
    }

    @Override
    public String Description() {
        return super.Description() + String.format("\t\tExperience: %d", this.experience);
    }

    public boolean AttackVisit(Player p) {
        this.cast(p);
        return true;
    }

    public int getExperience() {
        return experience;
    }

    public String toString() {
        return this.getName();
    }

    public abstract void cast(Unit unit);
    public abstract void takeTurn(GameContext gameContext);
}