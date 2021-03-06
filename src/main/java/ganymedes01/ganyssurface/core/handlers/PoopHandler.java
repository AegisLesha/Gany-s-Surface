package ganymedes01.ganyssurface.core.handlers;

import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.ModBlocks;
import ganymedes01.ganyssurface.ModItems;
import ganymedes01.ganyssurface.core.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class PoopHandler {

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if (event.entityLiving.worldObj.isRemote)
			return;

		if (event.entityLiving instanceof EntityAnimal || event.entityLiving instanceof EntityTameable || event.entityLiving instanceof EntityBat)
			if (event.entityLiving.worldObj.rand.nextInt(GanysSurface.poopingChance) == 0)
				if (!event.entityLiving.isChild()) {
					if (hasPoopNearby(event.entityLiving.worldObj, (int) event.entityLiving.posX, (int) event.entityLiving.posY, (int) event.entityLiving.posZ))
						return;
					if (event.entityLiving instanceof EntityBat)
						replaceNearbyAirBlock(event.entityLiving.worldObj, (int) event.entityLiving.posX, (int) event.entityLiving.posY, (int) event.entityLiving.posZ, ModBlocks.poop, 1);
					else
						replaceNearbyAirBlock(event.entityLiving.worldObj, (int) event.entityLiving.posX, (int) event.entityLiving.posY, (int) event.entityLiving.posZ, ModBlocks.poop, 0);
					event.entityLiving.playSound("mob.chicken.plop", 1.0F, (event.entityLiving.worldObj.rand.nextFloat() - event.entityLiving.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
				}
	}

	private void replaceNearbyAirBlock(World world, int x, int y, int z, Block block, int meta) {
		for (int i = -1; i < 2; i++)
			for (int k = -1; k < 2; k++)
				if (world.isAirBlock(x + i, y, z + k) || world.getBlock(x + i, y, z + k).isReplaceable(world, x + i, y, z + k)) {
					world.setBlock(x + i, y, z + k, block, meta, 3);
					ModBlocks.poop.onNeighborBlockChange(world, x + i, y, z + k, block);
					return;
				}
		InventoryUtils.dropStack(world, x, y, z, new ItemStack(ModItems.poop, 1, meta));
	}

	private boolean hasPoopNearby(World world, int x, int y, int z) {
		int count = 0;
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++)
				for (int k = -1; k <= 1; k++)
					if (world.getBlock(x + i, y + j, z + k) == ModBlocks.poop)
						count++;
		return count > 4;
	}
}