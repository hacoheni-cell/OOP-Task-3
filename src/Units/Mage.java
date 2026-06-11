package Units;
import Game.Position;

public class Mage extends Player {
    protected Integer spellPower;
    protected Integer manaPool;
    protected Integer currentMana;
    protected Integer manaCost;
    protected Integer hitsCount;
    protected Integer abilityRange;
    public Mage(String name, int healthPool, int attack, int defence, Position pos, SpecialAbility specialAbility, CombatSystem combat, Integer manaPool, Integer manaCost, Integer spellPower, Integer hitsCount, Integer abilityRange) {
        super(name, healthPool, attack, defence, pos,specialAbility,combat);
        this.manaPool = manaPool;
        currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.abilityRange = abilityRange;

    }

    public void LevelUp(){
        PlayerLevelUp();
        SetManaPool(manaPool + 25 * playerLevel);
        //no completed.
    }

    public void SetManaPool(int i) {
        //no complteded
    }

    //game tick is missing
    @Override
    public int Cast() {

        return 0;
    }

    @Override
    public String Description() {
        return "place holder for Mage description";
    }
}
