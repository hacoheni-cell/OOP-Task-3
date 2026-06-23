package Units;
import Combat_System.CombatSystem;
import Game.Position;

public class Monster extends Enemy {
    protected int visionRange;
    protected String description;

    public Monster(int visionRange,int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position,String description, CombatSystem combatUtiles){
        super(experience,name,healthPool,healthAmount,attackPoints,defencePoints,position, combatUtiles);
        this.visionRange = visionRange;
        this.description = description;
    }

    @Override
    public String Description() {
        return this.description;
    }

    @Override
    public int GetRange(){
        return visionRange;
    }
}
