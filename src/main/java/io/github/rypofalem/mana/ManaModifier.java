package io.github.rypofalem.mana;


public interface ManaModifier {

	/*
	 * return unique ID for this modifier
	 */
	String getID();

	/*
	 * return the amount that should be modified
	 */
	double amount();
}
