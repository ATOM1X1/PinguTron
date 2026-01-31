package pgdp.robot;

import javax.sound.sampled.*;
import java.util.*;
import java.util.function.Function;

/**
 * Robot class with its functionality. The program is handled by a processor which
 * adds evaluated commands into a queue. The commands are then executed.
 */
public class Robot {

    /// Attributes

    // final attributes
    private final String name;
    private final double size;
    private final Color color;

    protected final FailureSimulator failureSimulator;

    // internal states
    private Position position = new Position();
    private double direction;
    private World world;

    private List<Sensor<?>> sensors;
    private List<Memory<?>> memory;
    private Function<Robot, List<Command>> program;
    private Queue<Command> todo;
    private LinkedList<Position> trailPositions = new LinkedList<>();

    private String noDeath = "012345 ";
    public boolean isDead = false;

    /// Methods
    public Robot(String name, double direction, double size, Color color, FailureSimulator failureSimulator) {
        this.name = name;
        this.direction = direction;
        this.size = Math.min(Math.max(0.5, size), 1); // between 0.5 and 1
        this.failureSimulator = failureSimulator;
        this.sensors = new ArrayList<>();
        this.memory = new ArrayList<>();
        this.todo = new LinkedList<>();
        this.program = x -> List.of();
        this.color = color;
    }

    /// Pre-programmed Commands
    public boolean go(double distance) {
        // step can be negative if the penguin walks backwards
        double sign = Math.signum(distance);
        distance = Math.abs(distance);
        // penguin walks, each step being 0.2m
        while (distance > 0) {
            position.moveBy(sign * Math.min(distance, 0.2), direction);
            world.resolveCollision(this, position);
            distance -= 0.2;
        }
        return true;
    }

    public boolean turnBy(double deltaDirection) {
        direction += deltaDirection;
        return true;
    }

    public boolean turnTo(double newDirection) {
        direction = newDirection;
        return true;
    }

    public boolean paintWorld(Position pos, char blockType) {
        world.setTerrain(pos, blockType);
        return true;
    }

    public void work(){
        if (todo.isEmpty()){
            sense();
            think();
        }
        act();
    }

    private void sense(){
        for (var sens : sensors){
            processSensor(sens);
        }
    }

    private <T> void processSensor(Sensor<T> sensor){
        var data = sensor.getData();
        sensor.processor.accept(data);
    }

    private void think(){
        todo.addAll(program.apply(this));
    }

    private void act(){
        while (!todo.isEmpty()){
            if (!todo.poll().execute(this)){
                break;
            }
        }
    }

    public <T> void attachSensor(Sensor<T> sensor){
        sensors.add(sensor);
        sensor.setOwner(this);
    }

    public <T> Memory<T> createMemory(Memory<T> newMemory){
        memory.add(newMemory);
        return newMemory;
    }

    /// Getters and Setters
    public String getName() {
        return name;
    }

    public double getSize() {
        return size;
    }

    public Position getPosition() {
        return new Position(position);
    }

    public int getRoundedX() {
        return (int) Math.floor(position.x);
    }

    public int getRoundedY() {
        return (int) Math.floor(position.y);
    }

    public double getDirection() {
        return direction;
    }

    public LinkedList<Position> getTrailPositions() {
        return trailPositions;
    }

    public World getWorld() {
        return world;
    }

    public Color getColor() {
        return color;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setProgram(Function<Robot, List<Command>> program) {
        this.program = program;
    }

    public void spawnInWorld(World world, char spawnMarker) {
        this.world = world;
        this.position = new Position(world.spawnRobotAt(this, spawnMarker));
    }

    public boolean death(){
        if (!noDeath.contains(Character.toString(world.getTerrain(position)))) {
            isDead = true;
            return isDead;
        }
        return false;
    }

    @Override
    public String toString() {
        return "\"" + name + "\" position=" + position + " direction="
                + (((int) (Math.toDegrees(direction) * 100) / 100.0));
    }

    public static interface FailureSimulator {
        double getNextRandom();
        double getNextRandomBetween(double from, double to);
        int getNextRandomInt(int from, int to);
    }

    /**
     * Mainly used for RNG
     */
    public static class SimpleFailureSimulator implements FailureSimulator {

        private Random random;

        public SimpleFailureSimulator(int seed) {
            this.random = new Random(seed);
        }

        public SimpleFailureSimulator(){
            this.random = new Random();
        }

        @Override
        public double getNextRandom() {
            return random.nextDouble();
        }

        @Override
        public double getNextRandomBetween(double from, double to) {
            int n = random.nextInt(0, 2);
            return n == 0 ? Math.PI / 2 : -(Math.PI / 2);
        }

        @Override
        public int getNextRandomInt(int from, int to) {
            return random.nextInt(from, to);
        }
    }
}
