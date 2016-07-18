package me.andrz.brace;

/**
* Created by anders on 10/28/15.
*/
public class CharacterIncrementor extends Incrementor<Character> {

    public CharacterIncrementor(Character start, Character end, Integer incr) {
        super(start, end, incr);
    }

    /**
     * TODO: Will Character.toChars()[0] work with most Unicode? Probably not with high and low surrogate pairs.
     */
    @Override
    public void next() {
        start = Character.toChars(start + incr)[0];
    }
}
