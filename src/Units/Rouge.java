package Units;
import Game.Position;
public class Rouge extends Player {
    Integer cost;
    Integer currentEnergy;
    public Rouge(String name, int healthPool, int attack, int defence, Position pos, SpecialAbility specialAbility, CombatSystem combat, Integer cost) {
        super(name, healthPool, attack, defence, pos,specialAbility,combat);
        this.cost = cost;
        this.currentEnergy = 100;
    }

    public void LevelUp(){
        this.currentEnergy = 100;
        Unit.setAttackPoints(attackPoints + (3 * playerLevel));
        //Player.LevelUp();? need to do it or it automaticly?
        //playerLevel++; in player?
    }
    //game tick is missing
    @Override
    public int Cast() {
//ability cast in here or in ability?
        currentEnergy -= cost;
    }

    @Override
    public String Description() {
        return "place holder for Rouge description";
    }
}
