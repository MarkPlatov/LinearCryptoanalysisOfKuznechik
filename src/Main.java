import kuznechik.Builder;
import kuznechik.Crypt;
import utils.CryptPair;
import utils.Utils;

import java.util.Arrays;
import java.util.Vector;

public class Main {
	public static final int BLOCK_SIZE = Crypt.BLOCK_SIZE;
	public static final String FILE_NAME = "D://Mark//Development//GraduateWork//txt//GoodOuts.txt";
	
	
	public static void main (String[] args) {
		oneBlockFullTimeTest();
//		boolean b = isOutArrGood(new byte[]{
//				(byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
//				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
//		});
//		System.out.println(b);
	}
	
	public static Vector<CryptPair> oneBlockFull(int numberOfKeys, int numberOfRounds) {
		System.out.println("Initialisation...");
		byte[][] keys = Builder.genRandKeys(numberOfKeys);
		byte[][][] iterKeys = new byte[numberOfKeys][10][BLOCK_SIZE];
		for (int i = 0; i < numberOfKeys; i ++) iterKeys[i] = Builder.get10EqualIterKeysFromKey(keys[i]);
		
		byte[][] entries = Builder.buildEntriesOneNotNullBlock();
		byte[] tmpOut;
		
		Vector<CryptPair> goodOuts = new Vector<>();
		Crypt kusnechik = new Crypt();

		System.out.println("Initialisation done");
		System.out.println("Encryption for " +
				numberOfKeys +
				" keys in " +
				numberOfRounds +
				" rounds...");
		
		int i = 0;
		for (byte[] entry : entries) {
			i ++;
			for (byte[][] iterKey : iterKeys) {
				kusnechik.setIterKey(iterKey);
				tmpOut = kusnechik.encrypt(entry, 2);
				if (isOutArrGood(tmpOut)) {
					CryptPair goodOut = new CryptPair(
							entry,
							Arrays.copyOf(tmpOut, tmpOut.length),
							numberOfRounds,
							iterKey
					);
					goodOuts.add(goodOut);
					goodOut.addToFile(FILE_NAME);
				}
			}
			if (i % 100 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
		return goodOuts;
	}
	
	
	static void oneBlockFullTimeTest(){
		long start, stop = 0;
		double duration;
		
		int numberOfKeys = 40000;
		int numberOfRounds = 2;
		
		start = System.currentTimeMillis();
		Vector<CryptPair> goodOuts = oneBlockFull(numberOfKeys, numberOfRounds);
		
		System.out.println("Priting \"Good\" outs to " + FILE_NAME + " :");
		for (CryptPair cp : goodOuts) {
			System.out.println(goodOuts);
//			cp.addToFile(FILE_NAME);
		}
		
		testStop(start, stop);
	}
	
	static void keyGenerationTimeTest(){
		long start, stop = 0;
		double duration;
		
		byte[][] out;
		
		start = System.currentTimeMillis();
		out = Builder.genRandKeys(40000);
		testStop(start, stop);
		Utils.printArrayOfHexArrays(Arrays.copyOfRange(out, out.length - 1000, out.length));
		
	}
	
	static void oneBlockEncTimeTest() {
		long start, stop = 0;
		double duration;
		byte[][] out;
		
		start = System.currentTimeMillis();
		out = oneBlockEnc();
		testStop(start, stop);
		Utils.printArrayOfHexArrays(Arrays.copyOfRange(out, out.length - 1000, out.length));
		
	}
	
	
	private static void testStop(long start, long stop) {
		stop = System.currentTimeMillis();
		double duration;
		duration = stop - start;
		
		System.out.println("Test takes " + duration + " milliseconds");
		System.out.println("Test takes " + duration / 1000 + " seconds");
		System.out.println("Test takes " + duration / 1000 / 60 + " minutes");
	}
	
	static byte[][] oneBlockEnc(){
		byte[][] entriesOneBlock = Builder.buildEntriesOneNotNullBlock();
		byte[][] outsFromOneBlock = new byte[entriesOneBlock.length][BLOCK_SIZE];
		byte[] iterKey = Arrays.copyOfRange(Utils.EXAMPLE_KUZNECHIK_KEY, 0, BLOCK_SIZE);
		byte[][] iterKeys = Builder.get10EqualIterKeysFromKey(iterKey);
		
		Crypt kuznechik = new Crypt();
		kuznechik.setIterKey(iterKeys);
		
		int i = 0;
		for (byte[] a : entriesOneBlock){
			outsFromOneBlock[i] = kuznechik.encrypt(a, 2);
			i ++;
		}
		
//		System.out.println(Utils.byteArrToHexStr(outsFromOneBlock[44]));
		return outsFromOneBlock;
	}
	static boolean isOutArrGood(byte[] out){
		int notNullBlocksCounter = BLOCK_SIZE;
		for (byte b : out) { if (b == 0x00) notNullBlocksCounter --; }
		return notNullBlocksCounter < 4;
	}
}
