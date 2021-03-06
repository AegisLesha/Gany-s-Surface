package ganymedes01.ganyssurface.client.renderer.tileentity;

import ganymedes01.ganyssurface.tileentities.TileEntityDualWorkTable;
import ganymedes01.ganyssurface.tileentities.TileEntityWorkTable;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

@SideOnly(Side.CLIENT)
public class TileEntityWorkTableRender extends TileEntitySpecialRenderer {

	private final RenderItem itemRenderer;

	public TileEntityWorkTableRender() {
		itemRenderer = new RenderItem() {

			@Override
			public boolean shouldBob() {
				return false;
			}

			@Override
			public byte getMiniBlockCount(ItemStack stack, byte original) {
				return 1;
			}

			@Override
			public byte getMiniItemCount(ItemStack stack, byte original) {
				return 1;
			}
		};
		itemRenderer.setRenderManager(RenderManager.instance);
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float angle) {
		renderCraftingGrid((TileEntityWorkTable) tile, (float) x, (float) y, (float) z, 0);

		if (tile instanceof TileEntityDualWorkTable)
			renderCraftingGrid((TileEntityDualWorkTable) tile, (float) x, (float) y - 0.74F, (float) z, 9);
	}

	private void renderCraftingGrid(TileEntityWorkTable workTable, float x, float y, float z, int start) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				GL11.glPushMatrix();
				ItemStack stack = workTable.getStackInSlot(start + j + i * 3);
				if (stack != null) {
					float scaleFactor;
					float rotateAngle;
					if (stack.getItem() instanceof ItemBlock) {
						scaleFactor = 0.5F;
						rotateAngle = 0.0F;
					} else {
						scaleFactor = 0.5F * 0.6F;
						rotateAngle = -90.0F;
					}

					EntityItem entityItem = workTable.getEntityItem();
					entityItem.setEntityItemStack(stack);
					switch (workTable.getBlockMetadata()) {
						case 2:
							GL11.glTranslatef(x - j * 0.19F + 0.69F, y + 1.05F, z + 0.69F - i * 0.19F);
							GL11.glRotatef(rotateAngle + 90.0F, 0.0F, 1.0F, 0.0F);
							break;
						case 3:
							GL11.glTranslatef(x + 0.313F + j * 0.19F, y + 1.05F, z + 0.313F + i * 0.19F);
							GL11.glRotatef(rotateAngle + 270.0F, 0.0F, 1.0F, 0.0F);
							break;
						case 4:
							GL11.glTranslatef(x + 0.69F - i * 0.19F, y + 1.05F, z + 0.313F + j * 0.19F);
							GL11.glRotatef(rotateAngle + 180.0F, 0.0F, 1.0F, 0.0F);
							break;
						case 5:
							GL11.glTranslatef(x + 0.313F + i * 0.19F, y + 1.05F, z + 0.69F - j * 0.19F);
							GL11.glRotatef(rotateAngle + 0.0F, 0.0F, 1.0F, 0.0F);
							break;
						default:
							GL11.glTranslatef(x - j * 0.19F + 0.69F, y + 1.05F, z + 0.69F - i * 0.19F);
							GL11.glRotatef(rotateAngle + 90.0F, 0.0F, 1.0F, 0.0F);
							break;
					}
					GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

					itemRenderer.doRender(entityItem, 0, 0, 0, 0, 0);
				}
				GL11.glPopMatrix();
			}
	}
}