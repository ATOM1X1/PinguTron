package pgdp.robot;

import java.util.function.Consumer;

/**
 * Abstract sensor class for creating new sensors.
 * @param <T>
 */
public abstract class Sensor<T> {
    protected Robot owner;
    protected Consumer<T> processor;
    protected String name;
    protected double reliability;

    public Sensor(String name, double reliability) {
        this.name = name;
        this.reliability = reliability;
    }

    public void setOwner(Robot owner) {
        this.owner = owner;
    }

    public Sensor<T> setProcessor(Consumer<T> processor) {
        this.processor = processor;
        return this;
    }

    public abstract T getData();

    public abstract Sensor<T> createNewSensor();
}
