package ganymedes01.ganyssurface.core.utils;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

/**
 * Gany's Surface
 * 
 * @author ganymedes01
 * 
 */

public class InventoryUtils {

	public static void dropStack(World world, int x, int y, int z, ItemStack stack) {
		if (!world.isRemote && stack != null && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			float f = 0.7F;
			double d0 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double d1 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			double d2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
			EntityItem entityItem = new EntityItem(world, x + d0, y + d1, z + d2, stack);
			entityItem.delayBeforeCanPickup = 10;
			world.spawnEntityInWorld(entityItem);
		}
	}

	public static int[] getSlotsFromSide(IInventory iinventory, int side) {
		if (iinventory == null)
			return null;

		if (iinventory instanceof ISidedInventory)
			return ((ISidedInventory) iinventory).getAccessibleSlotsFromSide(side);
		else {
			int[] slots = new int[iinventory.getSizeInventory()];
			for (int i = 0; i < slots.length; i++)
				slots[i] = i;
			return slots;
		}
	}

	/**
	 * Extracts 1 item from the first found stack
	 * 
	 * @param iinventory
	 * @param side
	 * @param maxStackSize
	 * @return extracted stack
	 */
	public static ItemStack extractFromInventory(IInventory iinventory, int side) {
		return extractFromInventory(iinventory, side, 1);
	}

	/**
	 * Extracts a stack with size the same or smaller of @param maxStackSize
	 * 
	 * @param iinventory
	 * @param side
	 * @param maxStackSize
	 * @return extracted stack
	 */
	public static ItemStack extractFromInventory(IInventory iinventory, int side, int maxStackSize) {
		IInventory invt = getInventory(iinventory);
		return extractFromSlots(invt, side, maxStackSize, getSlotsFromSide(invt, side));
	}

	private static ItemStack extractFromSlots(IInventory iinventory, int side, int maxStackSize, int[] slots) {
		for (int slot : slots) {
			ItemStack invtStack = iinventory.getStackInSlot(slot);
			if (invtStack != null)
				if (iinventory instanceof ISidedInventory ? ((ISidedInventory) iinventory).canExtractItem(slot, invtStack, side) : true) {
					ItemStack copy = invtStack.copy();
					if (maxStackSize <= 0)
						iinventory.setInventorySlotContents(slot, null);
					else {
						int amount = Math.min(maxStackSize, invtStack.stackSize);
						invtStack.stackSize -= amount;
						copy.stackSize = amount;
						if (invtStack.stackSize <= 0)
							iinventory.setInventorySlotContents(slot, null);
					}
					return copy;
				}
		}
		return null;
	}

	public static boolean addEntitytoInventory(IInventory iinventory, EntityItem entity) {
		if (entity == null)
			return false;

		boolean flag = addStackToInventory(iinventory, entity.getEntityItem());
		if (flag)
			entity.setDead();
		else if (entity.getEntityItem().stackSize <= 0)
			entity.setDead();
		return flag;
	}

	public static boolean addStackToInventory(IInventory iinventory, ItemStack stack) {
		return addStackToInventory(iinventory, stack, 0);
	}

	public static boolean addStackToInventory(IInventory iinventory, ItemStack stack, int side) {
		if (iinventory == null)
			return false;

		if (stack == null || stack.stackSize <= 0)
			return false;

		IInventory invt = getInventory(iinventory);
		return addToSlots(invt, stack, side, getSlotsFromSide(invt, side));
	}

	private static boolean addToSlots(IInventory iinventory, ItemStack stack, int side, int[] slots) {
		for (int slot : slots) {
			if (iinventory instanceof ISidedInventory) {
				if (!((ISidedInventory) iinventory).canInsertItem(slot, stack, side))
					continue;
			} else if (!iinventory.isItemValidForSlot(slot, stack))
				continue;

			if (iinventory.getStackInSlot(slot) == null) {
				iinventory.setInventorySlotContents(slot, stack.copy());
				stack.stackSize = 0;
				return true;
			} else {
				ItemStack invtStack = iinventory.getStackInSlot(slot);
				if (invtStack.stackSize < Math.min(invtStack.getMaxStackSize(), iinventory.getInventoryStackLimit()) && areStacksTheSame(invtStack, stack, false)) {
					invtStack.stackSize += stack.stackSize;
					if (invtStack.stackSize > invtStack.getMaxStackSize()) {
						stack.stackSize = invtStack.stackSize - invtStack.getMaxStackSize();
						invtStack.stackSize = invtStack.getMaxStackSize();
					} else
						stack.stackSize = 0;
					return true;
				}
			}
		}
		return false;
	}

	public static boolean areStacksTheSame(ItemStack stack1, ItemStack stack2, boolean matchSize) {
		if (stack1 == null || stack2 == null)
			return false;

		if (stack1.getItem() == stack2.getItem())
			if (stack1.getItemDamage() == stack2.getItemDamage())
				if (!matchSize || stack1.stackSize == stack2.stackSize) {
					if (stack1.hasTagCompound() && stack2.hasTagCompound())
						return stack1.getTagCompound().equals(stack2.getTagCompound());
					return stack1.hasTagCompound() == stack2.hasTagCompound();
				}
		return false;
	}

	public static IInventory getInventory(IInventory inventory) {
		if (inventory instanceof TileEntityChest) {
			TileEntityChest chest = (TileEntityChest) inventory;
			TileEntityChest adjacent = null;
			if (chest.adjacentChestXNeg != null)
				adjacent = chest.adjacentChestXNeg;
			if (chest.adjacentChestXNeg != null)
				adjacent = chest.adjacentChestXNeg;
			if (chest.adjacentChestXPos != null)
				adjacent = chest.adjacentChestXPos;
			if (chest.adjacentChestZNeg != null)
				adjacent = chest.adjacentChestZNeg;
			if (chest.adjacentChestZPos != null)
				adjacent = chest.adjacentChestZPos;

			if (adjacent != null)
				return new InventoryLargeChest("", inventory, adjacent);
		}
		return inventory;
	}

	public static void dropInventoryContents(TileEntity tile) {
		if (tile == null || !(tile instanceof IInventory))
			return;
		IInventory iinventory = (IInventory) tile;
		for (int i = 0; i < iinventory.getSizeInventory(); i++) {
			ItemStack stack = iinventory.getStackInSlot(i);
			if (stack != null) {
				dropStack(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, stack.copy());
				iinventory.setInventorySlotContents(i, null);
			}
		}
		tile.markDirty();
	}
}