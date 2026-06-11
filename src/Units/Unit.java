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
    protected static void SetHealthPool(int i) {
    }

    protected static void SetDefencePoints(int i) {
    }

    protected static void SetHealthAmount(int min) {
    }

    protected static void SetAttackPoints(int i) {
    }
    public abstract String Description();


    public  boolean Advance(Unit other) {
        return other.Accept(this);
    }
    public  boolean Accept(Unit unit){
        return false;
    }
    private boolean Visit(Unit unit) {return false;}
    public boolean Visit(Player player) {return false;}
    public  boolean Visit(Enemy enemy) {return false;}
}
