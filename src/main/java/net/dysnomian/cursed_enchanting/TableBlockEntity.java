package net.dysnomian.cursed_enchanting;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class TableBlockEntity extends BlockEntity  implements ImplementedInventory {
   public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
   public static final BlockEntityType<TableBlockEntity> BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(TableBlockEntity::new, TableBlock.INSTANCE).build(null);
   
   private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

   public TableBlockEntity() {
      super(BLOCK_ENTITY_TYPE);
   }

   @Override
   public DefaultedList<ItemStack> getItems() {
      return items;
   }

   @Override
   public void fromTag(BlockState state, CompoundTag tag) {
      super.fromTag(state, tag);
      Inventories.fromTag(tag, items);
   }
 
   @Override
   public CompoundTag toTag(CompoundTag tag) {
      super.toTag(tag);  
      Inventories.toTag(tag, items);
      return tag;
   }
}