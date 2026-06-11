package Units;

public class Monster extends Enemy{
    protected int visionRange;

    public Monster(int visionRange,int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        super(experience,name,healthPool,healthAmount,attackPoints,defencePoints,position);
        this.visionRange = visionRange;
    }
    public void accept(Unit unit){
        unit.visit(this);
    }

    @Override
    public void visit(Player p) {
         CombatSystem.combat(this,p);
    }
}
