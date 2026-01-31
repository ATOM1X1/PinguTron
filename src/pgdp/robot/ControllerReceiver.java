package pgdp.robot;

/**
 * Processes user input which is handled inside PhysicalController in World class
 */
public class ControllerReceiver extends Sensor<World.Controller>{

    public ControllerReceiver(String name, double reliability) {
        super(name, reliability);
    }

    @Override
    public World.Controller getData() {
        return owner.getWorld().getController();
    }

    @Override
    public Sensor<World.Controller> createNewSensor() {
        var newSensor = new ControllerReceiver(name, reliability);
        newSensor.setProcessor(processor);
        return newSensor;
    }

    @Override
    public String toString() {
        return "ControllerReceiver " + name;
    }
}
