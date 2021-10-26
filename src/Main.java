import tests.Duration;
import utils.Constants;

public class Main {
	public static final int BLOCK_SIZE = Constants.BLOCK_SIZE;
	public static final String FILE_NAME = Constants.FILE_NAME;
	
	
	public static void main (String[] args) {
//		Duration.oneBlockFirstNotNull(1, true);
//		Duration.oneBlockNotNull(1, false);
//			System.out.println(	65025 * 119);
//		Builder.buildAllEntriesTwoNotNullBlock((byte)0x00);
		Duration.twoBlockNotNull(1, false, (byte) 0x00);
//		Duration.oneBlockFirstNotNull(9, false, (byte) 0xa5);
//		Duration.oneBlockNotNull(9, false, (byte) 0x00);
	}
	
	

}
