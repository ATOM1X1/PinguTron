package pgdp.robot;

 import javax.sound.sampled.*;

 import java.util.Random;

 import static pgdp.robot.RobotFactory.*;

/**
 * Main class
 */
public class User {
    /**
     * Gameworld is a string that is rendered in a JFrame.
     * Each character from the string is replaced with a corresponding
     * picture from the img folder in the World class. The numbers
     * identify where each robot spawns.
     */
	private static final String PLAY_GROUND_1 = """
			############################################################################################################################################
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                2                                                                                                     4                   #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#        0                                                                                                             5                   #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                1                                                                                                     3                   #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			#                                                                                                                                          #
			############################################################################################################################################
			""";
	public static void main(String[] args) {
		createExample();
	}

    /**
     * World and Robots are created here
     **/
	public static void createExample() {
		World world = new World(PLAY_GROUND_1);
        Random rand = new Random();
        int[] nums = rand.ints(20, 1, 6).distinct().toArray();

        makeTron("Tron", new Robot.SimpleFailureSimulator()).spawnInWorld(world, '0');
        makeUser(Color.ORANGE, nums[0] != 5 ? nums[0] % 2 == 1 ? -Math.PI/2 : Math.PI/2 : Math.PI, new Robot.SimpleFailureSimulator()).spawnInWorld(world, Character.forDigit(nums[0], 10));
        makeUser(Color.RED, nums[1] != 5 ? nums[1] % 2 == 1 ? -Math.PI/2 : Math.PI/2 : Math.PI, new Robot.SimpleFailureSimulator()).spawnInWorld(world, Character.forDigit(nums[1], 10));
        makeUser(Color.WHITE, nums[2] != 5 ? nums[2] % 2 == 1 ? -Math.PI/2 : Math.PI/2 : Math.PI, new Robot.SimpleFailureSimulator()).spawnInWorld(world, Character.forDigit(nums[2], 10));
        makeUser(Color.YELLOW, nums[3] != 5 ? nums[3] % 2 == 1 ? -Math.PI/2 : Math.PI/2 : Math.PI, new Robot.SimpleFailureSimulator()).spawnInWorld(world, Character.forDigit(nums[3], 10));
        makeUser(Color.PURPLE, nums[4] != 5 ? nums[4] % 2 == 1 ? -Math.PI/2 : Math.PI/2 : Math.PI, new Robot.SimpleFailureSimulator()).spawnInWorld(world, Character.forDigit(nums[4], 10));
		world.run();
	}
}
