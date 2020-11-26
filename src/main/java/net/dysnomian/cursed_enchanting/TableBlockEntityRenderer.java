package net.dysnomian.cursed_enchanting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TableBlockEntityRenderer extends BlockEntityRenderer<TableBlockEntity> {

	private static ItemStack bookStack = new ItemStack(Items.BOOK, 1);

	private static net.minecraft.block.EnchantingTableBlock enchantingTable = (net.minecraft.block.EnchantingTableBlock)net.minecraft.block.Blocks.ENCHANTING_TABLE;
 
    public TableBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }
 
    @Override
	public void render(TableBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		
		final double time = blockEntity.getWorld().getTime() + tickDelta;
		manipulateRenderMatices(time, matrices);
  
		// use the light from one block above instead of from inside our block.
		int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
		MinecraftClient.getInstance().getItemRenderer().renderItem(bookStack, ModelTransformation.Mode.GROUND, lightAbove, overlay, matrices, vertexConsumers);
  
		// Mandatory call after GL calls
		matrices.pop();
	 }

	private void manipulateRenderMatices(double time, MatrixStack matrices) {
		final float x = 0.5f;
		final float y = 0.95f;
		final float z = 0.5f;
		final float max_amplitude = 0.15f;
		final float vertical_speed_factor = 0.1f;
		final float rotation_speed_factor = 4f;
  
		// divide `time` to slow movement down, divide after taking sin to reduce amplitude of movement.
		final double y_offset = Math.sin(time * vertical_speed_factor) * max_amplitude;
  
		// move (up and down)
		matrices.translate(x, y + y_offset, z);
  
		// rotate (about y axis)
		final float angle = (float)(time * rotation_speed_factor);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(angle));
	}
}
