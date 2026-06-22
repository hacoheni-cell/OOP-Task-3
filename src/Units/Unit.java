package Units;
import Game.Position;

public abstract class Unit {
    protected String name;
    protected int healthPool;
    protected int healthAmount;
    protected int attackPoints;
    protected int defencePoints;
    protected Position position;
    public Unit(String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        this.name = name;
        this.healthPool = healthPool;
        this.healthAmount = healthAmount;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    // BUG FIX 1: setName declared boolean but missing return statement
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

    public abstract String Description();

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

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getDefencePoints() {
        return defencePoints;
    }

    public int getHealthAmount() {
        return healthAmount;
    }

    public void setHealthAmount(int newHealth) {
     healthAmount = newHealth;
    }
}
