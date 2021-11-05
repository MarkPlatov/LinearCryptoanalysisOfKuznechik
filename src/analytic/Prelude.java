package analytic;

import kuznechik.Builder;
import kuznechik.Crypt;
import tests.Validation;
import utils.CryptPair;

import java.util.Arrays;
import java.util.Vector;

import static utils.Constants.*;


class cryptThread extends Thread {
	public byte[][] entries;
	
	public cryptThread(){
		entries = Builder.buildAllEntriesTwoNotNullBlock((byte) 0x00);
	}
	public cryptThread(byte[][] entries){
		this.entries = entries;
	}
	
	@Override
	public void run() {
		System.out.println("Запущен поток  " + getName());
		Prelude.processEncForGoodOuts(entries, 1, (byte)0x00);
		System.out.println("Завершён поток " + getName());
	}
}

public class Prelude {
	
	
	/**
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void twoBlockNotNullGoodMultipleTr(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		int treads = 12;
		byte[][] entries = Builder.buildAllEntriesTwoNotNullBlock((byte)0x00);
		int entriesPartsLen = entries.length / treads;
		byte[][][] entriesParts = new byte[treads][entriesPartsLen][BLOCK_SIZE];
		for (int i = 0; i < treads; i++) {
			System.arraycopy(entries, i * entriesPartsLen, entriesParts[i], 0, entriesPartsLen );
		}
		for (int i = 0; i < treads; i++) {
			(new cryptThread(entriesParts[i])).start();
		}
	}
	
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
		
		return processEncForGoodOutsWithKeys(
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
	public static Vector<CryptPair> oneBlockNotNullGood(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		byte[][] entries = Builder.buildAllEntriesOneNotNullBlock();
		
		return processEncForGoodOutsPerRounds(
				entries,
				new byte[][][]{iterKey},
				numberOfRounds,
				verbose,
				goodByte);
	}
	public static Vector<CryptPair> oneBlockNotNullEq(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		byte[][] entries = Builder.buildAllEntriesOneNotNullBlock();
		
		return processEncForEqualOutsPerRounds(
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
	public static Vector<CryptPair> twoBlockNotNullGood(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		byte[][] entries = Builder.buildAllEntriesTwoNotNullBlock((byte)0x00);

		return processEncForGoodOuts(
				entries,
//				iterKey,
				numberOfRounds,
				goodByte
		);
	}
	
	/**
	 * Дешифрует всевозможные выходы с ОДНИМ и ДВУМЯ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 **/
	public static Vector<CryptPair> oneTwoBlockNotNullGoodDecrypt(int numberOfRounds, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		byte[][] oneBlock = Builder.buildAllEntriesOneNotNullBlock();
		byte[][] twoBlock = Builder.buildAllEntriesTwoNotNullBlock((byte)0x00);
		byte[][] entries = new byte[oneBlock.length + twoBlock.length][oneBlock[0].length];
		System.arraycopy(oneBlock, 0, entries, 0, oneBlock.length);
		System.arraycopy(twoBlock, 0, entries, oneBlock.length, twoBlock.length);
		
		return processDecryptForGoodOuts(
				entries,
				new byte[][][]{iterKey},
				numberOfRounds,
				goodByte
		);
	}
	
	public static Vector<CryptPair> twoBlockNotNullEq(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		});
		byte[][] entries = Builder.buildAllEntriesTwoNotNullBlock((byte)0x00);
		
		return processEncForEqualOutsPerRounds(
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
	public static Vector<CryptPair> processEncForGoodOutsWithKeys(
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
	
	public static Vector<CryptPair> processEncForGoodOuts(
			byte[][] entries,
//			byte[][] iterKeys,
			int numberOfRounds,
			byte goodByte
	){
		byte[] tmpOut;
//		int numberOfIterKeys = iterKeys.length;
		int numberOfEntries = entries.length;
		
		Vector<CryptPair> goodOuts = new Vector<>();
		Crypt kusnechik = new Crypt();
		Validation validation = new Validation(goodByte);
		
		
		System.out.println("Encryption for " +
				numberOfEntries +
				" entries with zero key in " +
				numberOfRounds +
				" rounds...");
		
		int i = 0;
		int goodBytes;
		String prefix = "_test1_";
		String[] fileName = {
				String.format("%s%s_0_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_1_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_2_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_3_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_4_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_5_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_6_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_7_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_8_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_9_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_10_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_11_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_12_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_13_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_14_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_15_0x00.txt", FILE_NAME, prefix),
				String.format("%s%s_16_0x00.txt", FILE_NAME, prefix),
		};
		
		byte[][] iterKeys = {{0x00}};
		
//		kusnechik.setIterKey(iterKeys);
		for (byte[] entry : entries) {
			i ++;
			tmpOut = kusnechik.encryptOneRoundZeroKey(entry);
			goodBytes = validation.goodBytesInArr(tmpOut);
			
			if (goodBytes > 3) {
				CryptPair goodOut = new CryptPair(
						entry,
						entry,
						Arrays.copyOf(tmpOut, tmpOut.length),
						numberOfRounds,
						iterKeys
				);
				goodOuts.add(goodOut);
				goodOut.addToFile(fileName[goodBytes]);
			}
			
			if (i % 1000000 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
		return goodOuts;
	}
	
	public static Vector<CryptPair> processDecryptForGoodOuts(
			byte[][] entries,
			byte[][][] iterKeys,
			int numberOfRounds,
//			boolean verbose,
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
				tmpOut = kusnechik.decrypt(entry, numberOfRounds);
				goodBytes = validation.goodBytesInArr(tmpOut);
				if (goodBytes > 2) {
					CryptPair goodOut = new CryptPair(
							Arrays.copyOf(tmpOut, tmpOut.length),
							null,
							entry,
							numberOfRounds,
							iterKey
					);
					goodOuts.add(goodOut);
					fileName = String.format("%s_Decrypt_0x00_%s.txt", FILE_NAME, goodBytes);
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
			if (i % 100000 == 0) {
				System.out.println(i + " of " + entries.length + " entries processed");
			}
		}
		System.out.println("Encryption done");
		return goodOuts;
	}
	public static Vector<CryptPair> processEncForEqualOutsPerRounds(
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
		int eqBytes;
		String fileNameMask = String.format("%s_equal_", FILE_NAME);
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
					eqBytes = validation.equalBytesInArr(tmpOut[j]);
					if (eqBytes > 2) {
						CryptPair goodOut = new CryptPair(
								entry,
								Arrays.copyOf(tmpOut[j - 1], tmpOut[j - 1].length),
								Arrays.copyOf(tmpOut[j], tmpOut[j].length),
								j,
								iterKey
						);
						goodOuts.add(goodOut);
						fileName = String.format("%s%s.txt", fileNameMask, eqBytes);
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
