package Units;

import Combat_System.CombatSystem;
import Game.GameContext;
import Game.Position;
import Game.MessageCallback;
import java.util.List;

public class Trap extends Enemy {
    protected int visibilityTime;
    protected int invisibilityTime;
    protected int ticksCount;
    protected boolean visible;
    protected int visionRange;

    public Trap(int visibilityTime, int invisibilityTime, int ticksCount, boolean visible, int experience, String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, Position position, int visionRange, String tile, CombatSystem combat, MessageCallback messageCallback) {
        super(experience, name, healthPool, healthAmount, attackPoints, defencePoints, position, tile, combat, messageCallback);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.ticksCount = ticksCount;
        this.visible = visible;
        this.visionRange = visionRange;
    }

    @Override
    public String toString() {
        return visible ? this.getName() : ".";
    }

    @Override
    public String Description() {
        return String.format("%s\t\tVision Range: %d\t\tDescription: %s",
                super.Description(), this.visionRange);
    }

    @Override
    public int GetRange() {
        return this.visionRange;
    }

    @Override
    public void GameTick() {
        visible = ticksCount < visibilityTime;
        ticksCount++;
        if (ticksCount == (visibilityTime + invisibilityTime)) {
            ticksCount = 0;
        }
    }

    @Override
    public void takeTurn(GameContext gameContext) {
        GameTick();
        List<Unit> targets = gameContext.getUnitsInRange(this.position, this.visionRange);
        for (Unit target : targets) {
            this.Attack(target);
        }
    }

    @Override
    public void cast(Unit target) {
        if (!this.visible) {
            return;
        }
        if (this.position.range(target.getPos()) < 2) {
            int damage = Math.max(0, this.getAttackPoints() - target.getDefencePoints());
            target.setHealthAmount(target.getHealthAmount() - damage);
            messageCallback.send(this.getName() + " hit " + target.getName() + " for " + damage + " damage.");
        }
    }
}