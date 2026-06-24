package Units;
import Combat_System.CombatSystem;
import Game.Position;

public class Trap extends Enemy {
    protected int visibilityTime;
    protected int invisibilityTime;
    protected int ticksCount;
    protected boolean visible;
    protected int visionRange;


    public Trap(int visibilityTime, int invisibilityTime, int ticksCount, boolean visible,int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position,int visionRange,char tile, CombatSystem combat) {
        super(experience,name,healthPool,healthAmount,attackPoints,defencePoints,position, tile,combat );
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = ticksCount;
        this.visible = visible;
        this.visionRange = visionRange;
    }
    public String toString(){
        return this.name;
    }

    @Override
    @Override
    public int GetRange() {
        return this.visionRange;
    }
}
