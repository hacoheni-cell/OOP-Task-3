package SpecialAbility;

public class FanOfKnives extends SpecialAbility{
    protected final Integer range = 2;
    @Override
    public int GetRange() {
        return range;
    }

    @Override
    public int Cast() {
        return 0;
    }
}
