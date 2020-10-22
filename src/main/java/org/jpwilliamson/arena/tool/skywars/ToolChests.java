package org.jpwilliamson.arena.tool.skywars;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jpwilliamson.arena.menu.SkyWarsChestMenu;
import org.jpwilliamson.arena.model.Arena;
import org.jpwilliamson.arena.model.skyWars.SkyWarsArena;
import org.jpwilliamson.arena.model.skyWars.SkyWarsSettings;
import org.jpwilliamson.arena.tool.VisualTool;
import org.jpwilliamson.arena.menu.EggWarsVillagerMenu;
import org.jpwilliamson.arena.model.eggwars.EggWarsArena;
import org.jpwilliamson.arena.model.eggwars.EggWarsSettings;
import org.jpwilliamson.arena.tool.eggwars.ToolEggWars;
import org.jpwilliamson.arena.tool.eggwars.ToolVillagers;
import org.jpwilliamson.arena.tool.skywars.ToolSkyWars;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig.LocationList;

import lombok.Getter;

/**
 *
 */
public class ToolChests extends ToolSkyWars {

	@Getter
	private static final Tool instance = new ToolChests();

	private ToolChests() {
		super("Reward Chests", CompMaterial.ENDER_CHEST);
	}

	/**
	 * @see org.mineacademy.fo.menu.tool.Tool#getItem()
	 */
	@Override
	public ItemStack getItem() {
				return ItemCreator.of(
				CompMaterial.DRAGON_EGG,
				"CHEST SPAWNPOINT",
				"Click to toggle where",
				"chests are placed",
				"or click air to",
				"edit their shop items")
				.build().make();
	}

	/**
	 * @see VisualTool#handleAirClick(org.bukkit.entity.Player, Arena, org.bukkit.event.inventory.ClickType)
	 */
	@Override
	protected void handleAirClick(Player player, SkyWarsArena arena, ClickType click) {
		//EggWarsVillagerMenu.openEditMenu(arena, player);
		SkyWarsChestMenu.openEditMenu(arena, player);
	}

//	/**
//	 * @see ToolEggWars#getLocations(EggWarsSettings)
//	 */
	@Override
	protected LocationList getLocations(SkyWarsSettings settings) {
		return settings.getChests();
	}
}
