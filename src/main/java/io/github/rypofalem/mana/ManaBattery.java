package io.github.rypofalem.mana;

import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Something that can receives, stores and releases mana
 */
public abstract class ManaBattery {
	private double mana;
	private double maxMana;
	private double regeneration;
	private Map<String, ManaModifier> maxModifierMap;
	private Map<String, ManaModifier> regenModifierMap;
	static final int UPDATEPERIOD = 10;


	public ManaBattery(){
		this(10);
	}

	public ManaBattery(double maxMana){
		this(0.0, maxMana, 0);
	}

	public ManaBattery(double mana, double maxMana, double regeneration){
		this.mana = mana;
		this.maxMana = maxMana;
		this.regeneration = regeneration;
		maxModifierMap = new HashMap<>();
		regenModifierMap = new HashMap<>();
	}

	public double getMaxMana(){
		return maxMana;
	}

	public double getMana(){
		return mana;
	}

	public boolean hasMana(double mana){
		return this.mana >= mana;
	}

	public boolean addMana(double extraMana){
		return addMana(extraMana, false);
	}

	/*
	 * Attempt to add mana.
	 * If mana goes over the maxMana, mana will equal the maxMana instead (return true)
	 * If mana would go under 0 and allowUnderFlow is true, mana will equal 0 instead (return true)
	 * If mana would go under 0 and allowUnderFlow is false, mana will not change (return false)
	 *
	 * returns true if mana was successfully added
	 */
	public boolean addMana(double extraMana, boolean allowUnderFlow){
		if(!allowUnderFlow && mana + extraMana < 0) return false;
		mana += extraMana;
		bringManaInBounds();
		return true;
	}

	public boolean subMana(double subMana){
		return addMana(-subMana, false);
	}

	public boolean subMana(double subMana, boolean allowUnderFlow){
		return addMana(-subMana, allowUnderFlow);
	}

	private double bringManaInBounds(){
		return mana = Math.min(Math.max(mana, 0), maxMana);
	}

	public String getManaMeter(){
		StringBuilder sb = new StringBuilder();
		final int stars = 10;
		final int goldStars = (int)(getMana()/getMaxMana() * stars);
		for(int i = 1; i <= stars; i++){
			if( i <= goldStars ){
				sb.append(ChatColor.GOLD.toString());
				sb.append("✦");
			} else{
				sb.append(ChatColor.DARK_AQUA.toString());
				sb.append("✧");
			}
			if(i == stars/2) sb.append(String.format(" %s%02d ", ChatColor.WHITE, (int)getMana()));
		}
		return sb.toString();
	}

	public void addMaxModifier(ManaModifier modifier){
		maxModifierMap.put(modifier.getID(), modifier);
		recalculateMaxModifiers();
	}

	public void removeMaxModifier(ManaModifier modifier){
		removeMaxModifier(modifier.getID());
	}

	public void removeMaxModifier(String ID){
		maxModifierMap.remove(ID);
		recalculateMaxModifiers();
	}

	public void addRegenModifier(ManaModifier modifier){
		regenModifierMap.put(modifier.getID(), modifier);
		recalculateRegenModifiers();
	}

	public void removeRegenModifier(ManaModifier modifier){
		removeRegenModifier(modifier.getID());
	}

	public void removeRegenModifier(String ID){
		regenModifierMap.remove(ID);
		recalculateRegenModifiers();
	}

	private double recalculateMaxModifiers(){
		maxMana = recalculateModifiers(maxModifierMap.values());
		bringManaInBounds();
		return maxMana;
	}

	private double recalculateRegenModifiers(){
		return regeneration = recalculateModifiers(regenModifierMap.values());
	}

	private double recalculateModifiers(Collection<ManaModifier> manaModifiers){
		double out = 0;
		for(ManaModifier mod : regenModifierMap.values()){
			out+= mod.amount();
		}
		return out;
	}

	public void update(){
		addMana(regeneration * UPDATEPERIOD / 20);
	}
}
