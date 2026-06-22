package Combat_System;

import Units.Unit;
import java.util.Random;

public class DefaultCombat implements CombatSystem {
    private Random random = new Random();

    @Override
    public int Combat(Unit attacker, Unit defender) {
        int attackRoll = random.nextInt(attacker.getAttackPoints() + 1);
        int defenceRoll = random.nextInt(defender.getDefencePoints() + 1);
        int damage = attackRoll - defenceRoll;
        System.out.println(attacker.getName() + " engaged in combat with " + defender.getName() + ".");
        System.out.println(attacker.getName() + " rolled " + attackRoll + " attack points.");
        System.out.println(defender.getName() + " rolled " + defenceRoll + " defense points.");

        if (damage > 0) {
            System.out.println(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + ".");

            int newHealth = defender.getHealthAmount() - damage;
            defender.setHealthAmount(newHealth);
        }
        else {
            System.out.println(attacker.getName() + " dealt 0 damage.");
        }
        if (defender.getHealthAmount() <= 0) {
            return -1;
        }
        return damage;
    }
}