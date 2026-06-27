package combat;

import Units.Unit;

public interface CombatSystem {
    int Combat(Unit attacker, Unit Defender);
    boolean Attack(Unit otherUnit, double damage, String attackerName);

}
