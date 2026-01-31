package pgdp.robot;

import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class sets up robots and their logic. Each robot has proximity sensors
 * and memory units. Proximity sensors measure the distance to the nearest obstacles,
 * which is then stored in a dedicated memory unit. This way robots can
 * avoid danger. The "AI" is handled by the program of a Robot which
 * is a list of Command objects.
 */
public class RobotFactory {
    private static String dontReplace = "012345";

    public static Robot makeTron(String name, Robot.FailureSimulator failureSimulator){
        Robot mainPingu = new Robot(name, 0, 0.8, Color.BLUE, failureSimulator);
        ControllerReceiver receiver = new ControllerReceiver("RC", 0.97);
        Memory<World.Controller> mem = new Memory<>("mem0", new World.PhysicalController(mainPingu.getWorld()));
        mainPingu.createMemory(mem);
        receiver.setProcessor(mem::set);
        mainPingu.attachSensor(receiver);
        AtomicBoolean goW = new AtomicBoolean(false);
        AtomicBoolean goA = new AtomicBoolean(false);
        AtomicBoolean goS = new AtomicBoolean(false);
        AtomicBoolean goD = new AtomicBoolean(true);
        mainPingu.setProgram(robot -> {
            List<Command> comms = new ArrayList<>();
            comms.add(x -> {

                // Makes the trail disappear after a certain amount of rendered blocks
                var oldPos = robot.getPosition();
                if (robot.getTrailPositions().size() >= 35){
                    robot.getWorld().setTerrain(robot.getTrailPositions().pollFirst(), ' ');
                }

                if (!dontReplace.contains(Character.toString(robot.getWorld().getTerrain(robot.getPosition())))) {
                    robot.getTrailPositions().add(oldPos);
                }
                if (x.getWorld().getController().isKeyPressed(KeyEvent.VK_W) && !goS.get()) {
                    goW.set(true);
                    goA.set(false);
                    goS.set(false);
                    goD.set(false);
                }
                else if (x.getWorld().getController().isKeyPressed(KeyEvent.VK_A) && !goD.get()) {
                    goW.set(false);
                    goA.set(true);
                    goS.set(false);
                    goD.set(false);
                }
                else if (x.getWorld().getController().isKeyPressed(KeyEvent.VK_S) && !goW.get()) {
                    goW.set(false);
                    goA.set(false);
                    goS.set(true);
                    goD.set(false);
                }
                else if (x.getWorld().getController().isKeyPressed(KeyEvent.VK_D) && !goA.get()) {
                    goW.set(false);
                    goA.set(false);
                    goS.set(false);
                    goD.set(true);
                }
                if(goW.get()) {
                    x.turnTo(3 * Math.PI/2);
                    x.go(1);
                    if (!dontReplace.contains(Character.toString(robot.getWorld().getTerrain(oldPos))))
                        x.getWorld().setTerrain(oldPos, 'b');
                }
                if(goA.get()) {
                    x.turnTo(Math.PI);
                    x.go(1);
                    if (!dontReplace.contains(Character.toString(robot.getWorld().getTerrain(oldPos))))
                        x.getWorld().setTerrain(oldPos, 'b');
                }
                if(goS.get()) {
                    x.turnTo(Math.PI/2);
                    x.go(1);
                    if (!dontReplace.contains(Character.toString(robot.getWorld().getTerrain(oldPos))))
                        x.getWorld().setTerrain(oldPos, 'b');
                }
                if(goD.get()) {
                    x.turnTo(0);
                    x.go(1);
                    if (!dontReplace.contains(Character.toString(robot.getWorld().getTerrain(oldPos))))
                        x.getWorld().setTerrain(oldPos, 'b');
                }
                return true;
            });
            return comms;
        });
        return mainPingu;
    }

    public static Robot makeUser(Color color, double rotation, Robot.FailureSimulator failureSimulator){
        Robot user = new Robot("User", rotation, 0.8, color, failureSimulator);
        ProximitySensor front = new ProximitySensor("Front", 0, 0.5, 7, 1);
        ProximitySensor left = new ProximitySensor("Left", -(Math.PI / 2), 0.5, 7, 1);
        ProximitySensor right = new ProximitySensor("Right", Math.PI / 2, 0.5, 7, 1);
        user.attachSensor(front); user.attachSensor(left); user.attachSensor(right);
        Memory<Double> memFront = new Memory<>("Front", 0.0);
        Memory<Double> memLeft = new Memory<>("Left", 0.0);
        Memory<Double> memRight = new Memory<>("Right", 0.0);
        front.setProcessor(memFront::set); left.setProcessor(memLeft::set); right.setProcessor(memRight::set);
        user.createMemory(memFront); user.createMemory(memLeft); user.createMemory(memRight);
        List<Command> comms = new ArrayList<>();
        comms.add(x -> {
            var oldPos = user.getPosition();
            if (user.getTrailPositions().size() >= 35){
                user.getWorld().setTerrain(user.getTrailPositions().pollFirst(), ' ');
            }
            if (!dontReplace.contains(Character.toString(user.getWorld().getTerrain(user.getPosition())))) {
                user.getTrailPositions().add(oldPos);
            }
            if (memLeft.get() <= 2.0 && memFront.get() > 2.0){
                if (failureSimulator.getNextRandomInt(0, 30) == 0) {
                    user.turnBy(Math.PI / 2);
                }
            } else if (memRight.get() <= 2.0 && memFront.get() > 2.0){
                if (failureSimulator.getNextRandomInt(0, 30) == 0) {
                    user.turnBy(-Math.PI / 2);
                }
            } else if (memFront.get() <= 2.0){
                if (failureSimulator.getNextRandomInt(0, 15) != 0)
                    user.turnBy(failureSimulator.getNextRandomBetween(-(Math.PI / 2), Math.PI / 2));
            } else {
                if (failureSimulator.getNextRandomInt(0, 40) == 0){
                    user.turnBy(failureSimulator.getNextRandomBetween(-(Math.PI / 2), Math.PI / 2));
                }
            }
            user.go(1);
            if (!dontReplace.contains(Character.toString(user.getWorld().getTerrain(oldPos))))
                switch (color){
                    case RED -> user.getWorld().setTerrain(oldPos, 'r');
                    case WHITE -> user.getWorld().setTerrain(oldPos, 'w');
                    case ORANGE -> user.getWorld().setTerrain(oldPos, 'o');
                    case PURPLE -> user.getWorld().setTerrain(oldPos, 'p');
                    case YELLOW -> user.getWorld().setTerrain(oldPos, 'y');
                }
            return true;
        });
        user.setProgram(x -> comms);
        return user;
    }
}
