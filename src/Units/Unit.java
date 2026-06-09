package Units;

public class Unit {
    private String name;
    private int healthPool;
    private int healteAmount;
    private int attackPoints;
    private int defencePoints;
    private Position position;
    public Unit(String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        this.name = name;
        this.healthPool = healthPool;
        this.healteAmount = healteAmount;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
        this.position = position;
    }
}
