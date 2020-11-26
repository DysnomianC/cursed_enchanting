package net.dysnomian.cursed_enchanting;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;

public class CursedEnchantmentScreenHandler extends EnchantmentScreenHandler {
	private final ScreenHandlerContext myContext;
	private final Random random = new Random();
	private final Property seed = Property.create();
	private final Inventory inventory;

	public CursedEnchantmentScreenHandler(int syncId, PlayerInventory playerInventory) {
		super(syncId, playerInventory);
		myContext = ScreenHandlerContext.EMPTY;
		inventory = new SimpleInventory(2) {
			public void markDirty() {
			   super.markDirty();
			   onContentChanged(this);
			}
		 };
		
	}

	public CursedEnchantmentScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(syncId, playerInventory, context);
		myContext = context;
		inventory = new SimpleInventory(2) {
			public void markDirty() {
			   super.markDirty();
			   onContentChanged(this);
			}
		 };
	}
	
	@Override
	public boolean canUse(PlayerEntity player) {
		return canUse(myContext, player, TableBlock.INSTANCE);
	}


	@Override
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
		if (inventory == this.inventory) {
			ItemStack itemStack = inventory.getStack(0);
			if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
				this.myContext.run((world, blockPos) -> {
					int i = 0;

					int j;
					for(j = -1; j <= 1; ++j) {
						for(int k = -1; k <= 1; ++k) {
							if ((j != 0 || k != 0) && world.isAir(blockPos.add(k, 0, j)) && world.isAir(blockPos.add(k, 1, j))) {
								if (world.getBlockState(blockPos.add(k * 2, 0, j * 2)).isOf(Blocks.BOOKSHELF)) {
								++i;
								}

								if (world.getBlockState(blockPos.add(k * 2, 1, j * 2)).isOf(Blocks.BOOKSHELF)) {
								++i;
								}

								if (k != 0 && j != 0) {
								if (world.getBlockState(blockPos.add(k * 2, 0, j)).isOf(Blocks.BOOKSHELF)) {
									++i;
								}

								if (world.getBlockState(blockPos.add(k * 2, 1, j)).isOf(Blocks.BOOKSHELF)) {
									++i;
								}

								if (world.getBlockState(blockPos.add(k, 0, j * 2)).isOf(Blocks.BOOKSHELF)) {
									++i;
								}

								if (world.getBlockState(blockPos.add(k, 1, j * 2)).isOf(Blocks.BOOKSHELF)) {
									++i;
								}
								}
							}
						}
					}

					this.random.setSeed((long)this.seed.get());

					for(j = 0; j < 3; ++j) {
						this.enchantmentPower[j] = EnchantmentHelper.calculateRequiredExperienceLevel(this.random, j, i, itemStack);
						this.enchantmentId[j] = -1;
						this.enchantmentLevel[j] = -1;
						if (this.enchantmentPower[j] < j + 1) {
							this.enchantmentPower[j] = 0;
						}
					}

					for(j = 0; j < 3; ++j) {
						if (this.enchantmentPower[j] > 0) {
							List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStack, j, this.enchantmentPower[j]);
							if (list != null && !list.isEmpty()) {
								EnchantmentLevelEntry enchantmentLevelEntry = (EnchantmentLevelEntry)list.get(this.random.nextInt(list.size()));
								this.enchantmentId[j] = net.minecraft.util.registry.Registry.ENCHANTMENT.getRawId(enchantmentLevelEntry.enchantment);
								this.enchantmentLevel[j] = enchantmentLevelEntry.level;
							}
						}
					}

					this.sendContentUpdates();
					});
			} else {
				for(int i = 0; i < 3; ++i) {
					this.enchantmentPower[i] = 0;
					this.enchantmentId[i] = -1;
					this.enchantmentLevel[i] = -1;
				}
			}
		}
	}
	
	private List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level) {
		this.random.setSeed((long)(this.seed.get() + slot));
		List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(this.random, stack, level, false);
		if (stack.getItem() == Items.BOOK && list.size() > 1) {
		   list.remove(this.random.nextInt(list.size()));
		}
  
		return list;
	 }
}

