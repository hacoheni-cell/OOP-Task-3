package SpecialAbility;

public class AvengersShiled implements SpecialAbility {
    @Override
    private final Integer range = 3;
    public int GetRange() {
        return range;
    }

    @Override
    public int Cast() {
        return 0;
    }
}
