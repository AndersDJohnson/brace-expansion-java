package me.andrz.brace;

/**
* Created by anders on 10/28/15.
*/
public class IntegerIncrementor extends Incrementor<Integer> {

    public IntegerIncrementor(Integer start, Integer end, Integer incr) {
        super(start, end, incr);
    }

    @Override
    public void next() {
        start += incr;
    }
}
