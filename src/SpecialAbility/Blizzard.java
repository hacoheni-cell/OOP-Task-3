package SpecialAbility;

public class Blizzard extends SpecialAbility {
    private final Integer range;
    public Blizzard(Integer range) {
        this.range = range;
    }
    @Override
    public int GetRange() {
        return range;
    }

    @Override
    public int Cast() {
        return 0;
    }
}
