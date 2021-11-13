package kuznechik;

import utils.EntriesBuildState;
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
	
	public static EntriesBuildState buildAllEntriesThreeNotNullBlock(byte nullBlock, EntriesBuildState in){
		int places = 560;
		int pairs = 255 * 255 * 255; // 255 * 255 = 65025;
		int arrSize = 25000000; // Длина массива, не убивающая память
		byte[][] outArr = new byte[arrSize][BLOCK_SIZE];
		EntriesBuildState out = EntriesBuildState.defaultState();
		byte a = in.a;
		byte b = in.b;
		byte c = in.c;
//		int i = in.i;
//		int j = in.j;
//		int k = in.k;
//		int currentPair = 0;
//		int currentPlace = 0;
//		int delta = 0;
		int currentIndex = 0;
		boolean firstRun = true;
		String log;
//		boolean firstRunDone = false;
		
		System.out.println("==============================================");
		do {               // Цикл по
			do {           // парам
				do {       // байт
					
					
					for (int i = firstRun ? in.i : 0; i < BLOCK_SIZE - 2; i++) {       // Цикл по
						for (int j = firstRun ? in.j : 	i + 1; j < BLOCK_SIZE - 1; j++) {   // размещениям
							for (int k = firstRun ? in.k : j + 1; k < BLOCK_SIZE; k++) {   // пар байт
								firstRun = false;
								
								
/*								if (
										a == (byte)0xff &&
										b == (byte)0xc4 &&
										c == (byte)0x30 &&
//										i == 3 &&
//										j == 7 &&
										currentIndex > arrSize - 500
								) {
									System.out.println("Hello first " + currentIndex);
									System.out.printf("a = %#X\n", a);
									System.out.printf("b = %#X\n", b);
									System.out.printf("c = %#X\n", c);
									System.out.println("i = " + i);
									System.out.println("j = " + j);
									System.out.println("k = " + k);
									System.out.println();
								}
								if (
										a == (byte)0xff &&
										b == (byte)0xc4 &&
										c >= (byte)0x30 &&
										i == 3 &&
										j >= 7 &&
										currentIndex < 500
								) {
									System.out.println("Hello second " + currentIndex);
									System.out.printf("a = %#X\n", a);
									System.out.printf("b = %#X\n", b);
									System.out.printf("c = %#X\n", c);
									System.out.println("i = " + i);
									System.out.println("j = " + j);
									System.out.println("k = " + k);
									System.out.println();
								}
								if (
										((a == (byte)0xff &&
										b == (byte)0xff &&
										c == (byte)0xff) ||
										a == (byte)0x00 )
//										i == 3 &&
//										j >= 7 &&
//										currentIndex < 500
								) {
									System.out.println("Hello ff " + currentIndex);
									System.out.printf("a = %#X\n", a);
									System.out.printf("b = %#X\n", b);
									System.out.printf("c = %#X\n", c);
									System.out.println("i = " + i);
									System.out.println("j = " + j);
									System.out.println("k = " + k);
									System.out.println();
								}*/
								
//								if (!firstRunDone && i == 13 && j == 14 && k == 15){
//									firstRunDone = true;
//									delta -= places - (currentPair * places + currentPlace);
//								}
//								if (currentPair * places + currentPlace > arrSize - 560 ||
//										currentPair * places + currentPlace < 1560
//								) {
//									if (currentIndex > arrSize - 560 ||
//										currentIndex < 1560
//								) {
//
////									System.out.println("Hello " + (currentPair * places + currentPlace));
//									System.out.println("Hello " + currentIndex);
//									System.out.printf("a = %#X\n", a);
//									System.out.printf("b = %#X\n", b);
//									System.out.printf("c = %#X\n", c);
//									System.out.println("i = " + i);
//									System.out.println("j = " + j);
//									System.out.println("k = " + k);
//									System.out.println();
//								}
//								Контроль переполнения
					/*			if (currentPair * places + currentPlace == arrSize){*/
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
									System.out.println(log);
									Utils.log(log);
									out = new EntriesBuildState(a, b, c, i, j, k, outArr);
									return out;
								}

//								Arrays.fill(outArr[currentPair * places + currentPlace], nullBlock);
//								outArr[currentPair * places + currentPlace][i] = a;
//								outArr[currentPair * places + currentPlace][j] = b;
//								outArr[currentPair * places + currentPlace][k] = c;
								Arrays.fill(outArr[currentIndex], nullBlock);
								outArr[currentIndex][i] = a;
								outArr[currentIndex][j] = b;
								outArr[currentIndex][k] = c;
					/*			currentPlace++;*/
								currentIndex ++;
								
								
							}
						}
					}
					/*currentPlace = 0;
					currentPair ++;*/
//					System.out.println(currentPair);
					
					c += 0x01;
				} while (c != (byte) 0x00);
				b += 0x01;
				c = 0x01;
			} while (b != (byte) 0x00);
			a += 0x01;
			b = 0x01;
		} while (a != (byte) 0x00);
		
		return out;
//		return new byte[][]{{}};
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
