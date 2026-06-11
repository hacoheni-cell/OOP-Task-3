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
    public boolean setName(String newName) {
        this.name = newName;
    }

    protected  void SetHealthPool(int i) {
        if (i < 0) {
            this.healthPool = 0;
        }
        else {
            this.healthPool = i;
        }
    }

    protected  void SetDefencePoints(int i) {
        if(i < 0) {
            this.defencePoints = 0;
        }
        else {
            this.defencePoints = i;
        }
    }

    protected  void SetHealthAmount(int health) {
        if (healthPool < health) {
            this.healthAmount = health;
        }
        else {
            healthAmount = health;
        }


    }

    protected  void SetAttackPoints(int i) {
        if( attackPoints < i) {
            attackPoints = 0;
        }
        else {
            attackPoints = i;
        }
    }
    public abstract String Description();


    public  boolean Advance(Unit other) {
        return other.AdvanceAccept(this);
    }
    public abstract boolean AdvanceAccept(Unit unit);
    private boolean AdvanceVisit(Unit unit) {return false;}
    public boolean AdvanceVisit(Player player) {return false;}
    public  boolean AdvanceVisit(Enemy enemy) {return false;}
    public abstract int GetRange();
}
