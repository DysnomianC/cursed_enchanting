package net.dysnomian.cursed_enchanting;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemGroup;

public class CursedEnchantingTableBlock extends Block {
	public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
	public static final CursedEnchantingTableBlock INSTANCE = new CursedEnchantingTableBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0f));
	public static final BlockItem INSTANCE_ITEM = new BlockItem(CursedEnchantingTableBlock.INSTANCE, new Item.Settings().group(ItemGroup.DECORATIONS));

	public static final BooleanProperty SOFTENED = BooleanProperty.of("softened");

	public CursedEnchantingTableBlock(FabricBlockSettings hardness) {
		super(hardness);
		// setDefaultState(getStateManager().getDefaultState().with(SOFTENED, false));
	}
}