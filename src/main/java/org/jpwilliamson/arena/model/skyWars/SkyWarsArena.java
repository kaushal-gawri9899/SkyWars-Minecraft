package org.jpwilliamson.arena.model.skyWars;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jpwilliamson.arena.menu.SkyWarsChestMenu;
import org.jpwilliamson.arena.menu.SpinRewardMenu;
import org.jpwilliamson.arena.model.ArenaPlayer;
import org.jpwilliamson.arena.model.ArenaScoreboard;
import org.jpwilliamson.arena.menu.EggWarsVillagerMenu;
import org.jpwilliamson.arena.model.Arena;
import org.jpwilliamson.arena.model.ArenaHeartbeat;
import org.jpwilliamson.arena.model.ArenaJoinMode;
import org.jpwilliamson.arena.model.ArenaLeaveReason;
import org.jpwilliamson.arena.model.eggwars.EggWarsHeartbeat;
import org.jpwilliamson.arena.model.eggwars.EggWarsScoreboard;
import org.jpwilliamson.arena.model.eggwars.EggWarsSettings;
import org.jpwilliamson.arena.util.Constants;
import org.mineacademy.fo.BlockUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.model.RandomNoRepeatPicker;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompProperty;
import org.mineacademy.fo.remain.CompSound;

/**
 * Eggwars arena is a game where players spawn on their
 * islands where they collect items spawned on the ground
 * in exchange for items.
 *
 * The point is to destroy one another's crystal (egg) then kill
 * the other player. Once your crystal is destroyed you stop respawning.
 * Last man standing wins.
 */
public class SkyWarsArena extends Arena {

	/**
	 * The arena unique identification type
	 */
	public static final String TYPE = "skywars";

	/**
	 * Create a new arena
	 *
	 * @param name
	 */
	public SkyWarsArena(final String name) {
		super(TYPE, name);
	}

	/**
	 * Create new arena settings
	 */
	@Override
	protected SkyWarsSettings createSettings() {
		return new SkyWarsSettings(this);
	}

	/**
	 * Create new arena heartbeat
	 */
	@Override
	protected ArenaHeartbeat createHeartbeat() {
		return new SkyWarsHeartbeat(this);
	}

	/**
	 * Return a custom scoreboard for this arena
	 */
	@Override
	protected ArenaScoreboard createScoreboard() {
		return new SkyWarsScoreboard(this);
	}

	// ------–------–------–------–------–------–------–------–------–------–------–------–
	// Game logic
	// ------–------–------–------–------–------–------–------–------–------–------–------–

	/**
	 * Teleport players and spawn eggs/villagers
	 */
	@Override
	protected void onStart() {
		super.onStart();


		if (isEdited())
			return;

		// Use no repeat picker to ensure no 2 players will spawn at one spot
		final RandomNoRepeatPicker<Location> locationPicker = RandomNoRepeatPicker.newPicker(Location.class);
		locationPicker.setItems(getSettings().getEntrances());

		//final List<Location> eggLocations = Common.toList(getSettings().getEggs());

		for (final ArenaPlayer arenaPlayer : getArenaPlayersInAllModes()) {
			final Player player = arenaPlayer.getPlayer();
			final Location location = locationPicker.pickRandom(player);

			// Teleport to arena
			teleport(player, location);
			Bukkit.broadcastMessage("This is skywars");
			SpinRewardMenu.openSpinMenu(this);
			SpinRewardMenu.openSpinInventory(player);
			// Save their location for respawns
			setPlayerTag(arenaPlayer, Constants.Tag.ENTRANCE_LOCATION, location);

			// Spawn crystal
			//final Location closestEgg = BlockUtil.findClosestLocation(player.getLocation(), eggLocations);
			//eggLocations.remove(closestEgg);

			//final EnderCrystal crystal = closestEgg.getWorld().spawn(closestEgg.clone().add(0.5, 1, 0.5), EnderCrystal.class);
			//setEntityTag(crystal, Constants.Tag.TEAM_CRYSTAL, player.getName());
		}

		// Spawn villagers
//		for (final Location villagerLocation : getSettings().getChests()) {
//			final Villager villager = villagerLocation.getWorld().spawn(villagerLocation.clone().add(0.5, 1, 0.5), Villager.class);
//
//			CompProperty.INVULNERABLE.apply(villager, true);
//		}
//		for(final Location chestLocation : getSettings().getChests()){
//			final CompMaterial.ENDER_CHEST chest = chestLocation.getWorld().spawn(chestLocation.clone().add(0.5, 1, 0.5), CompMaterial.class);
//		}
	}

	/**
	 * Remove crystals on stop
	 */
	@Override
	protected void onStop() {
		super.onStop();

		//removeCrystals();
	}

	/*
	 * Remove crystals that are left when the arena stops
	 */
	private void removeCrystals() {
		final World world = getSettings().getRegion().getWorld();

		for (final EnderCrystal crystal : world.getEntitiesByClass(EnderCrystal.class))
			if (hasEntityTag(crystal, Constants.Tag.TEAM_CRYSTAL))
				crystal.remove();
	}

	/**
	 * @see Arena#getRespawnLocation(org.bukkit.entity.Player)
	 */
	@Override
	protected Location getRespawnLocation(final Player player) {
		final ArenaPlayer cache = ArenaPlayer.getCache(player);
		final Location location = getPlayerTag(cache, Constants.Tag.ENTRANCE_LOCATION);
		Valid.checkNotNull(location, "Player " + player.getName() + " is missing entrance location!");

		return location;
	}

	/**
	 * Kick players without a crystal
	 */
	@Override
	protected void onPlayerRespawn(Player player, ArenaPlayer cache) {
		super.onPlayerRespawn(player, cache);

		if (!hasCrystal(player)) {
			leavePlayer(player, ArenaLeaveReason.CRYSTAL_DESTROYED);

			returnHandled();
		}
	}

	/*
	 * Return true if there is a crystal with the player tag associated spawned in the arena
	 */
	private boolean hasCrystal(Player player) {
		for (final Entity entity : getSettings().getRegion().getEntities())
			if (entity instanceof EnderCrystal && player.getName().equals(getEntityTag(entity, Constants.Tag.TEAM_CRYSTAL)))
				return true;

		return false;
	}

	/**
	 * Handle clicking on villagers
	 *
	 * @see Arena#onEntityClick(org.bukkit.entity.Player, org.bukkit.entity.Entity, org.bukkit.event.player.PlayerInteractAtEntityEvent)
	 */
	@Override
	protected void onEntityClick(Player player, Entity clicked, PlayerInteractAtEntityEvent event) {
		super.onEntityClick(player, clicked, event);

		if (clicked instanceof EnderChest){
			SkyWarsChestMenu.openPurchaseMenu(this, player);
		}
			//EggWarsVillagerMenu.openPurchaseMenu(this, player);

	}

	/**
	 * Prevent crystal going BOOM
	 *
	 * @see Arena#onDamage(org.bukkit.entity.Entity, org.bukkit.entity.Entity, org.bukkit.event.entity.EntityDamageByEntityEvent)
	 */
	@Override
	protected void onDamage(Entity attacker, Entity victim, EntityDamageByEntityEvent event) {
		super.onDamage(attacker, victim, event);

		if (victim instanceof EnderCrystal)
			event.setCancelled(true);
	}

	/**
	 * Handle crystal damage
	 *
	 * @see Arena#onPlayerDamage(org.bukkit.entity.Player, org.bukkit.entity.Entity, org.bukkit.event.entity.EntityDamageByEntityEvent)
	 */
	@Override
	protected void onPlayerDamage(Player attacker, Entity victim, EntityDamageByEntityEvent event) {
		super.onPlayerDamage(attacker, victim, event);

		if (!(victim instanceof EnderCrystal) || !hasEntityTag(victim, Constants.Tag.TEAM_CRYSTAL))
			return;

		final ArenaPlayer attackerCache = ArenaPlayer.getCache(attacker);

		final Player crystalOwner = Bukkit.getPlayer(getEntityTag(victim, Constants.Tag.TEAM_CRYSTAL));
		final ArenaPlayer crystalOwnerCache = findPlayer(crystalOwner);

//		if (attackerCache.equals(crystalOwnerCache)) {
//			Messenger.error(attacker, "You cannot damage your own crystal!");
//
//		}
//		else {
//			int damage = getNumericEntityTag(victim, Constants.Tag.CRYSTAL_DAMAGE, 0);
//			final int threshold = 10;
//
//			if (++damage >= threshold) {
//				victim.remove();
//				broadcastWarn(crystalOwner.getName() + "'s egg got destroyed!");
//				event.setCancelled(true);
//				returnHandled();
//			}
//
//			// Broadcast every second hit only
//			if (damage % 2 == 0) {
//				for (final Player otherPlayer : getPlayersInAllModes()) {
//					if (otherPlayer.equals(attacker)) {
//						Messenger.info(otherPlayer, Common.format("Damaged %s's egg! (%s/%s)", crystalOwner.getName(), damage, threshold));
//						CompSound.ANVIL_LAND.play(otherPlayer);
//
//						continue;
//					}
//
//					if (otherPlayer.equals(crystalOwner)) {
//						Messenger.info(otherPlayer, Common.format("&eYour egg got damaged! (%s/%s)", damage, threshold));
//
//						CompSound.SUCCESSFUL_HIT.play(otherPlayer);
//
//					} else
//						Messenger.info(otherPlayer, Common.format("%s's crystal just got damaged! (%s/%s)", crystalOwner.getName(), damage, threshold));
//				}
//			}
//
//			setNumericEntityTag(victim, Constants.Tag.CRYSTAL_DAMAGE, damage);
//		}
//
//		event.setCancelled(true);
//		returnHandled();
	}

	/**
	 * Prevent explosions from damaging safezone blocks
	 *
	 * @see Arena#onExplosion(org.bukkit.Location, java.util.List, org.bukkit.event.Cancellable)
	 */
	@Override
	protected void onExplosion(Location centerLocation, List<Block> blocks, Cancellable event) {
		super.onExplosion(centerLocation, blocks, event);

		//final EggWarsSettings settings = getSettings();
		final SkyWarsSettings settings = getSettings();
		final List<Location> locations = Common.joinArrays(
				settings.getIron(),
				settings.getGold(),
				settings.getDiamonds());

		for (final Entity entity : settings.getRegion().getEntities())
			if (entity instanceof EnderCrystal || entity instanceof Villager)
				locations.add(entity.getLocation());

		for (final Iterator<Block> it = blocks.iterator(); it.hasNext();) {
			final Block block = it.next();
			final Location blockLocation = block.getLocation();
			final Location closestSafezone = BlockUtil.findClosestLocation(blockLocation, locations);

			if (closestSafezone.distance(blockLocation) < 3)
				it.remove();
		}
	}

	/**
	 * @see Arena#onLeave(org.bukkit.entity.Player, ArenaLeaveReason)
	 */
	@Override
	protected void onLeave(final Player player, final ArenaLeaveReason reason) {
		super.onLeave(player, reason);

		checkLastStanding();
	}

	/**
	 * Run the last standing check when a player enters spectate mode, stop
	 * arena if only 1 player is left playing
	 */
	@Override
	protected void onSpectateStart(final Player player, final ArenaLeaveReason reason) {
		super.onSpectateStart(player, reason);

		checkLastStanding();
	}

	/*
	 * Check if there is only one last player in the playing mode then stop the arena
	 * and announce winner
	 */
	private void checkLastStanding() {
		if (getPlayers(ArenaJoinMode.PLAYING).size() == 1 && !isStopping()) {
			final Player winner = getPlayers(ArenaJoinMode.PLAYING).get(0);
			leavePlayer(winner, ArenaLeaveReason.LAST_STANDING);
		}
	}

	/**
	 * @see Arena#canSpectateOnLeave(org.bukkit.entity.Player)
	 */
	@Override
	protected boolean canSpectateOnLeave(final Player player) {
		return getPlayers(ArenaJoinMode.PLAYING).size() > 1;
	}

	// ------–------–------–------–------–------–------–------–------–------–------–------–
	// Pluggable
	// ------–------–------–------–------–------–------–------–------–------–------–------–

	/**
	 * @see Arena#hasPvP()
	 */
	@Override
	protected boolean hasPvP() {
		return true;
	}

	// ------–------–------–------–------–------–------–------–------–------–------–------–
	// Overrides
	// ------–------–------–------–------–------–------–------–------–------–------–------–

	/**
	 * @see Arena#getSettings()
	 */
	@Override
	public SkyWarsSettings getSettings() {
		return (SkyWarsSettings) super.getSettings();
	}
}

