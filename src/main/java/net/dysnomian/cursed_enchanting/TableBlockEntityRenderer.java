package net.dysnomian.cursed_enchanting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class TableBlockEntityRenderer extends BlockEntityRenderer<TableBlockEntity> {

	private static ItemStack bookStack = new ItemStack(Items.BOOK, 1);

	public TableBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }
 
    @Override
	public void render(TableBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (blockEntity.GO_FOR_FANCY) {
			renderFancyBook(blockEntity, tickDelta, matrices, vertexConsumers, light, overlay);
		}
		else {
			renderBoringBook(blockEntity, tickDelta, matrices, vertexConsumers, light, overlay);
		}
		
	}

	private void renderBoringBook(TableBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		
		final double time = blockEntity.getWorld().getTime() + tickDelta;
  
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

		// use the light from one block above instead of from inside our block.
		int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
		MinecraftClient.getInstance().getItemRenderer().renderItem(bookStack, ModelTransformation.Mode.GROUND, lightAbove, overlay, matrices, vertexConsumers);
  
		// Mandatory call after GL calls
		matrices.pop();
	}
	
	private final BookModel book = new BookModel();

	public static final SpriteIdentifier BOOK_TEXTURE;

	// copied from minecraft's EnchantingTableBlockEntityRenderer
	public void renderFancyBook(TableBlockEntity tableBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		  matrixStack.push();
		  matrixStack.translate(0.5D, 0.75D, 0.5D);
		  float g = (float)tableBlockEntity.ticks + f;
		  matrixStack.translate(0.0D, (double)(0.1F + MathHelper.sin(g * 0.1F) * 0.01F), 0.0D);
	
		  float h;
		  for(h = tableBlockEntity.nextAngularOffset - tableBlockEntity.angularOffset; h >= 3.1415927F; h -= 6.2831855F) {
		  }
	
		  while(h < -3.1415927F) {
			 h += 6.2831855F;
		  }
	
		  float k = tableBlockEntity.angularOffset + h * f;
		  matrixStack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(-k));
		  matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(80.0F));
		  float l = MathHelper.lerp(f, tableBlockEntity.pageAngle, tableBlockEntity.nextPageAngle);
		  float m = MathHelper.fractionalPart(l + 0.25F) * 1.6F - 0.3F;
		  float n = MathHelper.fractionalPart(l + 0.75F) * 1.6F - 0.3F;
		  float o = MathHelper.lerp(f, tableBlockEntity.pageTurningSpeed, tableBlockEntity.nextPageTurningSpeed);
		  this.book.setPageAngles(g, MathHelper.clamp(m, 0.0F, 1.0F), MathHelper.clamp(n, 0.0F, 1.0F), o);
		  VertexConsumer vertexConsumer = BOOK_TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
		  this.book.method_24184(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
		  matrixStack.pop();
	   }
	
	   static {
		  BOOK_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("entity/enchanting_table_book"));
		  // "cursed_enchanting:entity/cursed_enchanting_table_book" doesn't work *sadface*
	   }
}
