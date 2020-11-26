package net.dysnomian.cursed_enchanting;

import java.util.Random;

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
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;

public class TableBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory, Tickable {
   public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
   public static final BlockEntityType<TableBlockEntity> BLOCK_ENTITY_TYPE = BlockEntityType.Builder
         .create(TableBlockEntity::new, TableBlock.INSTANCE).build(null);

   private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
   private static final Random RANDOM = new Random();

   public final boolean GO_FOR_FANCY = RANDOM.nextBoolean();

   public TableBlockEntity() {
      super(BLOCK_ENTITY_TYPE);
   }

   // fields and tick method copied from minecraft's `EnchantingTableBlockEntity`, used bt the entity renderer to render the fancy book
   public int ticks;
   public float nextPageAngle;
   public float pageAngle;
   public float field_11969;
   public float field_11967;
   public float nextPageTurningSpeed;
   public float pageTurningSpeed;
   public float nextAngularOffset;
   public float angularOffset;
   public float angularPosition;

   public void tick() {
      this.pageTurningSpeed = this.nextPageTurningSpeed;
      this.angularOffset = this.nextAngularOffset;
      PlayerEntity playerEntity = this.world.getClosestPlayer((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D, 3.0D, false);
      if (playerEntity != null) {
         double xDiff = playerEntity.getX() - ((double)this.pos.getX() + 0.5D);
         double zDiff = playerEntity.getZ() - ((double)this.pos.getZ() + 0.5D);
         this.angularPosition = (float)MathHelper.atan2(zDiff, xDiff); // angle the book towards the player
         this.nextPageTurningSpeed += 0.1F;
         if (this.nextPageTurningSpeed < 0.5F || RANDOM.nextInt(40) == 0) {
            float f = this.field_11969;

            do {
               this.field_11969 += (float)(RANDOM.nextInt(4) - RANDOM.nextInt(4));
            } while(f == this.field_11969);
         }
      } else {
         this.angularPosition += 0.02F;
         this.nextPageTurningSpeed -= 0.1F;
      }

      while(this.nextAngularOffset >= 3.1415927F) {
         this.nextAngularOffset -= 6.2831855F;
      }

      while(this.nextAngularOffset < -3.1415927F) {
         this.nextAngularOffset += 6.2831855F;
      }

      while(this.angularPosition >= 3.1415927F) {
         this.angularPosition -= 6.2831855F;
      }

      while(this.angularPosition < -3.1415927F) {
         this.angularPosition += 6.2831855F;
      }

      float g;
      for(g = this.angularPosition - this.nextAngularOffset; g >= 3.1415927F; g -= 6.2831855F) {
      }

      while(g < -3.1415927F) {
         g += 6.2831855F;
      }

      this.nextAngularOffset += g * 0.4F;
      this.nextPageTurningSpeed = MathHelper.clamp(this.nextPageTurningSpeed, 0.0F, 1.0F);
      ++this.ticks;
      this.pageAngle = this.nextPageAngle;
      float h = (this.field_11969 - this.nextPageAngle) * 0.4F;
      h = MathHelper.clamp(h, -0.2F, 0.2F);
      this.field_11967 += (h - this.field_11967) * 0.9F;
      this.nextPageAngle += this.field_11967;
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