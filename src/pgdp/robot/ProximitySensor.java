package pgdp.robot;

/**
 * Sensor for robots to measure the distance to nearest obstacles.
 * Is attached to the robots outline
 */
public class ProximitySensor extends Sensor<Double>{
    protected double directionOffset, accuracy, maxRange;

    public ProximitySensor(String name, double directionOffset, double accuracy, double maxRange, double reliability) {
        super(name, reliability);
        this.directionOffset = directionOffset;
        this.accuracy = accuracy;
        this.maxRange = maxRange;
    }

    @Override
    public Double getData() {
        var ownerOffsetPos = new Position(owner.getPosition());
        ownerOffsetPos.moveBy(owner.getSize() / 2, owner.getDirection() + directionOffset);
        var newPos = new Position(ownerOffsetPos);
        while (ownerOffsetPos.distanceTo(newPos) < maxRange - 1e-12){
            if (owner.getWorld().getTerrain(newPos) != ' '){
                break;
            }
            newPos.moveBy(accuracy, owner.getDirection() + directionOffset);
        }
        return ownerOffsetPos.distanceTo(newPos);
    }

    @Override
    public Sensor<Double> createNewSensor() {
        var newSensor = new ProximitySensor(name, directionOffset, accuracy, maxRange, reliability);
        newSensor.setProcessor(processor);
        return newSensor;
    }

    @Override
    public String toString() {
        return "ProximitySensor " + name;
    }
}
