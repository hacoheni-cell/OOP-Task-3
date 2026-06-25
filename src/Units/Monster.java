package Units;

import Combat_System.CombatSystem;
import Game.GameContext;
import Game.Position;

public class Monster extends Enemy {
    protected int visionRange;
    protected String description;

    private static final int[][] MOVEMENT_DIRECTIONS = {
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0},
            {0, 0}
    };

    public Monster(int visionRange, int experience, String name, int healthPool, int healthAmount, int attackPoints, int defencePoints, Position position, String description,String tile, CombatSystem combatUtiles){
        super(experience, name, healthPool, healthAmount, attackPoints, defencePoints, position, tile, combatUtiles);
        this.visionRange = visionRange;
        this.description = description;
    }

    @Override
    public String Description() {
        return String.format("%s\t\tVision Range: %d\t\tDescription: %s",
                super.Description(), this.visionRange, this.description);
    }

    @Override
    public int GetRange(){
        return visionRange;
    }

    @Override
    public void GameTick() {
        return;
    }

    @Override
    public void cast(Unit unit) {
        return;
    }

    @Override
    public void takeTurn(GameContext gameContext) {
        Position playerPos = gameContext.getPlayerPos();

        if (this.position.range(playerPos) < visionRange) {
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