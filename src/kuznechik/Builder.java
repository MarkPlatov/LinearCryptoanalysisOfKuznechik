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
	
	public static byte[][] buildEntriesOneNotNullBlock(){
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
