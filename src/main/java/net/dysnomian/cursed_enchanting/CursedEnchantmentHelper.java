package net.dysnomian.cursed_enchanting;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.google.common.collect.Lists;

import org.jetbrains.annotations.Nullable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Util;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.registry.Registry;

/** 
 * Wraps calls to EnchantmentHelper. Adds helpers for when cursing enchantments.
*/
public class CursedEnchantmentHelper {
	/**
    * Gets the level of an enchantment on an item stack.
    */
	public static int getLevel(Enchantment enchantment, ItemStack stack) {
		return EnchantmentHelper.getLevel(enchantment, stack);
	}

	/**
	* Gets the enchantments on an item stack.
	* 
	* <p>For enchanted books, it retrieves from the item stack's stored than
	* regular enchantments.
	* 
	* @see net.minecraft.item.ItemStack#getEnchantments()
	* @see net.minecraft.item.EnchantedBookItem#getEnchantmentTag(net.minecraft.item.ItemStack)
	*/
	public static Map<Enchantment, Integer> get(ItemStack stack) {
		return EnchantmentHelper.get(stack);
	}

	/**
	 * Loads enchantments from an NBT list.
	*/
	public static Map<Enchantment, Integer> fromTag(ListTag tag) {
		return EnchantmentHelper.fromTag(tag);
	}

	/**
	 * Sets the enchantments on an item stack.
	* 
	* <p>For enchanted books, it sets the enchantments to the item stack's
	* stored enchantments than regular enchantments.
	* 
	* @see net.minecraft.item.ItemStack#getEnchantments()
	* @see net.minecraft.item.EnchantedBookItem#getEnchantmentTag(net.minecraft.item.ItemStack)
	*/
	public static void set(Map<Enchantment, Integer> enchantments, ItemStack stack) {
		EnchantmentHelper.set(enchantments, stack);
	}

	public static int getProtectionAmount(Iterable<ItemStack> equipment, DamageSource source) {
		return EnchantmentHelper.getProtectionAmount(equipment, source);
	}

	public static float getAttackDamage(ItemStack stack, EntityGroup group) {
		return EnchantmentHelper.getAttackDamage(stack, group);
	}

	public static float getSweepingMultiplier(LivingEntity entity) {
		return EnchantmentHelper.getSweepingMultiplier(entity);
	}

	public static void onUserDamaged(LivingEntity user, Entity attacker) {
		EnchantmentHelper.onUserDamaged(user, attacker);
	}

	public static void onTargetDamaged(LivingEntity user, Entity target) {
		EnchantmentHelper.onTargetDamaged(user, target);
	}

	/**
	 * Returns the highest level of the passed enchantment in the enchantment's
	* applicable equipment slots' item stacks.
	* 
	* @param enchantment the enchantment
	* @param entity the entity whose equipment slots are checked
	*/
	public static int getEquipmentLevel(Enchantment enchantment, LivingEntity entity) {
		return EnchantmentHelper.getEquipmentLevel(enchantment, entity);
	}


	// TODO replace below with calls to EnchantmentHelper instead of `this`.

	public static int getKnockback(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.KNOCKBACK, entity);
	}

	public static int getFireAspect(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.FIRE_ASPECT, entity);
	}

	public static int getRespiration(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.RESPIRATION, entity);
	}

	public static int getDepthStrider(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.DEPTH_STRIDER, entity);
	}

	public static int getEfficiency(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.EFFICIENCY, entity);
	}

	public static int getLuckOfTheSea(ItemStack stack) {
		return getLevel(Enchantments.LUCK_OF_THE_SEA, stack);
	}

	public static int getLure(ItemStack stack) {
		return getLevel(Enchantments.LURE, stack);
	}

	public static int getLooting(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.LOOTING, entity);
	}

	public static boolean hasAquaAffinity(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.AQUA_AFFINITY, entity) > 0;
	}

	public static boolean hasFrostWalker(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.FROST_WALKER, entity) > 0;
	}

	public static boolean hasSoulSpeed(LivingEntity entity) {
		return getEquipmentLevel(Enchantments.SOUL_SPEED, entity) > 0;
	}

	public static boolean hasBindingCurse(ItemStack stack) {
		return getLevel(Enchantments.BINDING_CURSE, stack) > 0;
	}

	public static boolean hasVanishingCurse(ItemStack stack) {
		return getLevel(Enchantments.VANISHING_CURSE, stack) > 0;
	}

	public static int getLoyalty(ItemStack stack) {
		return getLevel(Enchantments.LOYALTY, stack);
	}

	public static int getRiptide(ItemStack stack) {
		return getLevel(Enchantments.RIPTIDE, stack);
	}

	public static boolean hasChanneling(ItemStack stack) {
		return getLevel(Enchantments.CHANNELING, stack) > 0;
	}

	/**
	 * Returns a pair of an equipment slot and the item stack in the supplied
	* entity's slot, indicating the item stack has the enchantment supplied.
	* 
	* <p>If multiple equipment slots' item stacks are valid, a random pair is
	* returned.
	* 
	* @param enchantment the enchantment the equipped item stack must have
	* @param entity the entity to choose equipments from
	*/
	@Nullable
	public static Entry<EquipmentSlot, ItemStack> chooseEquipmentWith(Enchantment enchantment, LivingEntity entity) {
		return EnchantmentHelper.chooseEquipmentWith(enchantment, entity);
	}

	/**
	 * Returns a pair of an equipment slot and the item stack in the supplied
	* entity's slot, indicating the item stack has the enchantment supplied
	* and fulfills the extra condition.
	* 
	* <p>If multiple equipment slots' item stacks are valid, a random pair is
	* returned.
	* 
	* @param enchantment the enchantment the equipped item stack must have
	* @param entity the entity to choose equipments from
	* @param condition extra conditions for the item stack to pass for selection
	*/
	@Nullable
	public static Entry<EquipmentSlot, ItemStack> chooseEquipmentWith(Enchantment enchantment, LivingEntity entity, Predicate<ItemStack> condition) {
		return EnchantmentHelper.chooseEquipmentWith(enchantment, entity, condition);
	}
  
	 /**
	  * Returns the required experience level for an enchanting option in the
	  * enchanting table's screen, or the enchantment screen.
	  * 
	  * @param random the random, which guarantees consistent results with the same seed
	  * @param slotIndex the index of the enchanting option
	  * @param bookshelfCount the number of bookshelves
	  * @param stack the item stack to enchant
	  */
	 public static int calculateRequiredExperienceLevel(Random random, int slotIndex, int bookshelfCount, ItemStack stack) {
		return EnchantmentHelper.calculateRequiredExperienceLevel(random, slotIndex, bookshelfCount, stack);
	 }
  
	/**
	 * Enchants the {@code target} item stack and returns it.
	* 
	* @param random the seed
	* @param target the item stack to enchant
	* @param level the experience level
	* @param treasureAllowed whether treasure enchantments may appear
	*/
	public static ItemStack enchant(Random random, ItemStack target, int level, boolean treasureAllowed) {
		return EnchantmentHelper.enchant(random, target, level, treasureAllowed);
	}

	/**
	 * Generate the enchantments for enchanting the {@code stack}.
	*/
	public static List<EnchantmentLevelEntry> generateEnchantments(Random random, ItemStack stack, int level, boolean treasureAllowed) {
		List<EnchantmentLevelEntry> enchantments = EnchantmentHelper.generateEnchantments(random, stack, level, treasureAllowed);
		
		int levelCursed = Math.max(level, 25); // always use at least level 25 for generating curses, vanilla curses don't show up below 25.

		List<EnchantmentLevelEntry> cursedEntries = getPossibleCursedEntries(levelCursed, stack);
		if (!cursedEntries.isEmpty()) {
			int originalEnchantmentCount = enchantments.size();
			int enchantmentCount;
			int sanity = 100;
			do {
				EnchantmentLevelEntry cursedEntry = WeightedPicker.getRandom(random, cursedEntries);
				cursedEntries.remove(cursedEntry);
				enchantments.add(cursedEntry);
				removeConflicts(cursedEntries, (EnchantmentLevelEntry)Util.getLast(enchantments));
				enchantmentCount = enchantments.size();
				--sanity;
			} while (enchantmentCount == originalEnchantmentCount && !cursedEntries.isEmpty() && sanity > 0);
		}
		return enchantments;
	}

	/**
	 * Remove entries conflicting with the picked entry from the possible
	* entries.
	* 
	* @param possibleEntries the possible entries
	* @param pickedEntry the picked entry
	*/
	public static void removeConflicts(List<EnchantmentLevelEntry> possibleEntries, EnchantmentLevelEntry pickedEntry) {
		EnchantmentHelper.removeConflicts(possibleEntries, pickedEntry);  
	}
  
	/**
	 * Returns whether the {@code candidate} enchantment is compatible with the
	* {@code existing} enchantments.
	*/
	public static boolean isCompatible(Collection<Enchantment> existing, Enchantment candidate) {
		return EnchantmentHelper.isCompatible(existing, candidate);
	}
  
	/**
	 * Gets all the possible entries for enchanting the {@code stack} at the
	* given {@code power}.
	*/
	public static List<EnchantmentLevelEntry> getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed) {
		List<EnchantmentLevelEntry> entries = EnchantmentHelper.getPossibleEntries(power, stack, treasureAllowed);

		Iterator<Enchantment> enchantmentIterator = Registry.ENCHANTMENT.iterator();
		while (true) {
			Enchantment enchantment;
			do {
				if (!enchantmentIterator.hasNext()) {
				return entries;
				}

				enchantment = enchantmentIterator.next();
			} while(!enchantment.isCursed());

			for(int level = enchantment.getMaxLevel(); level > enchantment.getMinLevel() - 1; --level) {
				if (power >= enchantment.getMinPower(level) && power <= enchantment.getMaxPower(level)) {
					entries.add(new EnchantmentLevelEntry(enchantment, level));
					break;
				}
			}
		}
	}

	public static List<EnchantmentLevelEntry> getPossibleCursedEntries(int power, ItemStack stack)
	{
		List<EnchantmentLevelEntry> entries = Lists.newArrayList();
		Item item = stack.getItem();
		boolean isBook = stack.getItem() == Items.BOOK;

		List<Enchantment> allEnchantments = Lists.newArrayList();

		Iterator<Enchantment> enchantmentIterator = Registry.ENCHANTMENT.iterator();
		while (true) {
			Enchantment enchantment;
			do {
				do {
					do {
						if (!enchantmentIterator.hasNext()) {
							return entries;
						}

						enchantment = enchantmentIterator.next();
						allEnchantments.add(enchantment);
					} while(!enchantment.isCursed());
				} while(!enchantment.isAvailableForRandomSelection());
			} while(!enchantment.type.isAcceptableItem(item) && !isBook);

			for(int level = enchantment.getMaxLevel(); level > enchantment.getMinLevel() - 1; --level) {
				if (power >= enchantment.getMinPower(level) && power <= enchantment.getMaxPower(level)) {
					entries.add(new EnchantmentLevelEntry(enchantment, level));
					break;
				}
			}
		}
	}
  
	@FunctionalInterface
	interface Consumer {
		void accept(Enchantment enchantment, int level);
	}
}