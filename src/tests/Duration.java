package tests;

import analytic.Prelude;
import kuznechik.Builder;
import utils.CryptPair;
import utils.Utils;

import java.util.Arrays;
import java.util.Vector;

public class Duration {
	
	/**
	 * Засекает время выполнения Prelude.oneBlockFull(numberOfKeys, numberOfRounds);
	 *
	 * Шифрует всевозможные входы с ОДНИМ ненулевым блоком
	 * на numberOfRounds раунда. Использует numberOfKeys СЛУЧАЙНЫХ ИТЕРАЦИОННЫХ КЛЮЧЕЙ.
	 * Возвращает массив ШТ
	 **/
	static void oneBlockFull(int numberOfKeys, int numberOfRounds, boolean verbose, byte goodByte){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Vector<CryptPair> goodOuts = Prelude.oneBlockFull(numberOfKeys, numberOfRounds, verbose, goodByte);
		
//		System.out.println("Printing \"Good\" outs to " + FILE_NAME + " :");
//		for (CryptPair cp : goodOuts) {
//			System.out.println(cp);
//		}
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Prelude.oneBlockFirstNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void oneBlockFirstNotNull(int numberOfRounds, boolean verbose, byte goodByte){
		long start, stop = 0;
		Vector<CryptPair> goodOuts;
		
		start = System.currentTimeMillis();
		goodOuts = Prelude.firstBlockNotNull(numberOfRounds, verbose, goodByte);
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Prelude.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void oneBlockNotNull(int numberOfRounds, boolean verbose, byte goodByte){
		long start, stop = 0;
		Vector<CryptPair> goodOuts;
		
		start = System.currentTimeMillis();
		goodOuts = Prelude.oneBlockNotNullGood(numberOfRounds, verbose, goodByte);
		
//		System.out.println("Printing \"Good\" outs to " + FILE_NAME + " :");
//		for (CryptPair cp : goodOuts) System.out.println(cp);
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Prelude.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void twoBlockNotNull(int numberOfRounds, boolean verbose, byte goodByte){
		long start, stop = 0;
		Vector<CryptPair> goodOuts;
		
		start = System.currentTimeMillis();
		goodOuts = Prelude.twoBlockNotNullGood(numberOfRounds, verbose, goodByte);
		
//		System.out.println("Printing \"Good\" outs to " + FILE_NAME + " :");
//		for (CryptPair cp : goodOuts) System.out.println(cp);
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Prelude.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void twoBlockNotNullMultTreads(int numberOfRounds, boolean verbose, byte goodByte){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Prelude.twoBlockNotNullGoodMultipleTr(numberOfRounds, verbose, goodByte);
		
//		System.out.println("Printing \"Good\" outs to " + FILE_NAME + " :");
//		for (CryptPair cp : goodOuts) System.out.println(cp);
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения функций:
	 *      Prelude.oneBlockNotNullGood(numberOfRounds, verbose, goodByte);
	 * 		Prelude.twoBlockNotNullGood(numberOfRounds, verbose, goodByte);
	 * 		Prelude.oneBlockNotNullEq(numberOfRounds, verbose, goodByte);
	 * 		Prelude.twoBlockNotNullEq(numberOfRounds, verbose, goodByte);
	 **/
	public static void allTests(int numberOfRounds, boolean verbose, byte goodByte){
		long start, stop = 0;
		Vector<CryptPair> goodOuts;
		
		start = System.currentTimeMillis();
		Prelude.oneBlockNotNullGood(numberOfRounds, verbose, goodByte);
		Prelude.twoBlockNotNullGood(numberOfRounds, verbose, goodByte);
		Prelude.oneBlockNotNullEq(numberOfRounds, verbose, goodByte);
		Prelude.twoBlockNotNullEq(numberOfRounds, verbose, goodByte);

//		System.out.println("Printing \"Good\" outs to " + FILE_NAME + " :");
//		for (CryptPair cp : goodOuts) System.out.println(cp);
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения функций:
	 *
	 *      Prelude.oneTwoBlockNotNullGoodDecrypt(numberOfRounds, verbose, goodByte);
	 *
	 **/
	public static void oneTwoDecryptTests(int numberOfRounds, byte goodByte){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Prelude.oneTwoBlockNotNullGoodDecrypt(numberOfRounds, goodByte);
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Builder.genRandKeys(40000);
	 **/
	public static void keyGeneration(){
		long start, stop = 0;
		byte[][] out;
		
		start = System.currentTimeMillis();
		out = Builder.genRandKeys(40000);
		testStop(start, stop);
		Utils.printArrayOfHexArrays(Arrays.copyOfRange(out, out.length - 1000, out.length));
		
	}
	
	/**
	 * Засекает время выполнения Prelude.oneBlockEnc(), которая:
	 *
	 * Шифрует всевозможные входы с одним ненулевым блоком
	 * на два раунда. Как итерационные ключи использует 1-ю половину
	 * эталонного ключа кузнечика. Возвращает массив ШТ
	 **/
	public static void oneBlockEnc() {
		long start, stop = 0;
		byte[][] out;
		
		start = System.currentTimeMillis();
		out = Prelude.oneBlockEnc();
		testStop(start, stop);
		Utils.printArrayOfHexArrays(Arrays.copyOfRange(out, out.length - 1000, out.length));
		
	}
	
	public static void testStop(long start, long stop) {
		stop = System.currentTimeMillis();
		double duration;
		duration = stop - start;
		
		System.out.println("Test takes " + duration + " milliseconds");
		System.out.println("Test takes " + duration / 1000 + " seconds");
		System.out.println("Test takes " + duration / 1000 / 60 + " minutes");
	}
}
