package analytic;

import cipher.Builder;
import utils.Utils;

public class Duration {
	/**
	 * Засекает время выполнения Tests.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void oneBlockTest(){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Tests.oneBlockTest();
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Tests.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void twoBlockTest(){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Tests.twoBlockTest();
		
		testStop(start, stop);
	}
	
	/**
	 * Засекает время выполнения Tests.oneBlockNotNull(numberOfRounds)
	 *
	 * Шифрует всевозможные входы с ПЕРВЫМ ненулевым блоком
	 * на numberOfRounds раунда. Использует НУЛЕВЫЕ ИТЕРАЦИОННЫЕ КЛЮЧИ.
	 * Возвращает массив ШТ
	 **/
	public static void threeBlockTest(){
		long start, stop = 0;
		
		start = System.currentTimeMillis();
		Tests.threeBlockTest();
		
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
