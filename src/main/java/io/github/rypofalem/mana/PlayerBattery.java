package io.github.rypofalem.mana;



import com.connorlinfoot.actionbarapi.ActionBarAPI;
import org.bukkit.Bukkit;
;
import java.util.Map;
import java.util.UUID;

public class PlayerBattery extends ManaBattery{
	static Map<UUID,PlayerBattery> playerBatteries;
	UUID player;

	PlayerBattery(UUID player){
		super(0, 10, .25);
		this.player = player;
	}

	PlayerBattery(double maxMana, UUID player) {
		super(maxMana);
		this.player = player;
	}

	public void displayManaMeter(){
//		Map<String, Object> map = new HashMap<>();
//		map.put("text", getManaMeter());
//		String raw = JSONValue.toJSONString(map);
//		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("title %s actionbar %s", Bukkit.getPlayer(player).getName(), raw));

		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("title %s actionbar [{\"text\":\"%s\"}]", Bukkit.getPlayer(player).getName(), getManaMeter()));
		ActionBarAPI.sendActionBar(Bukkit.getPlayer(player), getManaMeter());
	}

	@Override
	public boolean addMana(double extraMana, boolean allowUnderFlow){
		double before = getMana();
		boolean successful = super.addMana(extraMana, allowUnderFlow);
		if(successful && before != getMana()) displayManaMeter();
		return successful;
	}

	public static PlayerBattery of(UUID player){
		PlayerBattery battery = playerBatteries.get(player);
		if(battery == null){
			battery = new PlayerBattery(player);
			playerBatteries.put(player, battery);
		}
		return battery;
	}
}
