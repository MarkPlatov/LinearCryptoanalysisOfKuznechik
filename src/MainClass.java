import tests.Duration;
import utils.Constants;

public class MainClass {
	public static final int BLOCK_SIZE = Constants.BLOCK_SIZE;
	public static final String FILE_NAME = Constants.FILE_NAME;
	
	
	public static void main (String[] args) {
		byte zero = 0x00;
		boolean verbose = false;
		int rounds = 1;
		
//		Duration.oneBlockNotNull(1, false,zero);
//		Duration.twoBlockNotNull(1, false, (byte) 0x00);
		long start, stop = 0;
		start = System.currentTimeMillis();
		Duration.twoBlockNotNullMultTreads(rounds, verbose, zero);
		Duration.testStop(start, stop);
//		byte[][] a = Builder.buildAllEntriesThreeNotNullBlock(zero);
//		Duration.allTests(9, false, (byte) 0x00);
//		Duration.oneTwoDecryptTests(1, (byte) 0x00);
//		Duration.oneBlockNotNull(9, false, (byte) 0x00);
	}
	
	

}
