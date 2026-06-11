package Units;

public class Monster extends Enemy{
    protected int visionRange;

    public Monster(int visionRange,int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        super(experience,name,healthPool,healthAmount,attackPoints,defencePoints,position);
        this.visionRange = visionRange;
    }
    public void AdvanceAccept(Unit unit){
        unit.AdvanceVisit(this);
    }

    @Override
    public void AdvanceVisit(Player p) {
         CombatSystem.combat(this,p);
    }
}
