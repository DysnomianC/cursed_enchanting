package net.dysnomian.cursed_enchanting;

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
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;

public class TableBlock extends Block implements BlockEntityProvider {
	// strength() sets blast resistance and hardness.
	private static final int miningLevel = 0; // 0 - wood/gold
	private static final FabricBlockSettings tableSettings = FabricBlockSettings.of(Material.STONE)
		.breakByTool(FabricToolTags.PICKAXES, miningLevel)
		.requiresTool()
		.sounds(BlockSoundGroup.STONE)
		.strength(3.5f);
	private static final VoxelShape SHAPE = Block.createCuboidShape(0D, 0D, 0D, 16D, 12D, 16D);
		
	public static final Identifier ID = new Identifier("cursed_enchanting", "cursed_enchanting_table");
	public static final TableBlock INSTANCE = new TableBlock(tableSettings);
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
		return SHAPE;
	}

	@Override
	public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
		if (world.isClient) return ActionResult.SUCCESS;
		
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (!(blockEntity instanceof TableBlockEntity))
		{
			// handle this case?
			System.err.println("BlockEntity for a `TableBlock` isn't a `TableBlockEntity`");
		}
		TableBlockEntity tableBlockEntity = (TableBlockEntity)blockEntity;
 
 
        if (player.getStackInHand(hand).isEmpty()) {
			// If the player is not holding anything we'll get give them the items in the block entity one by one
 
             // Find the first slot that has an item and give it to the player
			 if (!tableBlockEntity.getStack(1).isEmpty()) {
                // Give the player the stack in the inventory
                player.inventory.offerOrDrop(world, tableBlockEntity.getStack(1));
                // Remove the stack from the inventory
                tableBlockEntity.removeStack(1);
            } else if (!tableBlockEntity.getStack(0).isEmpty()) {
                player.inventory.offerOrDrop(world, tableBlockEntity.getStack(0));
                tableBlockEntity.removeStack(0);
            } else {
				// if the inventory is empty do the arm swinging animation
				return ActionResult.SUCCESS;
			}
		}
		else
		{
            // Check what is the first open slot and put an item from the player's hand there
            if (tableBlockEntity.getStack(0).isEmpty()) {
                // Put the stack the player is holding into the inventory
                tableBlockEntity.setStack(0, player.getStackInHand(hand).copy());
                // Remove the stack from the player's hand
                player.getStackInHand(hand).setCount(0);
            } else if (tableBlockEntity.getStack(1).isEmpty()) {
                tableBlockEntity.setStack(1, player.getStackInHand(hand).copy());
                player.getStackInHand(hand).setCount(0);
            } else {
                // If the inventory is full we play the arm swinging animation
                return ActionResult.SUCCESS;
            }
		} 
		
		// make sure to mark as dirty after interacting with the inventory.
		tableBlockEntity.markDirty();
        return ActionResult.CONSUME;
	}
	
}