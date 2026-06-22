package Combat_System;

import Units.Unit;

public interface CombatSystem {
    int Combat(Unit attacker, Unit Defender);
    int Attack(Unit attacker, Unit Defender);

}
