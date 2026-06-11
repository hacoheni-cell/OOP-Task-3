package Units;
import Game.Position;
import SpecialAbility.SpecialAbility;
import SpecialAbility.Blizzard;

public class Mage extends Player {
    protected final Integer Range;
    protected SpecialAbility specialA;
    protected Integer spellPower;
    protected Integer manaPool;
    protected Integer currentMana;
    protected Integer manaCost;
    protected Integer hitsCount;
    public Mage(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, Integer manaPool, Integer manaCost, Integer spellPower, Integer hitsCount, Integer abilityRange) {
        super(name, healthPool, attack, defence, pos, combat);
        this.manaPool = manaPool;
        currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        specialA = new Blizzard(abilityRange);
        this.Range = abilityRange;
    }
    protected void SetSpellPower(int i) {
        if ( i < 0){
            spellPower = 0;
        }
        else {
            spellPower = i;
        }
    }

    protected void SetCurrentMana(int i) {
        if (i < 0) {
            currentMana = 0;
        }
        else if ( i > manaPool) {
            currentMana = manaPool;
        }
        else {
            currentMana = i;
        }
    }

    protected void SetManaPool(int i) {
        if(i < 0) {
            manaPool = 0;
        }
        else {
            manaPool = i;
        }
    }

    public void LevelUp(){
        super.LevelUp();
        SetManaPool(manaPool + 25 * playerLevel);
        SetCurrentMana(currentMana + manaPool / 4);
        SetSpellPower(spellPower + 10 * playerLevel);
    }



    //game tick is missing
    @Override
    public int Cast() {
        int hits = 0;
        if( currentMana < manaCost ) {
            throw new IllegalArgumentException("mana is too low for cast.");
        }
        SetCurrentMana(currentMana - manaCost);
        //another cast logic.
        return 0;
    }

    @Override
    public String Description() {
        return "place holder for Mage description";
    }
    @Override
    public int GetRange() {
        return Range;
    }
}
