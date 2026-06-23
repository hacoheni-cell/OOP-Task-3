package Combat_System;

import Units.Unit;

public interface CombatSystem {
    int Combat(Unit attacker, Unit Defender);
    int Attack(Unit otherUnit, double damage, String attackerName);

}
