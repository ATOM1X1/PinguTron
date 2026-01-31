package pgdp.robot;

/**
 * Interface that allows to load a program for a penguin to execute
 */
public interface Command {
    boolean execute(Robot robot);
}
