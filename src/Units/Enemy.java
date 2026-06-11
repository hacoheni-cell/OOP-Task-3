package Units;

import jdk.jshell.spi.ExecutionControl;

abstract public class Enemy extends Unit{
    protected int experience;
    public Enemy(int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        super(name, healthPool, healthAmount, attackPoints, defencePoints, position);
        this.experience = experience;
    }

    public void AdvanceAccept(Unit unit){
        unit.AdvanceVisit(this);
    }
    protected void AdvanceVisit(Player p){
        CombatSystem.combat(this,p);
    }
    public int getExperience() {
        return experience;
    }
    public String toString(){
        return "" + this.getName();
    }
    public void attack(Unit unit){
        .....
    }
    public boolean equals(Object object){
        boolean res = false;
        if(object instanceof Enemy){

        }
    }
}
