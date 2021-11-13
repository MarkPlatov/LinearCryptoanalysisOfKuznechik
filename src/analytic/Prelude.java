package analytic;

import kuznechik.Builder;
import kuznechik.Crypt;
import tests.Validation;
import utils.CryptPair;
import utils.EntriesBuildState;
import utils.Utils;

import java.util.Arrays;
import java.util.Vector;

import static utils.Constants.*;


class CryptThread extends Thread {
	public byte[][] entries;
	
	public CryptThread(){
		entries = Builder.buildAllEntriesTwoNotNullBlock((byte) 0x00);
	}
	public CryptThread(byte[][] entries){
		this.entries = entries;
	}
	
	@Override
	public void run() {
		System.out.println("Запущен поток  " + getName());
//		Prelude.processEncForGoodOuts(entries, 1, (byte)0x00);
		Prelude.processDecr(entries, 1, (byte)0x00);
		System.out.println("Завершён поток " + getName());
	}
}

public class Prelude {
	
	
	/**
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void threeBlockNotNullGoodMultipleTr(int numberOfRounds, boolean verbose, byte goodByte) {
		System.out.println("Initialisation...");
//		byte[][] iterKey = Builder.get10EqualIterKeysFromKey(new byte[]{
//				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
//				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
//		});
		int treadsNum = 12;
		CryptThread[] treads = new CryptThread[treadsNum];
		
//		byte[][] entries = Builder.buildAllEntriesTwoNotNullBlock((byte)0x00);
		
		EntriesBuildState state = Builder.buildAllEntriesThreeNotNullBlock((byte)0x00, EntriesBuildState.defaultState());
		
		while (!state.isStateFinal()){
			byte[][] entries = state.out;
			int entriesPartsLen = entries.length / treadsNum + 1;
			int delta = 0;
			byte[][][] entriesParts = new byte[treadsNum][entriesPartsLen][BLOCK_SIZE];
			
			for (int i = 0; i < treadsNum; i++) {
				if (i == treadsNum - 1) {
					delta = entriesPartsLen * treadsNum - entries.length;
				}
				System.arraycopy(
						entries,
						i * entriesPartsLen,
						entriesParts[i],
						0,
						entriesPartsLen - delta);
			}
	
			for (int i = 0; i < treadsNum; i++) {
				treads[i] = new CryptThread(entriesParts[i]);
				treads[i].start();
			}
			
			for (int i = 0; i < treadsNum; i++) {
				try {
					treads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			state = Builder.buildAllEntriesThreeNotNullBlock((byte) 0x00, state);
			System.out.println();
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

		return processEnc(
				entries,
//				iterKey,
				numberOfRounds,
				goodByte
		);
	}
	
	public static void interestingEntriesTest(){
		byte[][] entries1 =
				{
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0d, (byte) 0x00, (byte) 0xec, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xaa, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1d, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0x00, (byte) 0xdd, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc9, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x28, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf1, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x47, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xee},
						{(byte) 0x33, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4b},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x34, (byte) 0x9a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb2},
						{(byte) 0x37, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xd8, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x54, (byte) 0x00, (byte) 0x86, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x67, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf2},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4b, (byte) 0x33},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x71, (byte) 0x4f, (byte) 0x00, (byte) 0x00, (byte) 0xae, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x77, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xfe, (byte) 0x00, (byte) 0x00},
						{(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x67, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x8b, (byte) 0x00, (byte) 0x59, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xa6, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x91, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x47, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x9e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc1, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0xc0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x5a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0xc1, (byte) 0x00, (byte) 0xa7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xcc, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0xd4, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xde, (byte) 0x00, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xd8, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93, (byte) 0x00, (byte) 0x83},
						{(byte) 0x00, (byte) 0x00, (byte) 0xdb, (byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8c},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe3, (byte) 0x39, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x19, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe8, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb7, (byte) 0x00, (byte) 0x60, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xed, (byte) 0x00, (byte) 0x15, (byte) 0x00, (byte) 0x84, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0xf5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x35, (byte) 0x4c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
				};
		
		byte[][] entries2 =
				{
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xbb, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0c, (byte) 0x0e, (byte) 0xea, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x46, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x13, (byte) 0x67, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x2d, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x17, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb9, (byte) 0x00, (byte) 0xd4, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x5c, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x1b, (byte) 0x00, (byte) 0x5b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x1d, (byte) 0x00, (byte) 0x00, (byte) 0x66, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x26, (byte) 0x00, (byte) 0x82, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x40, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x26, (byte) 0x00, (byte) 0x00, (byte) 0xfe, (byte) 0x00, (byte) 0x3a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2c, (byte) 0x97, (byte) 0x00, (byte) 0x83},
						{(byte) 0x00, (byte) 0x2c, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xdb, (byte) 0x00, (byte) 0xd9, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x36, (byte) 0xb3, (byte) 0x00, (byte) 0xcb, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x3e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xfd, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x42, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xd4},
						{(byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d, (byte) 0x00, (byte) 0x32},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5c, (byte) 0xd1, (byte) 0x00, (byte) 0x00, (byte) 0xb3, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x41, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe4, (byte) 0x00, (byte) 0x00, (byte) 0xd7},
						{(byte) 0x00, (byte) 0x5f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x65, (byte) 0xbd, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc6, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x66, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xed},
						{(byte) 0x00, (byte) 0x00, (byte) 0x6b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x7b, (byte) 0x00, (byte) 0x1e, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x73, (byte) 0x00, (byte) 0xa3, (byte) 0x45, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x75, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2c, (byte) 0x97, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xd4, (byte) 0x66},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x96, (byte) 0x00, (byte) 0x38, (byte) 0x00, (byte) 0x85, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x98, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x51, (byte) 0x00, (byte) 0x95, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x9a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x22, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x8e},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x99, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xfd, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb5},
						{(byte) 0x00, (byte) 0x00, (byte) 0xa5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xe7, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xab, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2e, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x6b, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x49, (byte) 0x00, (byte) 0xbb, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb7, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x9c, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0x00},
						{(byte) 0xb8, (byte) 0x42, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x90, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb9, (byte) 0x00, (byte) 0x00, (byte) 0x95, (byte) 0x00, (byte) 0x2b, (byte) 0x00},
						{(byte) 0x00, (byte) 0xba, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x23, (byte) 0x00, (byte) 0x00, (byte) 0x3d, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0xbf, (byte) 0x00, (byte) 0x00, (byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5d, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc2, (byte) 0x2b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xb4, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc4, (byte) 0x00, (byte) 0x24, (byte) 0xcc, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc8, (byte) 0x00, (byte) 0x00, (byte) 0x68, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x50, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xda, (byte) 0x8e, (byte) 0x00, (byte) 0x00, (byte) 0x34, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xf7, (byte) 0x00, (byte) 0x64, (byte) 0x48, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
						{(byte) 0x00, (byte) 0xfe, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00},
				};
		
		byte[][] bufferOut;
		Crypt kuz = new Crypt();
		kuz.setIterKey(Builder.get10EqualIterKeysFromKey(Crypt.ZERO_ROUND_KEY));
		Validation v = new Validation((byte)0x00);
		
		for (byte[] e : entries2){
			bufferOut = kuz.encryptPerRound(e,9);
			System.out.println("Enc: " + Utils.byteArrToHexStr(e));
			int i = 0;
			for (byte[] bytes : bufferOut){
				System.out.println("Round " + i + ": zero bytes: " + v.goodBytesInArr(bytes));
				i ++;
			}
			System.out.println("\n");
		}
		
		for (byte[] e : entries2){
			bufferOut = kuz.decryptPerRound(e,9);
			System.out.println("Decrypt: " + Utils.byteArrToHexStr(e));
			int i = 0;
			for (byte[] bytes : bufferOut){
				System.out.println("Round " + i + ": zero bytes: " + v.goodBytesInArr(bytes));
				i ++;
			}
			System.out.println("\n");
		}
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
	
	public static Vector<CryptPair> processEnc(
			byte[][] entries,
			int numberOfRounds,
			byte goodByte
	){
		byte[] tmpOut;
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
		String prefix = "_TREE_BLOCK_TEST_";
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
		
		for (byte[] entry : entries) {
			i ++;
			tmpOut = kusnechik.encryptOneRoundZeroKey(entry);
			goodBytes = validation.goodBytesInArr(tmpOut);
			
			if (goodBytes > 4) {
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
	
	public static Vector<CryptPair> processDecr(
			byte[][] entries,
//			byte[][] iterKeys,
			int numberOfRounds,
			byte goodByte
	){
		byte[] tmpOut;
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
		String prefix = "_TREE_BLOCK_DECRYPT_TEST_";
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

		for (byte[] entry : entries) {
			i ++;
			tmpOut = kusnechik.decryptOneRoundZeroKey(entry);
			goodBytes = validation.goodBytesInArr(tmpOut);
			
			if (goodBytes > 4) {
				CryptPair goodOut = new CryptPair(
						Arrays.copyOf(tmpOut, tmpOut.length),
						null,
						entry,
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
