package me.andrz.brace;

/**
* Created by anders on 10/28/15.
*/
public abstract class Incrementor<T extends Comparable<T>> {
    protected T start;
    protected T end;
    protected Integer incr;

    public T getStart() {
        return start;
    }

    public void setStart(T start) {
        this.start = start;
    }

    public T getEnd() {
        return end;
    }

    public void setEnd(T end) {
        this.end = end;
    }

    public Integer getIncr() {
        return incr;
    }

    public void setIncr(Integer incr) {
        this.incr = incr;
    }

    public Incrementor(T start, T end, Integer incr) {
        this.start = start;
        this.end = end;
        this.incr = incr;
    }

    public abstract void next();

    public String valid() {
        if (start == null || end == null) {
            return "null";
        }
        if (incr == null) {
            incr = start.compareTo(end) < 0 ? 1 : -1;
        }
        else {
            boolean zero = incr == 0;
            if (zero) {
                return "inf zero";
            }
            boolean neg = incr < 0 && start.compareTo(end) < 0;
            if (neg) {
                return "inf neg";
            }
            boolean pos = incr > 0 && start.compareTo(end) > 0;
            if (pos) {
                return "inf pos";
            }
        }
        return null;
    }

    public boolean hasNext() {
        return incr > 0 ? start.compareTo(end) <= 0 : start.compareTo(end) >= 0;
    }
}
