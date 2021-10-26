package kuznechik;

import utils.Utils;

import java.util.Arrays;

public class Builder {
	public static int BLOCK_SIZE = Crypt.BLOCK_SIZE;
	
	public static byte[][] get10EqualIterKeysFromKey(byte[] key){
		byte[][] iterKeys = new byte[10][BLOCK_SIZE];
		int l = key.length;
		
		if (l != BLOCK_SIZE) {
			System.err.println("Invalid required iter key length: " + l);
			return iterKeys;
		}
		
		Arrays.fill(iterKeys, key);
		return iterKeys;
	}
	
	public static byte[][] buildAllEntriesOneNotNullBlock(){
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
	
	public static byte[][] buildAllEntriesTwoNotNullBlock(byte nullBlock){
		int places = 120; // 15 + 14 + 13 + 12 + 11 + 10 + 9 + 8 + 7 + 6 + 5 + 4 + 3 + 2 + 1= 120;
		int pairs = 65025; // 255 * 255 = 65025;
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
	
	public static byte[][] buildEntriesFirstBlockNotNull(){
		int numberOfVariants = 255; //2^12 - BLOCK_SIZE (== 16)
		byte[][] out = new byte[numberOfVariants][BLOCK_SIZE];
		byte notNullBlock = 0x00;
//		byte nullBlock = (byte) 0xa5;
		byte nullBlock = (byte) 0x00;
		int currentVariant = 0;
		do {
			if (notNullBlock == nullBlock) {
				notNullBlock += 0x01;
				continue;
			}
//			for (int i = 0; i < BLOCK_SIZE; i ++) out[currentVariant * BLOCK_SIZE + i][i] = notNullBlock;
			out[currentVariant][0] = notNullBlock;
			out[currentVariant][1] = nullBlock;
			out[currentVariant][2] = nullBlock;
			out[currentVariant][3] = nullBlock;
			out[currentVariant][4] = nullBlock;
			out[currentVariant][5] = nullBlock;
			out[currentVariant][6] = nullBlock;
			out[currentVariant][7] = nullBlock;
			out[currentVariant][8] = nullBlock;
			out[currentVariant][9] = nullBlock;
			out[currentVariant][10] = nullBlock;
			out[currentVariant][11] = nullBlock;
			out[currentVariant][12] = nullBlock;
			out[currentVariant][13] = nullBlock;
			out[currentVariant][14] = nullBlock;
			out[currentVariant][15] = nullBlock;
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
