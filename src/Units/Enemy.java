package Units;
import Combat_System.CombatSystem;
import Game.Level;
import Game.Position;

public abstract class Enemy extends Unit {
    protected int experience;
    public Enemy(int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position, CombatSystem combatUtiles){
        super(name, healthPool, healthAmount, attackPoints, defencePoints, position, combatUtiles);
        this.experience = experience;
    }

    public boolean AdvanceAccept(Unit unit){
        return unit.AdvanceVisit(this);
    }
    public boolean AttackAccept(Unit unit){
        return unit.AttackVisit(this);
    }

    public boolean AdvanceVisit(Player p){
        int res = combatUtiles.Combat(this,p);
        if (res == -1) {
            System.out.println("Place holder for player is dead.");
            return false;
        }
        System.out.println("place holder for player is alive and gained points? or 0 points");
        return true;
    }
    public boolean AttackVisit(Player p){
        int res = combatUtiles.Attack(this,p);
        return false;
    }
    public int getExperience() {
        return experience;
    }
    public String toString(){
        return this.getName();
    }
    public abstract void processStep(Level currentLevel);
}
