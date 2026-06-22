package Units;

public class Trap extends Enemy {
    protected int visibilityTime;
    protected int invisibilityTime;
    protected int ticksCount;
    protected boolean visible;
    protected int visionRange;
    protected String description;

    public Trap(int visibilityTime, int invisibilityTime, int ticksCount, boolean visible,int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position,int visionRange,String description) {
        super(experience,name,healthPool,healthAmount,attackPoints,defencePoints,position);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = ticksCount;
        this.visible = visible;
        this.visionRange = visionRange;
        this.description = description;
    }
    public String toString(){
        return this.name;
    }

    @Override
    public String Description() {
        return description;
    }

    @Override
    public int GetRange() {
        return this.visionRange;
    }
}
