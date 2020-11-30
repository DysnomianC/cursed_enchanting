package net.dysnomian.cursed_enchanting;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CursedEnchantmentScreenHandler extends ScreenHandler {
	private static final int TOOL_SLOT_INDEX = 0;
	public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchantment");
	
	private final ScreenHandlerContext context;
	private final Random random = new Random();
	private final Property seed = Property.create();
	private final Inventory inventory = new SimpleInventory(2) {
		public void markDirty() {
		   super.markDirty();
		   onContentChanged(this);
		}
	};;
	public final int[] enchantmentPower = new int[3];
	public final int[] enchantmentId = new int[]{-1, -1, -1};
	public final int[] enchantmentLevel = new int[]{-1, -1, -1};

	public CursedEnchantmentScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY);		
	}

	public CursedEnchantmentScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(CursedEnchantingMain.TABLE_SCREEN_HANDLER_TYPE_2, syncId);

		this.context = context;
		
		// slots for the two enchanting inputs
		this.addSlot(new Slot(this.inventory, TOOL_SLOT_INDEX, 15, 47) {
			public boolean canInsert(ItemStack stack) {
			   return true;
			}
   
			public int getMaxItemCount() {
			   return 1;
			}
		});
		this.addSlot(new Slot(this.inventory, 1, 35, 47) {
			public boolean canInsert(ItemStack stack) {
				return stack.getItem() == Items.REDSTONE;
			}
		});


		// slots for the player's inventory
		int k;
		for(k = 0; k < 3; ++k) {
			for(int j = 0; j < 9; ++j) {
			this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
			}
		}

		for(k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
		}

		// properties
		this.addProperty(Property.create((int[])this.enchantmentPower, 0));
		this.addProperty(Property.create((int[])this.enchantmentPower, 1));
		this.addProperty(Property.create((int[])this.enchantmentPower, 2));
		this.addProperty(this.seed).set(playerInventory.player.getEnchantmentTableSeed());
		this.addProperty(Property.create((int[])this.enchantmentId, 0));
		this.addProperty(Property.create((int[])this.enchantmentId, 1));
		this.addProperty(Property.create((int[])this.enchantmentId, 2));
		this.addProperty(Property.create((int[])this.enchantmentLevel, 0));
		this.addProperty(Property.create((int[])this.enchantmentLevel, 1));
		this.addProperty(Property.create((int[])this.enchantmentLevel, 2));
	}
	
	@Override
	public void onContentChanged(Inventory inventory) {
		if (inventory == this.inventory) {
			ItemStack itemStack = inventory.getStack(0);
			if (!itemStack.isEmpty() && itemStack.isEnchantable()) {
			   this.context.run((world, blockPos) -> {

					int pretnedBookshelfCount = 8; // number chosen after a bit of testing.
				   
					this.random.setSeed((long)this.seed.get());
					int slotIndex;
					for(slotIndex = 0; slotIndex < 3; ++slotIndex) {
						this.enchantmentPower[slotIndex] = CursedEnchantmentHelper.calculateRequiredExperienceLevel(this.random, slotIndex, pretnedBookshelfCount, itemStack);
						this.enchantmentId[slotIndex] = -1;
						this.enchantmentLevel[slotIndex] = -1;
						if (this.enchantmentPower[slotIndex] < slotIndex + 1) {
							this.enchantmentPower[slotIndex] = 0;
						}
					}
   
					for(slotIndex = 0; slotIndex < 3; ++slotIndex) {
						if (this.enchantmentPower[slotIndex] > 0) {
							List<EnchantmentLevelEntry> list = this.generateEnchantments(itemStack, slotIndex, this.enchantmentPower[slotIndex]);
							if (list != null && !list.isEmpty()) {
								EnchantmentLevelEntry enchantmentLevelEntry = (EnchantmentLevelEntry)list.get(this.random.nextInt(list.size()));
								this.enchantmentId[slotIndex] = net.minecraft.util.registry.Registry.ENCHANTMENT.getRawId(enchantmentLevelEntry.enchantment);
								this.enchantmentLevel[slotIndex] = enchantmentLevelEntry.level;
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

	public boolean onButtonClick(PlayerEntity player, int buttonId) {
		ItemStack toolToEnchant = this.inventory.getStack(0);
		ItemStack redstoneStack = this.inventory.getStack(1);
		int slotNum = buttonId + 1;
		if ((redstoneStack.isEmpty() || redstoneStack.getCount() < slotNum) && !player.abilities.creativeMode) {
			return false;
		} else if (this.enchantmentPower[buttonId] <= 0 || toolToEnchant.isEmpty() || (player.experienceLevel < slotNum || player.experienceLevel < this.enchantmentPower[buttonId]) && !player.abilities.creativeMode) {
			return false;
		} else {
			this.context.run((world, blockPos) -> {
				ItemStack enchantedTool = toolToEnchant;
				List<EnchantmentLevelEntry> enchantments = this.generateEnchantments(toolToEnchant, buttonId, this.enchantmentPower[buttonId]);
				if (!enchantments.isEmpty()) {
					// instead of costing levels, we cost the player health.
					//player.applyEnchantmentCosts(toolToEnchant, slotNum);
					int damage_amount = slotNum * 2;
					DamageSource damage_source = DamageSource.MAGIC; // TODO add custom damage source with specific death message.
					player.damage(damage_source, damage_amount);
					

					boolean bl = toolToEnchant.getItem() == Items.BOOK;
					if (bl) {
						enchantedTool = new ItemStack(Items.ENCHANTED_BOOK);
						CompoundTag compoundTag = toolToEnchant.getTag();
						if (compoundTag != null) {
							enchantedTool.setTag(compoundTag.copy());
						}
						
						this.inventory.setStack(TOOL_SLOT_INDEX, enchantedTool);
					}

					for(int k = 0; k < enchantments.size(); ++k) {
						EnchantmentLevelEntry ele = enchantments.get(k);
						if (bl) {
							EnchantedBookItem.addEnchantment(enchantedTool, ele);
						} else {
							enchantedTool.addEnchantment(ele.enchantment, ele.level);
						}
					}

					if (!player.abilities.creativeMode) {
						redstoneStack.decrement(slotNum);
						if (redstoneStack.isEmpty()) {
							this.inventory.setStack(1, ItemStack.EMPTY);
						}
					}

					player.incrementStat(Stats.ENCHANT_ITEM);
					if (player instanceof ServerPlayerEntity) {
						Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, enchantedTool, slotNum);
					}

					this.inventory.markDirty();
					this.seed.set(player.getEnchantmentTableSeed());
					this.onContentChanged(this.inventory);
					world.playSound((PlayerEntity)null, blockPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
				}

			});
			return true;
		}
	}
	
	private List<EnchantmentLevelEntry> generateEnchantments(ItemStack stack, int slot, int level) {
		this.random.setSeed((long)(this.seed.get() + slot));
		List<EnchantmentLevelEntry> list = CursedEnchantmentHelper.generateEnchantments(this.random, stack, level, false);
		if (stack.getItem() == Items.BOOK && list.size() > 1) {
		   list.remove(this.random.nextInt(list.size()));
		}
  
		return list;
	}

	@Environment(EnvType.CLIENT)
	public int getRedstoneCount() {
		// Actually return redstone count but method signature is required to match use by `EnchantmentScreen`.
		ItemStack itemStack = this.inventory.getStack(1);
		return itemStack.isEmpty() ? 0 : itemStack.getCount();
	}

	@Environment(EnvType.CLIENT)
	public int getSeed() {
		return this.seed.get();
	}

	public void close(PlayerEntity player) {
		super.close(player);
		this.context.run((world, blockPos) -> {
			this.dropInventory(player, player.world, this.inventory);
		});
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return canUse(context, player, TableBlock.INSTANCE);
	}

	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(index);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (index == 0) {
				if (!this.insertItem(itemStack2, 2, 38, true)) {
				return ItemStack.EMPTY;
				}
			} else if (index == 1) {
				if (!this.insertItem(itemStack2, 2, 38, true)) {
				return ItemStack.EMPTY;
				}
			} else if (itemStack2.getItem() == Items.REDSTONE) {
				if (!this.insertItem(itemStack2, 1, 2, true)) {
				return ItemStack.EMPTY;
				}
			} else {
				if (((Slot)this.slots.get(0)).hasStack() || !((Slot)this.slots.get(0)).canInsert(itemStack2)) {
				return ItemStack.EMPTY;
				}

				ItemStack itemStack3 = itemStack2.copy();
				itemStack3.setCount(1);
				itemStack2.decrement(1);
				((Slot)this.slots.get(0)).setStack(itemStack3);
			}

			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, itemStack2);
		}

		return itemStack;
	}
}

