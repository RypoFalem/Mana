package io.github.rypofalem.mana;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;


public class ManaPlugin extends JavaPlugin implements Listener{

	@Override
	public void onEnable(){
		PlayerBattery.playerBatteries = new HashMap<>();
		Bukkit.getScheduler().runTaskTimer(this,
				() -> {
					for (Player player : Bukkit.getOnlinePlayers()) {
						PlayerBattery battery = PlayerBattery.of(player.getUniqueId());
						battery.update();
					}
				}
		, 1, PlayerBattery.UPDATEPERIOD);
	}
}
