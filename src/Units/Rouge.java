package Units;
import Game.Position;

public class Rouge extends Player {
    protected Integer cost;
    protected Integer currentEnergy;
    public Rouge(String name, int healthPool, int attack, int defence, Position pos, SpecialAbility specialAbility, CombatSystem combat, Integer cost) {
        super(name, healthPool, attack, defence, pos,specialAbility,combat);
        this.cost = cost;
        this.currentEnergy = 100;
    }

    public void LevelUp(){
        PlayerLevelUp();
        this.currentEnergy = 100;
        SetAttackPoints(attackPoints + (3 * playerLevel));
    }
    //game tick is missing
    @Override
    public int Cast() {
        //if(currentEnergy < cost) { throw new IllegalArgumentException("No enough energy for cast.");}
        currentEnergy -= cost;
        //- For each enemy within range < 2, deal damage (reduce health value) equals to the
        //rogue’s attack points (each enemy will try to defend itself).
        return 0;
    }

    @Override
    public String Description() {
        return "place holder for Rouge description";
    }
}
