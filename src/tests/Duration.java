package tests;

import analytic.Prelude;
import kuznechik.Builder;
import utils.Utils;

public class Duration {
	/**
	 * Засекает время выполнения Prelude.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void oneBlockNotNull(){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Prelude.oneBlockTest();
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Prelude.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void twoBlockTest(int numberOfRounds, boolean verbose, byte goodByte){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Prelude.twoBlockTest();
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Prelude.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void threeBlockTest(){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Prelude.threeBlockTest();
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Builder.genRandKeys(40000);
	 **/
	public static void keyGeneration(){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Builder.genRandKeys(40000);
		
		testStop(start, stop);
	}
	
	public static void testStop(long start, long stop) {
		stop = System.currentTimeMillis();
		double duration;
		duration = stop - start;
		
		String timeLog = "Test takes " + duration + " milliseconds\n" +
							"Test takes " + duration / 1000 + " seconds\n" +
							"Test takes " + duration / 1000 / 60 + " minutes\n";
		System.out.println(timeLog);
		Utils.log(timeLog);
	}
}
