package Units;

import jdk.jshell.spi.ExecutionControl;

public class Enemy extends Unit{
    int experience;
    public Enemy(int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        super(name, healthPool, healthAmount, attackPoints, defencePoints, position);
        this.experience = experience;
    }

    public void vial(Player player){
        combatUtiles.combat(this,player);
    }
    public int getExperience() {
        return experience;
    }
    public String toString(){
        return "" + this.getName();
    }
    public boolean equals(Object object){
        boolean res = false;
        if(object instanceof Enemy){

        }
    }
}
