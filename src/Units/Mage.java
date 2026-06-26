package Units;

import Combat_System.CombatSystem;
import Game.Position;
import Game.MessageCallback;
import java.util.List;
import java.util.Random;

public class Mage extends Player {
    protected final Integer attackRange;
    protected Integer spellPower;
    protected Integer manaPool;
    protected Integer currentMana;
    protected Integer manaCost;
    protected Integer hitsCount;

    public Mage(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat, Integer manaPool, Integer manaCost, Integer spellPower, Integer hitsCount, Integer abilityRange, String tileString, MessageCallback messageCallback) {
        super(name, healthPool, attack, defence, pos, tileString, combat, messageCallback);
        this.manaPool = manaPool;
        currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.spellPower = spellPower;
        this.hitsCount = hitsCount;
        this.attackRange = abilityRange;
    }

    protected void SetSpellPower(int i) {
        if (i < 0) {
            spellPower = 0;
        } else {
            spellPower = i;
        }
    }

    protected void SetCurrentMana(int i) {
        if (i < 0) {
            currentMana = 0;
        } else if (i > manaPool) {
            currentMana = manaPool;
        } else {
            currentMana = i;
        }
    }

    protected void SetManaPool(int i) {
        if (i < 0) {
            manaPool = 0;
        } else {
            manaPool = i;
        }
    }

    public void LevelUp() {
        super.LevelUp();
        SetManaPool(manaPool + 25 * playerLevel);
        SetCurrentMana(currentMana + manaPool / 4);
        SetSpellPower(spellPower + 10 * playerLevel);
    }

    @Override
    public int Cast(List<Unit> listOfUnits) {
        int hits = 0;
        if (currentMana < manaCost) {
            messageCallback.send("Cannot cast: Not enough mana.");
            return 0;
        }
        SetCurrentMana(currentMana - manaCost);
        messageCallback.send(this.getName() + " cast Blizzard.");

        listOfUnits.remove(this);

        while (hits < hitsCount) {
            if (listOfUnits.isEmpty()) {
                break;
            }
            Random rand = new Random();
            Unit target = listOfUnits.get(rand.nextInt(listOfUnits.size()));

            this.Attack(target);

            if (target.isDead()) {
                listOfUnits.remove(target);
            }
            hits++;
        }
        return 0;
    }

    @Override
    public boolean Cast(Enemy enemy) {
        boolean hit = this.combatUtiles.Attack(enemy, this.spellPower, "Mage");

        if (enemy.isDead()) {
            messageCallback.send(enemy.getName() + " died. " + this.getName() + " gained " + enemy.getExperience() + " experience.");
            this.SetExperience(this.experience + enemy.getExperience());
        }
        return hit;
    }
    @Override
    public String Description() {
        return String.format("%s\t\tMana: %d/%d\t\tSpell Power: %d",
                super.Description(), this.currentMana, this.manaPool, this.spellPower);
    }

    @Override
    public int GetRange() {
        return attackRange;
    }

    @Override
    public void GameTick() {
        SetCurrentMana(Math.min(manaPool, currentMana + playerLevel));
    }
}