package net.dysnomian.cursed_enchanting;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

public class CursedEnchantingMain implements ModInitializer {
	
	public static ScreenHandlerType<TableGuiDescription> TABLE_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerSimple(
		TableGuiDescription.ID,
		(syncId, inventory) -> new TableGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!");

		Registry.register(Registry.BLOCK, TableBlock.ID, TableBlock.INSTANCE);
		Registry.register(Registry.ITEM, TableBlock.ID, TableBlock.INSTANCE_ITEM);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, TableBlockEntity.ID, TableBlockEntity.BLOCK_ENTITY_TYPE);
	}
}
