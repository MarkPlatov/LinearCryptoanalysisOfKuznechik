package analytic;

import kuznechik.Builder;
import kuznechik.Crypt;
import tests.Validation;
import utils.CryptPair;

import java.util.Arrays;
import java.util.Vector;

import static utils.Constants.*;

public class Prelude {
	
	
	/**
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует numberOfKeys СЛУЧАЙНЫХ ИТЕРАЦИОННЫХ КЛЮЧЕЙ.
	 * Возвращает массив ШТ
	 **/
	public static Vector<CryptPair> oneBlockFull(int numberOfKeys, int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] keys = Builder.genRandKeys(numberOfKeys);
		byte[][][] iterKeys = new byte[numberOfKeys][10][BLOCK_SIZE];
		for (int i = 0; i < numberOfKeys; i ++) iterKeys[i] = Builder.get10EqualIterKeysFromKey(keys[i]);
		
		byte[][] entries = Builder.buildAllEntriesOneNotNullBlock();
		
		return processEncForGoodOuts(
				entries,
				iterKeys,
				numberOfRounds,
				verbose,
				goodByte);
	}
	
	/**
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static Vector<CryptPair> firstBlockNotNull(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		
		byte[][] entries = Builder.buildEntriesFirstBlockNotNull();
		
/*		return processEncForGoodOuts(
				entries,
				new byte[][][]{iterKey},
				numberOfRounds,
				verbose,
				goodByte);*/
		return processEncForGoodOutsPerRounds(
				entries,
				new byte[][][]{iterKey},
				numberOfRounds,
				verbose,
				goodByte);
	}
	
	/**
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static Vector<CryptPair> oneBlockNotNull(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		byte[][] entries = Builder.buildAllEntriesOneNotNullBlock();
		
/*		return processEncForGoodOuts(
				entries,
				new byte[][][]{iterKey},
				numberOfRounds,
				verbose,
				goodByte);*/
		
		return processEncForGoodOutsPerRounds(
				entries,
				new byte[][][]{iterKey},
				numberOfRounds,
				verbose,
				goodByte);
	}
	
	/**
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static Vector<CryptPair> twoBlockNotNull(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		byte[][] entries = Builder.buildAllEntriesTwoNotNullBlock((byte)0x00);

/*		return processEncForGoodOuts(
				entries,
				new byte[][][]{iterKey},
				numberOfRounds,
				verbose,
				goodByte
		);*/
		return processEncForGoodOutsPerRounds(
				entries,
				new byte[][][]{iterKey},
				numberOfRounds,
				verbose,
				goodByte
		);
	}
	
	/**
	 * Шифрует всевозможные входы с одним ненулевым блоком
	 * на два раунда. Как итерационные ключи использует 1-ю половину
	 * эталонного ключа кузнечика. Возвращает массив ШТ
	**/
	public static byte[][] oneBlockEnc(){
		byte[][] entriesOneBlock = Builder.buildAllEntriesOneNotNullBlock();
		byte[][] outsFromOneBlock = new byte[entriesOneBlock.length][BLOCK_SIZE];
		byte[] iterKey = Arrays.copyOfRange(EXAMPLE_KUZNECHIK_KEY, 0, BLOCK_SIZE);
		byte[][] iterKeys = Builder.get10EqualIterKeysFromKey(iterKey);
		
		Crypt kuznechik = new Crypt();
		kuznechik.setIterKey(iterKeys);
		
		int i = 0;
		for (byte[] a : entriesOneBlock){
			outsFromOneBlock[i] = kuznechik.encrypt(a, 2);
			i ++;
		}
		
		return outsFromOneBlock;
	}
	
	/**
	 * Шифрует заданные входы всеми заданными ключами заданное кол-во раундов.
	 * Возвращает "хорошие" пары ОТ + ШТ
	**/
	public static Vector<CryptPair> processEncForGoodOuts(
			byte[][] entries,
			byte[][][] iterKeys,
			int numberOfRounds,
			boolean verbose,
			byte goodByte
	){
		byte[] tmpOut;
		int numberOfIterKeys = iterKeys.length;
		int numberOfEntries = entries.length;
		
		Vector<CryptPair> goodOuts = new Vector<>();
		Crypt kusnechik = new Crypt();
		Validation validation = new Validation(goodByte);
		
		
		System.out.println("Encryption for " +
				numberOfEntries +
				" entries with " +
				numberOfIterKeys +
				" keys in " +
				numberOfRounds +
				" rounds...");
		
		int i = 0;
		int goodBytes;
		String fileName;
		for (byte[] entry : entries) {
			i ++;
			for (byte[][] iterKey : iterKeys) {
				kusnechik.setIterKey(iterKey);
				if (verbose)
					tmpOut = kusnechik.encryptPrintAllSteps(entry, numberOfRounds);
				else
					tmpOut = kusnechik.encrypt(entry, numberOfRounds);
				 
				goodBytes = validation.goodBytesInArr(tmpOut);
				if (goodBytes > 2) {
					CryptPair goodOut = new CryptPair(
							entry,
							entry,
							Arrays.copyOf(tmpOut, tmpOut.length),
							numberOfRounds,
							iterKey
					);
					goodOuts.add(goodOut);
					fileName = String.format("%s%s.txt", FILE_NAME, goodBytes);
					goodOut.addToFile(fileName);
				}
			}
			if (i % 1000 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
		return goodOuts;
	}
	
	public static Vector<CryptPair> processEncForGoodOutsPerRounds(
			byte[][] entries,
			byte[][][] iterKeys,
			int numberOfRounds,
			boolean verbose,
			byte goodByte
	){
		byte[][] tmpOut;
		int numberOfIterKeys = iterKeys.length;
		int numberOfEntries = entries.length;
		
		Vector<CryptPair> goodOuts = new Vector<>();
		Crypt kusnechik = new Crypt();
		Validation validation = new Validation(goodByte);
		
		
		System.out.println("Encryption for " +
				numberOfEntries +
				" entries with " +
				numberOfIterKeys +
				" keys in " +
				numberOfRounds +
				" rounds...");
		
		int i = 0;
		int goodBytes;
		String fileNameMask = String.format("%s_%#X_", FILE_NAME, goodByte);
		String fileName;
		for (byte[] entry : entries) {
			i ++;
			for (byte[][] iterKey : iterKeys) {
				kusnechik.setIterKey(iterKey);
				if (verbose) {
					System.err.println("The is not possible to verbose this function now");
					return goodOuts;
				} else tmpOut = kusnechik.encryptPerRound(entry, numberOfRounds);
				
				for (int j = 1; j < tmpOut.length; j ++) {
					goodBytes = validation.goodBytesInArr(tmpOut[j]);
					if (goodBytes > 2) {
						CryptPair goodOut = new CryptPair(
								entry,
								Arrays.copyOf(tmpOut[j - 1], tmpOut[j - 1].length),
								Arrays.copyOf(tmpOut[j], tmpOut[j].length),
								j,
								iterKey
						);
						goodOuts.add(goodOut);
						fileName = String.format("%s%s.txt", fileNameMask, goodBytes);
						goodOut.addToFile(fileName);
					}
				}
			}
			if (i % 10000 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
		return goodOuts;
	}
}
