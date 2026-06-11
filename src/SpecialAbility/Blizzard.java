package SpecialAbility;

public class Blizzard extends SpecialAbility {
    private final Integer range;

    @Override
    public Blizzard(Integer range){
        this.range = range;
    }
    public int GetRange() {
        return range;
    }

    @Override
    public int Cast() {
        return 0;
    }
}
