package combat;

import Units.Unit;
import businessLayer.MessageCallback;
import java.util.Random;

public class DefaultCombat implements CombatSystem {
    private Random random = new Random();
    public static  DefaultCombat INSTANCE = null;
    private MessageCallback messageSender;

    private DefaultCombat(MessageCallback messageSender) {
        this.messageSender = messageSender;
    }
    public static DefaultCombat getInstance(MessageCallback messageSender) {
        if (INSTANCE == null) {
            INSTANCE = new DefaultCombat(messageSender);
        }
        return INSTANCE;
    }

    @Override
    public int Combat(Unit attacker, Unit defender) {
        int attackRoll = random.nextInt(attacker.getAttackPoints() + 1);
        int defenceRoll = random.nextInt(defender.getDefencePoints() + 1);
        int damage = attackRoll - defenceRoll;
        messageSender.send(attacker.getName() + " engaged in combat with " + defender.getName() + ".");
        messageSender.send(attacker.Description());
        messageSender.send(defender.Description());
        messageSender.send(attacker.getName() + " rolled " + attackRoll + " attack points.");
        messageSender.send(defender.getName() + " rolled " + defenceRoll + " defense points.");

        if (damage > 0) {
            messageSender.send(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + ".");

            int newHealth = defender.getHealthAmount() - damage;
            defender.SetHealthAmount(newHealth);
        }
        else {
            messageSender.send(attacker.getName() + " dealt 0 damage.");
        }

        if (defender.getHealthAmount() <= 0) {
            return -1;
        }
        return damage;
    }

    public boolean Attack(Unit otherUnit, double damage, String attackerName) {
        int defenceRoll = random.nextInt(otherUnit.getDefencePoints() + 1);
        int damageTaken = (int) damage - defenceRoll;
        messageSender.send(otherUnit.getName() + " rolled " + defenceRoll + " defense points.");
        if (damageTaken > 0) {
            messageSender.send(attackerName + " dealt " + damageTaken + " damage to " + otherUnit.getName() + " with a special ability.");
            double newHealth = otherUnit.getHealthAmount() - damageTaken;
            otherUnit.SetHealthAmount((int) newHealth);
        }
        else {
            damageTaken = 0;
            messageSender.send(attackerName + "'s attack was fully blocked by " + otherUnit.getName() + " (0 damage).");
        }

        if (otherUnit.getHealthAmount() <= 0) {
            return true;
        }

        return false;
    }
}