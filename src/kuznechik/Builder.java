package kuznechik;

import utils.EntriesBuildState;
import utils.Utils;
import java.util.Arrays;

public class Builder {
	public static int BLOCK_SIZE = Crypt.BLOCK_SIZE;
	
	public static byte[][] get10EqualIterKeysFromKey(byte[] key){
		byte[][] iterKeys = new byte[10][BLOCK_SIZE];
		
		if (key.length != BLOCK_SIZE) {
			System.err.println("Invalid required iter key length: " + key.length);
			return iterKeys;
		}
		
		Arrays.fill(iterKeys, key);
		return iterKeys;
	}
	
	public static byte[][] buildSequencesOneNotNullBlock(){
		int numberOfVariants = 4080; //2^12 - BLOCK_SIZE (== 16)
		byte[][] out = new byte[numberOfVariants][BLOCK_SIZE];
		byte notNullBlock = 0x01;
		int currentVariant = 0;
		do {
			for (int i = 0; i < BLOCK_SIZE; i ++) out[currentVariant * BLOCK_SIZE + i][i] = notNullBlock;
			
			notNullBlock += 0x01;
			currentVariant ++;
		} while (notNullBlock != (byte) 0x00);
		return out;
	}
	
	public static byte[][] buildSequencesTwoNotNullBlock(){
		byte nullBlock = 0x00;
		int places = 120;
		int pairsOnPlacesVariants = 7803000; // places * pairs = 7803000;
		byte[][] out = new byte[pairsOnPlacesVariants][BLOCK_SIZE];
		byte firstNotNullBlock = 0x01;
		byte secondNotNullBlock = 0x01;
		int currentPair = 0;
		int currentPlace = 0;

		do {               // Цикл по
			do {           // парам байт
				for (int i = 0; i < BLOCK_SIZE - 1; i++) {      // Цикл по размещениям
					for (int j = i + 1; j < BLOCK_SIZE; j++) {  // пар байт
						Arrays.fill(out[currentPair * places + currentPlace], nullBlock);
						out[currentPair * places + currentPlace][i] = firstNotNullBlock;
						out[currentPair * places + currentPlace][j] = secondNotNullBlock;
						currentPlace++;
					}
				}
				currentPlace = 0;
				currentPair ++;
				secondNotNullBlock += 0x01;
			} while (secondNotNullBlock != (byte) 0x00);
			firstNotNullBlock += 0x01;
			secondNotNullBlock = 0x01;
		} while (firstNotNullBlock != (byte) 0x00);
		
		return out;
	}
	
	public static EntriesBuildState buildSequencesThreeNotNullBlock(EntriesBuildState in){
		byte nullBlock = 0x00;
		int arrSize = 25000000; // Длина массива, не убивающая память
		byte[][] outArr = new byte[arrSize][BLOCK_SIZE];
		EntriesBuildState out = EntriesBuildState.defaultState();
		byte a = in.a;
		byte b = in.b;
		byte c = in.c;
		int currentIndex = 0;
		boolean firstRun = true;
		String log;
		
		do {               // Цикл по
			do {           // парам
				do {       // байт
					
					
					for (int i = firstRun ? in.i : 0; i < BLOCK_SIZE - 2; i++) {            // Цикл по
						for (int j = firstRun ? in.j : 	i + 1; j < BLOCK_SIZE - 1; j++) {   // размещениям
							for (int k = firstRun ? in.k : j + 1; k < BLOCK_SIZE; k++) {    // пар байт
								
								firstRun = false;
								if (
										a == (byte)0xff &&
										b == (byte)0xff &&
										c == (byte)0xff &&
										i == 13 &&
										j == 14 &&
										k == 15
								) {
									out = EntriesBuildState.finalState(outArr);
									return out;
								}
								if (currentIndex == arrSize){
									log = String.format(
											"\na = %#x\nb = %#x\nc = %#x\ni = %d\nj = %d\nk = %d\ncurrentIndex = %d\n",
											a, b, c, i, j, k, currentIndex
									);
									Utils.log(log);
									out = new EntriesBuildState(a, b, c, i, j, k, outArr);
									return out;
								}

								Arrays.fill(outArr[currentIndex], nullBlock);
								outArr[currentIndex][i] = a;
								outArr[currentIndex][j] = b;
								outArr[currentIndex][k] = c;
								currentIndex ++;
								
							}
						}
					}
					
					c += 0x01;
				} while (c != (byte) 0x00);
				b += 0x01;
				c = 0x01;
			} while (b != (byte) 0x00);
			a += 0x01;
			b = 0x01;
		} while (a != (byte) 0x00);
		
		return out;
	}
	
	public static byte[][] buildEntriesFirstBlockNotNull(){
		int numberOfVariants = 255;
		byte[][] out = new byte[numberOfVariants][BLOCK_SIZE];
		byte notNullBlock = 0x00;
		byte nullBlock = (byte) 0x00;
		int currentVariant = 0;
		do {
			if (notNullBlock == nullBlock) {
				notNullBlock += 0x01;
				continue;
			}

			Arrays.fill(out[currentVariant], nullBlock);
			out[currentVariant][0] = notNullBlock;

			notNullBlock += 0x01;
			currentVariant ++;
		} while (notNullBlock != (byte) 0x00);
		return out;
	}
	
	public static byte[][] genRandKeys(int numberOfKeys){
		if (numberOfKeys < 1) {
			System.err.println("Invalid key gen request");
			return new byte[1][BLOCK_SIZE];
		}
		
		byte[][] keys = new byte[numberOfKeys][BLOCK_SIZE];
		for (int i = 0; i < numberOfKeys; i ++){
			for (int j = 0; j < BLOCK_SIZE; j ++){
				keys[i][j] = Utils.getRandByte();
			}
		}
		
		return keys;
	}
}
