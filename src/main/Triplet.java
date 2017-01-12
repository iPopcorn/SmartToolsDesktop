package main;

/**
 * Created by taylor on 1/12/17.
 */
public class Triplet<T, U, V> {
    private final T first;
    private final U second;
    private V third;

    public Triplet(T first, U second, V third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public V getThird() {
        return third;
    }

    public void setThird(V newThird){
        this.third = newThird;
    }


}
