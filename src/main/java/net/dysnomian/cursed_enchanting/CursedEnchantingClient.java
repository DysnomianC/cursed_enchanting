package net.dysnomian.cursed_enchanting;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class CursedEnchantingClient implements ClientModInitializer {
	@Override
    public void onInitializeClient() {
		// Here we will put client-only registration code
		BlockEntityRendererRegistry.INSTANCE.register(CursedEnchantingTableBlockEntity.BLOCK_ENTITY_TYPE, CursedEnchantingTableBlockEntityRenderer::new);
    }	
}
