package Units;

import jdk.jshell.spi.ExecutionControl;

  public abstract class Enemy extends Unit{
    protected int experience;
    public Enemy(int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        super(name, healthPool, healthAmount, attackPoints, defencePoints, position);
        this.experience = experience;
    }

    public boolean AdvanceAccept(Unit unit){
        return unit.AdvanceVisit(this);
    }
    public boolean AttackAccept(Unit unit){
        return  unit.AttackVisit(this);
    }
    protected boolean AdvanceVisit(Player p){
        return CombatSystem.Combat(this,p);
    }
    protected boolean AttackVisit(Player p){
        return CombatSystem.Attack(this,p);
    }
    public int getExperience() {
        return experience;
    }
    public String toString(){
        return this.getName();
    }
}
