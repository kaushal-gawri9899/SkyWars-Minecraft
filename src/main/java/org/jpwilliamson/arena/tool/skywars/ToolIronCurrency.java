package org.jpwilliamson.arena.tool.skywars;

import org.bukkit.inventory.ItemStack;
import org.jpwilliamson.arena.model.eggwars.EggWarsSettings;
import org.jpwilliamson.arena.model.skyWars.SkyWarsSettings;
import org.jpwilliamson.arena.tool.eggwars.ToolEggWars;
import org.jpwilliamson.arena.tool.eggwars.ToolIrons;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig.LocationList;

import lombok.Getter;

/**
 *
 */
public class ToolIronCurrency extends ToolSkyWars {

	@Getter
	private static final Tool instance = new ToolIronCurrency();

	private ToolIronCurrency() {
		super("Iron Currency", CompMaterial.IRON_BLOCK);
	}

	/**
	 * @see org.mineacademy.fo.menu.tool.Tool#getItem()
	 */
	@Override
	public ItemStack getItem() {
		return ItemCreator.of(
				CompMaterial.IRON_ORE,
				"IRON CURRENCY SPAWNPOINT",
				"Add Location where",
				"iron will appear.")
				.build().make();
	}

//	/**
//	 * @see ToolEggWars#getLocations(EggWarsSettings)
//	 */
	@Override
	protected LocationList getLocations(SkyWarsSettings settings) {
		return settings.getIron();
	}
}
