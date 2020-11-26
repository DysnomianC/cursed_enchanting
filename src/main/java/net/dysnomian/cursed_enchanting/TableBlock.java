package net.dysnomian.cursed_enchanting;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;

public class TableBlock extends Block implements BlockEntityProvider {
	//private static Identifier dropTableId = new Identifier("cursed_enchanting", "table_drop"); // .drops(dropTableId)
	
	// strength() sets blast resistance and hardness.
	private static FabricBlockSettings SETTINGS = FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).sounds(BlockSoundGroup.STONE).strength(3.5f);
	
	public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
	public static final TableBlock INSTANCE = new TableBlock(SETTINGS);
	public static final BlockItem INSTANCE_ITEM = new BlockItem(TableBlock.INSTANCE, new Item.Settings().group(ItemGroup.DECORATIONS));

	public TableBlock(FabricBlockSettings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
	   return new TableBlockEntity();
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.75f, 1f);
	}
	
}