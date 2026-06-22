package Units;
import Game.Position;

public abstract class Player extends Unit {
    protected Integer experience;
    protected Integer playerLevel;
    protected CombatSystem combatUtiles;

    public Player(String name, int healthPool, int attack, int defence, Position pos, CombatSystem combat){
        super(name, healthPool, healthPool, attack, defence, pos);
        experience = 0;
        playerLevel = 1;
        this.combatUtiles = combat;
    }

    public abstract int Cast();

    public abstract String Description();


    public boolean AdvanceAccept(Unit other) {
        return other.AdvanceVisit(this);
    }
    public boolean AttackAccept(Unit other) {
        return other.AttackVisit(this);
    }
    protected boolean AdvanceVisit(Enemy enemy) {
        int res = this.combatUtiles.Combat(this, enemy);
        if (res == -1) {
            System.out.println("Place holder for player is dead.");
            return false;
        }
        System.out.println("place holder for player is alive and gained points? or 0 points");
        return true;
    }
   protected boolean AttackVisit(Enemy enemy){
        return CombatSystem.Attack(this,enemy);
   }
    public void LevelUp(){
        SetExperience(experience - 50 * playerLevel);
        playerLevel++;
        SetHealthPool(healthPool + 10 * playerLevel);
        SetHealthAmount(healthPool);
        SetAttackPoints(attackPoints + 4 * playerLevel);
        SetDefencePoints(defencePoints + playerLevel);
    }

    protected void SetExperience(int i) {
        experience = Math.max(i, 0);
        if(experience >= 50 * playerLevel) {
            LevelUp();
        }
    }
    public abstract int GetRange();
}
