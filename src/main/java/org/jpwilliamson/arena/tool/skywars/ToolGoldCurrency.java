package org.jpwilliamson.arena.tool.skywars;

import org.bukkit.inventory.ItemStack;
import org.jpwilliamson.arena.model.eggwars.EggWarsSettings;
import org.jpwilliamson.arena.model.skyWars.SkyWarsSettings;
import org.jpwilliamson.arena.tool.eggwars.ToolEggWars;
import org.jpwilliamson.arena.tool.eggwars.ToolGold;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig.LocationList;

import lombok.Getter;

/**
 *
 */
public class ToolGoldCurrency extends ToolSkyWars {

	@Getter
	private static final Tool instance = new ToolGoldCurrency();

	private ToolGoldCurrency() {
		super("Gold Currency", CompMaterial.GOLD_BLOCK);
	}

	/**
	 * @see org.mineacademy.fo.menu.tool.Tool#getItem()
	 */
	@Override
	public ItemStack getItem() {
		return ItemCreator.of(
				CompMaterial.GOLD_ORE,
				"GOLD Currency SPAWNPOINT",
				"Add location where",
				"gold will appear.")
				.build().make();
	}

//	/**
//	 * @see ToolEggWars#getLocations(EggWarsSettings)
//	 */
	@Override
	protected LocationList getLocations(SkyWarsSettings settings) {
		return settings.getGold();
	}
}
