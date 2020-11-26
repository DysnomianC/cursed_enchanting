package net.dysnomian.cursed_enchanting;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

public class TableBlockEntity extends BlockEntity {
   
   public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
   public static final BlockEntityType<TableBlockEntity> BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(TableBlockEntity::new, TableBlock.INSTANCE).build(null);

   public TableBlockEntity() {
      super(BLOCK_ENTITY_TYPE);
   }
}