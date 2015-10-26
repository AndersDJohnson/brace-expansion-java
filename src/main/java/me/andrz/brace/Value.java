package me.andrz.brace;

/**
 * Created by anders on 10/25/15.
 */
public class Value<T> {

    private T value;

    public Value() {}

    public Value (T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
