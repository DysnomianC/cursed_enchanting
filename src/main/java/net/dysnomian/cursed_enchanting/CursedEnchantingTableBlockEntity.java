package net.dysnomian.cursed_enchanting;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;

public class CursedEnchantingTableBlockEntity extends BlockEntity {
   
   public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
   public static final BlockEntityType<CursedEnchantingTableBlockEntity> BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(CursedEnchantingTableBlockEntity::new, CursedEnchantingTableBlock.INSTANCE).build(null);

   public CursedEnchantingTableBlockEntity() {
      super(BLOCK_ENTITY_TYPE);
   }
}