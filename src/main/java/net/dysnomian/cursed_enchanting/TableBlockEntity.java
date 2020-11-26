package net.dysnomian.cursed_enchanting;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class TableBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
   public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
   public static final BlockEntityType<TableBlockEntity> BLOCK_ENTITY_TYPE = BlockEntityType.Builder
         .create(TableBlockEntity::new, TableBlock.INSTANCE).build(null);

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

   @Override
   public Text getDisplayName() {
      return new TranslatableText(getCachedState().getBlock().getTranslationKey());
   }

   @Override
   public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
      return new TableGuiDescription(syncId, inv, ScreenHandlerContext.create(world, pos));
   }
}