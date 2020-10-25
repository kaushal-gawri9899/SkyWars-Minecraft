package org.jpwilliamson.arena.menu;

import org.jetbrains.annotations.Nullable;
import org.jpwilliamson.arena.model.skyWars.SkyWarsArena;
import org.mineacademy.fo.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;


import java.util.ArrayList;
import java.util.List;

public class SpinRewardMenu extends Menu {

	private static List<CompMaterial> randomList = new ArrayList<CompMaterial>();
	private static List<CompMaterial> rewardList = new ArrayList<CompMaterial>();
	private static Inventory spinInventory;
	private String inventoryTitle = "Spin And Win";
	private SkyWarsArena arena;


	public SpinRewardMenu(SkyWarsArena arena)
	{
	super();
	this.arena = arena;
	setSize(9*5);
	setTitle(inventoryTitle);
	spinInventory = Bukkit.createInventory(null, 9*5,"Spin and Win");
//	randomList = new ArrayList<CompMaterial>();
//	rewardList = new ArrayList<CompMaterial>();
	fillRandomList();
	createInitialSpinMenu();
	}

	public static void openSpinInventory(Player player){

		//createInitialSpinMenu();
	player.openInventory(spinInventory);
	}



	public static void createInitialSpinMenu(){

	for(int i=0; i<45; i++)
	{
		int val = i+1;
		if((val+1)/2==23)
		{
			int newVal = (val+1)/2;
			ItemStack stack =ItemCreator.of(CompMaterial.DIAMOND_SWORD, "Click to spin", "Get Your Reward!").build().make();
			spinInventory.setItem(newVal,new ItemStack(stack));
		}

		else{
			if(val%9==1)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
			else if(val%9==2)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.GREEN_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
			else if(val%9==3)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
			else if(val%9==4)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.BLUE_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
			else if(val%9==5 && (val+1)/2!=23)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
			else if(val%9==6)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
			else if(val%9==7)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.CYAN_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
			else if(val%9==8)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.PURPLE_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
			else if(val%9==0)
			{
				ItemStack stack = ItemCreator.of(CompMaterial.LIME_STAINED_GLASS_PANE).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
		}
	}

	}


	public static void updateState(Player player){

		for(int i=0; i<spinInventory.getSize(); i++)
		{
			int val = i+1;
			if((val+1)/2==23)
			{
				int newVal = (val+1)/2;
				int min=0;
				int max = 14;
				int seed = (int) (Math.random()*(max-min+1) + min);
				ItemStack stack = ItemCreator.of(rewardList.get(seed),"You Recieved A Reward!").build().make();
				spinInventory.setItem(newVal,new ItemStack(stack));
			}
			else{
				int min=0;
				int max = 7;
				int seed = (int) (Math.random()*(max-min+1) + min);
				ItemStack stack = ItemCreator.of(randomList.get(seed)).build().make();
				spinInventory.setItem(val,new ItemStack(stack));
			}
		}
	}

	public static ItemStack getItemAtCentre(){
		if(spinInventory.getItem(23)!=null)
		return spinInventory.getItem(23);
		else
			return ItemCreator.of(CompMaterial.DIAMOND_SWORD, "Click to spin", "Get Your Reward!").build().make();
	}

	public static void openSpinMenu(SkyWarsArena arena)
	{
		new SpinRewardMenu(arena);
	}

	@Override
	protected void onDisplay(InventoryDrawer drawer) {
		super.onDisplay(drawer);
	}

	@Override
	public ItemStack getItemAt(int slot) {
		return super.getItemAt(slot);
	}

	@Override
	protected void onMenuClick(Player player, int slot, InventoryAction action, ClickType click, ItemStack cursor, ItemStack clicked, boolean cancelled) {
		super.onMenuClick(player, slot, action, click, cursor, clicked, cancelled);
	}

	@Override
	protected void onMenuClose(Player player, Inventory inventory) {
		super.onMenuClose(player, inventory);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	protected String[] getInfo() {
		return super.getInfo();
	}

	public void fillRandomList(){
		randomList.add(CompMaterial.RED_STAINED_GLASS_PANE);
		randomList.add(CompMaterial.GREEN_STAINED_GLASS_PANE);
		randomList.add(CompMaterial.ORANGE_STAINED_GLASS_PANE);
		randomList.add(CompMaterial.BLUE_STAINED_GLASS_PANE);
		randomList.add(CompMaterial.YELLOW_STAINED_GLASS_PANE);
		randomList.add(CompMaterial.CYAN_STAINED_GLASS);
		randomList.add(CompMaterial.PURPLE_STAINED_GLASS);
		randomList.add(CompMaterial.LIME_STAINED_GLASS);

		rewardList.add(CompMaterial.ARROW);
		rewardList.add(CompMaterial.SPECTRAL_ARROW);
		rewardList.add(CompMaterial.TIPPED_ARROW);
		rewardList.add(CompMaterial.BOW);
		rewardList.add(CompMaterial.CROSSBOW);
		rewardList.add(CompMaterial.STONE_SWORD);
		rewardList.add(CompMaterial.GOLDEN_SWORD);
		rewardList.add(CompMaterial.DIAMOND_SWORD);
		rewardList.add(CompMaterial.WOODEN_SWORD);
		rewardList.add(CompMaterial.TRIDENT);
		rewardList.add(CompMaterial.APPLE);
		rewardList.add(CompMaterial.GOLDEN_APPLE);
		rewardList.add(CompMaterial.ENCHANTED_GOLDEN_APPLE);
		rewardList.add(CompMaterial.CARROT);
		rewardList.add(CompMaterial.MILK_BUCKET);
		rewardList.add(CompMaterial.SHIELD);

	}
}
