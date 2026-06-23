package Units;
import Combat_System.CombatSystem;
import Game.Position;

public class Monster extends Enemy {
    protected int visionRange;

    public Monster(int visionRange,int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position,String description, CombatSystem combatUtiles){
        super(experience,name,healthPool,healthAmount,attackPoints,defencePoints,position, combatUtiles);
        this.visionRange = visionRange;
    }

    @Override
    public String description() {
        return super.description() + String.format("\t\tVision Range: %d", visionRange);
    }

    @Override
    public int GetRange(){
        return visionRange;
    }
}
