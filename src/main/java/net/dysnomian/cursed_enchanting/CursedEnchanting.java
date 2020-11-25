package net.dysnomian.cursed_enchanting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CursedEnchanting implements ModInitializer {
	public static final ItemGroup CE_GROUP = FabricItemGroupBuilder.create(
		new Identifier("tutorial", "example"))
		.icon(() -> new ItemStack(CursedEnchantingTableBlock.INSTANCE))
		.build();
	
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
