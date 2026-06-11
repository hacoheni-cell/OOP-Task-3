package Units;

import jdk.jshell.spi.ExecutionControl;

abstract public class Enemy extends Unit{
    int experience;
    public Enemy(int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position){
        super(name, healthPool, healthAmount, attackPoints, defencePoints, position);
        this.experience = experience;
    }

    abstract public void visit(Player player);
    abstract public void accept(Unit unit);
    public void visit(Unit unit){
        return unit.accept(this);
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
