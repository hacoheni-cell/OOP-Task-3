package Units;

import Combat_System.CombatSystem;
import Game.GameContext;
import Game.Position;
import Game.MessageCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Player extends Unit {
    protected Integer experience;
    protected Integer playerLevel;
    protected Map<String, Consumer<GameContext>> actions;

    public Player(String name, int healthPool, int attack, int defence, Position pos, String tileString, CombatSystem combat, MessageCallback messageCallback){
        super(name, healthPool, healthPool, attack, defence, pos, tileString, combat, messageCallback);
        experience = 0;
        playerLevel = 1;
        initializeActions();
    }

    private void initializeActions() {
        actions = new HashMap<>();

        actions.put("w", ctx -> ctx.getCell(this.getPos(), 0, -1).Accept(this));
        actions.put("s", ctx -> ctx.getCell(this.getPos(), 0, 1).Accept(this));
        actions.put("a", ctx -> ctx.getCell(this.getPos(), -1, 0).Accept(this));
        actions.put("d", ctx -> ctx.getCell(this.getPos(), 1, 0).Accept(this));

        actions.put("e", ctx -> this.Cast(ctx.getUnitsInRange(this.getPos(), this.GetRange())));
        actions.put("q", ctx -> passTurn());
    }

    private void passTurn() {
        return;
    }

    public void processInput(String input, GameContext context) {
        Consumer<GameContext> action = actions.get(input.toLowerCase());

        if (action != null) {
            action.accept(context);
        } else {
            messageCallback.send("Unknown command. Use W, A, S, D, E, or Q.");
        }
    }

    public abstract int Cast(List<Unit> listOfUnits);

    @Override
    public String Description() {
        return String.format("%s\t\tLevel: %d\t\tExperience: %d/%d",
                super.Description(), this.playerLevel, this.experience, (50 * playerLevel));
    }

    public boolean AdvanceAccept(Unit other) {
        return other.AdvanceVisit(this);
    }
    public boolean AttackAccept(Unit other) {
        return other.AttackVisit(this);
    }
    public boolean AdvanceVisit(Enemy enemy) {
        int res = this.combatUtiles.Combat(this, enemy);
        if (res == -1) {
            return false;
        }
        return true;
    }
    public boolean AttackVisit(Enemy enemy) {
        return this.Cast(enemy);
    }

    public abstract boolean Cast(Enemy enemy);

    public void LevelUp() {
        SetExperience(experience - 50 * playerLevel);
        playerLevel++;
        int healthGain = 10 * playerLevel;
        int attackGain = 4 * playerLevel;
        int defenceGain = playerLevel;

        SetHealthPool(healthPool + healthGain);
        SetHealthAmount(healthPool);
        SetAttackPoints(attackPoints + attackGain);
        SetDefencePoints(defencePoints + defenceGain);

        messageCallback.send(this.name + " reached level " + playerLevel + ": +" + healthGain + " Health, +" + attackGain + " Attack, +" + defenceGain + " Defense");
    }

    public void SetExperience(int i) {
        experience = Math.max(i, 0);
        if(experience >= 50 * playerLevel) {
            LevelUp();
        }
    }
    public boolean isDead(){
        return healthAmount <= 0;
    }
    public abstract int GetRange();
    public String toString() {
        if(isDead()) {
            return "X";
        }
        return this.tileString;
    }
}