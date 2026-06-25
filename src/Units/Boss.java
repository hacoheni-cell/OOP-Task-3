package Units;

import Combat_System.CombatSystem;
import Game.GameContext;
import Game.Position;
import Game.MessageCallback;
import java.util.List;

public class Boss extends Enemy {
    protected int visionRange;
    protected String description;
    protected int abilityCooldown;
    protected int remainingCooldown;

    private static final int[][] MOVEMENT_DIRECTIONS = {
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0},
            {0, 0}
    };

    public Boss(int visionRange, int experience, String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, Position position, char tileString, String description, CombatSystem combatUtiles, int abilityCooldown, MessageCallback messageCallback) {
        super(experience, name, healthPool, healthAmount, attackPoints, defencePoints, position, tileString, combatUtiles, messageCallback);
        this.visionRange = visionRange;
        this.description = description;
        this.abilityCooldown = abilityCooldown;
        this.remainingCooldown = 0;
    }

    @Override
    public String Description() {
        return String.format("%s\t\tVision Range: %d\t\tCooldown: %d/%d\t\tDescription: %s",
                super.Description(), this.visionRange, this.remainingCooldown, this.abilityCooldown, this.description);
    }

    @Override
    public int GetRange() {
        return visionRange;
    }

    @Override
    public void GameTick() {
        if (remainingCooldown > 0) {
            remainingCooldown--;
        }
    }

    @Override
    public void cast(Unit unit) {
        int damage = Math.max(0, this.getAttackPoints() - unit.getDefencePoints());
        unit.setHealthAmount(unit.getHealthAmount() - damage);
        messageCallback.send(this.getName() + " casted a special ability on " + unit.getName() + " for " + damage + " damage.");
    }

    @Override
    public void takeTurn(GameContext gameContext) {
        GameTick();
        Position playerPos = gameContext.getPlayerPos();

        if (this.position.range(playerPos) < visionRange) {
            if (remainingCooldown == 0) {
                remainingCooldown = abilityCooldown;
                List<Unit> targets = gameContext.getUnitsInRange(this.position, this.visionRange);
                for (Unit target : targets) {
                    this.Attack(target);
                }
            } else {
                int dx = this.position.getX() - playerPos.getX();
                int dy = this.position.getY() - playerPos.getY();

                int stepX = 0;
                int stepY = 0;

                if (Math.abs(dx) > Math.abs(dy)) {
                    stepX = Integer.compare(playerPos.getX(), this.position.getX());
                } else {
                    stepY = Integer.compare(playerPos.getY(), this.position.getY());
                }

                gameContext.getCell(this.position, stepX, stepY).Accept(this);
            }
        } else {
            randomMove(gameContext);
        }
    }

    private void randomMove(GameContext gameContext) {
        int randomIndex = (int) (Math.random() * MOVEMENT_DIRECTIONS.length);
        int[] chosenMove = MOVEMENT_DIRECTIONS[randomIndex];

        gameContext.getCell(this.position, chosenMove[0], chosenMove[1]).Accept(this);
    }
}