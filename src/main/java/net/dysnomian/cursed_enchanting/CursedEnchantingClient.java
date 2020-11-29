package net.dysnomian.cursed_enchanting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.screen.ScreenHandlerType;

public class CursedEnchantingClient implements ClientModInitializer {
	@Override
    public void onInitializeClient() {
		// Here we will put client-only registration code
		BlockEntityRendererRegistry.INSTANCE.register(TableBlockEntity.BLOCK_ENTITY_TYPE, TableBlockEntityRenderer::new);

		ScreenRegistry.<TableGuiDescription, TableBlockScreen>register(
			CursedEnchantingMain.TABLE_SCREEN_HANDLER_TYPE,
			(gui, inventory, title) -> new TableBlockScreen(gui, inventory.player, title)
		);

		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) ->
		{
			registry.register(TableBlockEntityRenderer.BOOK_SPRITE_ID);
		});
		
		ScreenRegistry.<CursedEnchantmentScreenHandler, CursedEnchantmentScreen>register(
			CursedEnchantingMain.TABLE_SCREEN_HANDLER_TYPE_2,
			(gui, inventory, title) -> new CursedEnchantmentScreen(gui, inventory, title)
		);

		// ScreenRegistry.register(
		// 	CursedEnchantingMain.TABLE_SCREEN_HANDLER_TYPE_2,
		// 	CursedEnchantmentScreen::new
		// );

		// register(ScreenHandlerType.ENCHANTMENT, EnchantmentScreen::new);
		// {
		// 	ScreenHandlerType<? extends M> type = ScreenHandlerType.ENCHANTMENT;
		// 	HandledScreens.Provider<M, U> provider = CursedEnchantmentScreen::new;
		// 	HandledScreens.Provider<?, ?> provider2 = (HandledScreens.Provider)PROVIDERS.put(type, provider);
		// }
    }	
}
