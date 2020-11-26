package net.dysnomian.cursed_enchanting;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;

public class TableBlock extends Block implements BlockEntityProvider {
	// strength() sets blast resistance and hardness.
	private static final int miningLevel = 0; // 0 - wood/gold
	private static final FabricBlockSettings tableSettings = FabricBlockSettings.of(Material.STONE)
		.breakByTool(FabricToolTags.PICKAXES, miningLevel)
		.requiresTool()
		.sounds(BlockSoundGroup.STONE)
		.strength(3.5f);
	private static final FabricItemSettings tableItemSettings = new FabricItemSettings().group(ItemGroup.DECORATIONS);
	private static final VoxelShape SHAPE = Block.createCuboidShape(0D, 0D, 0D, 16D, 12D, 16D);
		
	public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
	public static final TableBlock INSTANCE = new TableBlock(tableSettings);
	public static final BlockItem INSTANCE_ITEM = new BlockItem(TableBlock.INSTANCE, tableItemSettings);

	public TableBlock(FabricBlockSettings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
	   return new TableBlockEntity();
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}
	
	@Nullable
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
	   BlockEntity blockEntity = world.getBlockEntity(pos);
	   return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory)blockEntity : null;
	}
	
	@Override
	public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
		if (world.isClient) return ActionResult.SUCCESS;

		// (tut) You need a Block.createScreenHandlerFactory implementation that delegates to the block entity, such as the one from BlockWithEntity
		player.openHandledScreen(blockState.createScreenHandlerFactory(world, blockPos));
		return ActionResult.SUCCESS;
		
	}
	
}