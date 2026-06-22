package Units;
import Game.Position;

// BUG FIX 4: removed unused import of jdk.jshell.spi.ExecutionControl
public abstract class Enemy extends Unit {
    protected int experience;
    public Enemy(int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        super(name, healthPool, healthAmount, attackPoints, defencePoints, position);
        this.experience = experience;
    }

    public boolean AdvanceAccept(Unit unit){
        return unit.AdvanceVisit(this);
    }
    public boolean AttackAccept(Unit unit){
        return unit.AttackVisit(this);
    }

    public boolean AdvanceVisit(Player p){
        int res = CombatSystem.Combat(this,p);
        if (res == -1) {
            System.out.println("Place holder for player is dead.");
            return false;
        }
        System.out.println("place holder for player is alive and gained points? or 0 points");
        return true;
    }
    public boolean AttackVisit(Player p){
        CombatSystem.Attack(this,p);
        return false;
    }
    public int getExperience() {
        return experience;
    }
    public String toString(){
        return this.getName();
    }
}
