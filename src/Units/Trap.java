package Units;

public class Trap extends Enemy {
    protected int visibilityTime;
    protected int invisibilityTime;
    protected int ticksCount;
    protected boolean visible;
    
    public Trap(int visibilityTime, int invisibilityTime, int ticksCount, boolean visible,int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position) {
        super(experience,name,healthPool,healthAmount,attackPoints,defencePoints,position);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = ticksCount;
        this.visible = visible;
    }
    public String toString(){
        return "Trap";
    }
    public void accept(Unit unit){
        unit.visit(this);
    }

    public void visit(Player p){
        combatSystem.combat(this,p);
    }
}
