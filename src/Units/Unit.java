package Units;

public abstract class Unit {
    private String name;
    private int healthPool;
    private int healthAmount;
    private int attackPoints;
    private int defencePoints;
    private Position position;
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
