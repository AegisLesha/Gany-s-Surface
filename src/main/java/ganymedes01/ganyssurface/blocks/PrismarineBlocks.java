package ganymedes01.ganyssurface.blocks;

import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.ModBlocks.ISubBlocksBlock;
import ganymedes01.ganyssurface.core.utils.Utils;
import ganymedes01.ganyssurface.items.block.ItemPrismarineBlocks;
import ganymedes01.ganyssurface.lib.Strings;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class PrismarineBlocks extends Block implements ISubBlocksBlock {

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public PrismarineBlocks() {
		super(Material.rock);
		setHardness(1.5F);
		setResistance(10.0F);
		if (GanysSurface.enablePrismarineStuff)
			setCreativeTab(GanysSurface.surfaceTab);
		else
			setCreativeTab(null);
		setBlockName(Utils.getUnlocalizedName(Strings.PRISMARINE_BLOCKS));
		setBlockTextureName(Utils.getBlockTexture(Strings.PRISMARINE_BLOCKS));
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 3; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[Math.max(Math.min(meta, icons.length - 1), 0)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		icons = new IIcon[3];
		for (int i = 0; i < 3; i++)
			icons[i] = reg.registerIcon(Utils.getBlockTexture(Strings.PRISMARINE_BLOCKS + i));
	}

	@Override
	public Class<? extends ItemBlock> getItemBlockClass() {
		return ItemPrismarineBlocks.class;
	}
}