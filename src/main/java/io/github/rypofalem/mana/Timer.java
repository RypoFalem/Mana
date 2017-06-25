package io.github.rypofalem.mana;


public class Timer {
	static long[] timings = new long[100];
	static int index = 0;
	static double average = 0;
	static long refTime = 0;

	public static void check(){
		long sum = 0;
		long highest = 0;
		for(long x : timings){
			sum+=x;
			if(highest < x) highest = x;
		}
		average = sum / (double)timings.length / 1000000.0;
	}

	public static void addTiming(long timing){
		timings[index%timings.length] = timing;
		index++;
	}

	public static String toStaticString(){
		return String.format("Avg: %.4f", average);
	}

	public static void start(){
		refTime = System.nanoTime();
	}

	public static void stop(){
		addTiming(System.nanoTime() - refTime);
	}

	public static void printIfReady(boolean force){
		if(timings[99] != 0){
			check();
			System.out.print(toStaticString());
		}
	}
}