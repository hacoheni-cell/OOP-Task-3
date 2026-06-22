package SpecialAbility;

public class AvengersShiled extends SpecialAbility {
    private final Integer range = 3;
    @Override
    public int GetRange() {
        return range;
    }

    @Override
    public int Cast() {
        return 0;
    }
}
