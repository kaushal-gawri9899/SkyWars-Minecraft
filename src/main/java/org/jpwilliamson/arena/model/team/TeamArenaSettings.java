package org.jpwilliamson.arena.model.team;

import java.util.List;

import org.bukkit.Location;
import org.jpwilliamson.arena.model.ArenaSettings;
import org.jpwilliamson.arena.model.ArenaTeam;
import org.jpwilliamson.arena.model.ArenaTeamPoints;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;

import lombok.Getter;

/**
 * Represents settings used in monster arenas
 */
public class TeamArenaSettings extends ArenaSettings {

	/**
	 * The entry spawn point for each team
	 */
	private ArenaTeamPoints teamSpawnpoints;

	/**
	 * How big can the imbalance be in teams between players? This
	 * is used to auto balance teams and must be at least 1
	 */
	@Getter
	private int maximumTeamImbalance;

	/**
	 * Create new arena settings
	 *
	 * @param arenaName
	 */
	public TeamArenaSettings(final TeamArena arena) {
		super(arena);
	}

	/**
	 * @see ArenaSettings#onLoadFinish()
	 */
	@Override
	protected void onLoadFinish() {
		super.onLoadFinish();

		this.teamSpawnpoints = new ArenaTeamPoints(this, getMap("Team_Spawnpoints"));
		this.maximumTeamImbalance = getInteger("Maximum_Team_Imbalance", 2);

		Valid.checkBoolean(maximumTeamImbalance > 0, "Maximum team imbalance must be greater than 0");
	}

	/**
	 * How big can the imbalance be in teams between players? This
	 * is used to auto balance teams and must be at least 1
	 *
	 * @param maximumTeamImbalance the maximumInbalance to set
	 */
	public void setMaximumTeamImbalance(final int maximumTeamImbalance) {
		this.maximumTeamImbalance = maximumTeamImbalance;

		save();
	}

	/**
	 * Set the team spawn point at the given location, removing the old point
	 * since we only enable 1 spawn point per team
	 *
	 * @param location
	 */
	public void setSpawnpoint(final ArenaTeam team, final Location location) {
		teamSpawnpoints.setPoint(team, location);
	}

	/**
	 * Add a new location
	 *
	 * @param location
	 */
	public void addSpawnpoint(final ArenaTeam team, final Location location) {
		teamSpawnpoints.addPoint(team, location);
	}

	/**
	 * Remove an existing location
	 *
	 * @param location
	 */
	public void removeSpawnpoint(final ArenaTeam team, final Location location) {
		teamSpawnpoints.removePoint(team, location);
	}

	/**
	 * Return true if the given location exists
	 *
	 * @param location
	 * @return
	 */
	public boolean hasSpawnpointLocation(final Location location) {
		return teamSpawnpoints.hasPoint(location);
	}

	/**
	 * Return what team owns the spawnpoint at the given location
	 *
	 * @param location
	 * @return
	 */
	public ArenaTeam findTeam(final Location location) {
		return teamSpawnpoints.findTeam(location);
	}

	/**
	 * Get the spawnpoint for the given team
	 *
	 * @param team
	 * @return
	 */
	public Location findSpawnpoint(final ArenaTeam team) {
		return teamSpawnpoints.findPoint(team);
	}

	/**
	 * Get a list of all locations that have a team spawnpoint set
	 *
	 * @return
	 */
	public List<Location> getSpawnpoints() {
		return teamSpawnpoints.getLocations();
	}

	/**
	 * @see ArenaSettings#serialize()
	 */
	@Override
	public SerializedMap serialize() {
		final SerializedMap map = super.serialize();

		map.putArray(
				"Team_Spawnpoints", teamSpawnpoints,
				"Maximum_Team_Imbalance", maximumTeamImbalance);

		return map;
	}
}
