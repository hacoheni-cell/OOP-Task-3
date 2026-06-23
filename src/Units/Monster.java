package Units;
import Combat_System.CombatSystem;
import Game.Level;
import Game.Position;

public class Monster extends Enemy {
    protected int visionRange;
    protected String description;

    public Monster(int visionRange,int experience,String name,int healthPool,int healthAmount,int attackPoints,int defencePoints,Position position,String description, CombatSystem combatUtiles){
        super(experience,name,healthPool,healthAmount,attackPoints,defencePoints,position, combatUtiles);
        this.visionRange = visionRange;
        this.description = description;
    }

    @Override
    public String Description() {
        return this.description;
    }

    @Override
    public int GetRange(){
        return visionRange;
    }

    public void processStep(Level currentLevel) {
        Position playerPos = currentLevel.getPlayer().getPos();

        if (this.position.range(playerPos) < visionRange) {
            int dx = this.position.getX() - playerPos.getX();
            int dy = this.position.getY() - playerPos.getY();

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    currentLevel.getCell(this.position, -1, 0).Accept(this);
                } else {
                    currentLevel.getCell(this.position, 1, 0).Accept(this);
                }
            } else {
                if (dy > 0) {
                    currentLevel.getCell(this.position, 0, -1).Accept(this);
                } else {
                    currentLevel.getCell(this.position, 0, 1).Accept(this);
                }
            }
        } else {
            randomMove(currentLevel);
        }
    }
    private boolean randomMove(Level currentLevel) {
        int moveChoice = (int) (Math.random() * 5);
        switch (moveChoice) {
            case 0: currentLevel.getCell(this.position, 0, -1).Accept(this); break;
            case 1: currentLevel.getCell(this.position, 0, 1).Accept(this); break;
            case 2: currentLevel.getCell(this.position, -1, 0).Accept(this); break;
            case 3: currentLevel.getCell(this.position, 1, 0).Accept(this); break;
            case 4: break;
        }
    }
}
