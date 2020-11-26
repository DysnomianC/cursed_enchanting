package net.dysnomian.cursed_enchanting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class CursedEnchantingClient implements ClientModInitializer {
	@Override
    public void onInitializeClient() {
		// Here we will put client-only registration code
		BlockEntityRendererRegistry.INSTANCE.register(TableBlockEntity.BLOCK_ENTITY_TYPE, TableBlockEntityRenderer::new);

		ScreenRegistry.<TableGuiDescription, TableBlockScreen>register(
			CursedEnchantingMain.TABLE_SCREEN_HANDLER_TYPE,
			(gui, inventory, title) -> new TableBlockScreen(gui, inventory.player, title)
		);
    }	
}
