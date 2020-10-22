package org.jpwilliamson.arena.tool.skywars;

import org.bukkit.inventory.ItemStack;
import org.jpwilliamson.arena.model.eggwars.EggWarsSettings;
import org.jpwilliamson.arena.model.skyWars.SkyWarsSettings;
import org.jpwilliamson.arena.tool.eggwars.ToolDiamonds;
import org.jpwilliamson.arena.tool.skywars.ToolSkyWars;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig.LocationList;

import lombok.Getter;

/**
 *
 */
public class ToolDiamondCurrency extends ToolSkyWars {

	@Getter
	private static final Tool instance = new ToolDiamondCurrency();

	private ToolDiamondCurrency() {
		super("Diamond Currency", CompMaterial.DIAMOND_BLOCK);
	}

//	/**
//	 * @see org.mineacademy.fo.menu.tool.Tool#getItem()
//	 */
	@Override
	public ItemStack getItem() {
		return ItemCreator.of(
				CompMaterial.DIAMOND_ORE,
				"DIAMOND CURRENCY SPAWNPOINT",
				"Add location where",
				"diamonds will appear.")
				.build().make();
	}

//	/**
//	 * @see ToolEggWars#getLocations(EggWarsSettings)
//	 */
	@Override
	protected LocationList getLocations(SkyWarsSettings settings) {
		return settings.getDiamonds();
	}
}
