package pgdp.robot;

/**
 * Memory class
 * @param <T> Memory type
 */
public class Memory<T> {
    private String label;
    private T data;

    public Memory(String label, T data) {
        this.label = label;
        this.data = data;
    }

    public T get() {
        return data;
    }

    public void set(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return label + ": " + data;
    }
}
