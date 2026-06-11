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
        PlayerLevelUp();
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
}
