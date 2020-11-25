package net.dysnomian.cursed_enchanting;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class CursedEnchanting implements ModInitializer {
	
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!\n\n\nH\nE\nL\nL\nO\n!\n");

		Registry.register(Registry.BLOCK, CursedEnchantingTableBlock.ID, CursedEnchantingTableBlock.INSTANCE);
		Registry.register(Registry.ITEM, CursedEnchantingTableBlock.ID, CursedEnchantingTableBlock.INSTANCE_ITEM);
	}
}
