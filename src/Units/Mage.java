package Units;
import Combat_System.CombatSystem;
import Game.Position;
import java.util.List;
import java.util.Random;

public class Mage extends Player {
    protected final Integer attackRange;
    protected Integer spellPower;
    protected Integer manaPool;
    protected Integer currentMana;
    protected Integer manaCost;
    protected Integer hitsCount;
    public Mage(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, Integer manaPool, Integer manaCost, Integer spellPower, Integer hitsCount, Integer abilityRange,  char tileString) {
        super(name, healthPool, attack, defence, pos, tileString, combat);
        this.manaPool = manaPool;
        currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.attackRange = abilityRange;
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
    public int Cast(List<Unit> listOfUnits) {
        int hits = 0;
        if( currentMana < manaCost ) {
            throw new IllegalArgumentException("mana is too low for cast.");
        }
        SetCurrentMana(currentMana - manaCost);
        while(hits < hitsCount ) {
            if (listOfUnits == null || listOfUnits.size() == 0) {
                return 0;
            }
            Random rand = new Random();
            int randomIndex = rand.nextInt(listOfUnits.size());
            Unit otherUnit = listOfUnits.get(randomIndex);
            this.Attack(otherUnit);
            if(otherUnit.healthAmount == 0) {
                listOfUnits.remove(otherUnit);
            }
            hits++;
        }
        return 0;
    }
    public boolean Cast(Enemy enemy) {
        return this.combatUtiles.Attack(enemy,this.spellPower,"Mage");
    }

    @Override
    public String Description() {
        return "place holder for Mage description";
    }
    @Override
    public int GetRange() {
        return attackRange;
    }

    @Override
    public void GameTick() {
        SetCurrentMana(Math.min(manaPool,currentMana + playerLevel));
    }
}
