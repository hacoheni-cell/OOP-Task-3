package Units;

import combat.CombatSystem;
import Game.Position;
import businessLayer.MessageCallback;

public abstract class Unit {
    protected String name;
    protected int healthPool;
    protected int healthAmount;
    protected int attackPoints;
    protected int defencePoints;
    protected Position position;
    protected CombatSystem combatUtiles;
    String tileString;
    protected MessageCallback messageCallback;

    public Unit(String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, Position position, String tileString, CombatSystem combatUtiles, MessageCallback messageCallback) {
        this.name = name;
        this.healthPool = healthPool;
        this.healthAmount = healthAmount;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
        this.position = position;
        this.combatUtiles = combatUtiles;
        this.tileString = tileString;
        this.messageCallback = messageCallback;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void SetHealthPool(int i) {
        if (i < 0) {
            this.healthPool = 0;
        }
        else {
            this.healthPool = i;
        }
    }

    public void SetDefencePoints(int i) {
        if(i < 0) {
            this.defencePoints = 0;
        }
        else {
            this.defencePoints = i;
        }
    }

    public void SetHealthAmount(int health) {
        if (health > healthPool) {
            this.healthAmount = healthPool;
        }
        else {
            this.healthAmount = health;
        }
    }

    public void SetAttackPoints(int i) {
        if (i < 0) {
            attackPoints = 0;
        }
        else {
            attackPoints = i;
        }
    }

    public String Description() {
        return String.format("%s\t\tHealth: %d/%d\t\tAttack: %d\t\tDefence: %d",
                name, healthAmount, healthPool, attackPoints, defencePoints);
    }

    public boolean Advance(Unit other){
        return other.AdvanceAccept(this);
    }
    public boolean Attack(Unit other){
        return other.AttackAccept(this);
    }
    public abstract boolean AttackAccept(Unit unit);
    public abstract boolean AdvanceAccept(Unit unit);
    public boolean AdvanceVisit(Player player) {return false;}
    public boolean AdvanceVisit(Enemy enemy) {return false;}
    public boolean AttackVisit(Player player) {return false;}
    public boolean AttackVisit(Enemy enemy) {return false;}
    public abstract int GetRange();
    public abstract void GameTick();

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getDefencePoints() {
        return defencePoints;
    }

    public int getHealthAmount() {
        return healthAmount;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public boolean isDead() {
        return healthAmount <=0;
    }
    public Position getPos() {
        return position;
    }
    public String toString() {
        return this.tileString;
    }
}