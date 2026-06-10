package Units;

public class Trap extends Enemy {
    private int visibilityTime;
    private int invisibilityTime;
    private int ticksCount;
    private boolean visible;
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
}
