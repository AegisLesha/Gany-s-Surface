package ganymedes01.ganyssurface.recipes;

import ganymedes01.ganyssurface.ModItems;
import ganymedes01.ganyssurface.core.utils.UnsizedStack;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class OrganicMatterRegistry {

	private static HashMap<UnsizedStack, Integer> matterYield = new HashMap<UnsizedStack, Integer>();
	private static HashMap<Integer, Integer> oreYield = new HashMap<Integer, Integer>();
	private static HashMap<Material, Integer> materialYield = new HashMap<Material, Integer>();

	static {
		addMatterYield(new ItemStack(Items.coal), -1);
		addMatterYield(new ItemStack(Items.coal, 1, 1), 16);
		addItemYield(Items.wooden_sword);
		addItemYield(Items.wooden_hoe);
		addItemYield(Items.wooden_axe);
		addItemYield(Items.wooden_shovel);
		addItemYield(Items.leather_helmet);
		addItemYield(Items.leather_chestplate);
		addItemYield(Items.leather_leggings);
		addItemYield(Items.leather_boots);
		addItemYield(Items.sign);
		addItemYield(Items.saddle);
		addItemYield(Items.book);
		addItemYield(Items.fishing_rod);
		addItemYield(Items.item_frame);
		addItemYield(Items.boat);
		addItemYield(Items.bone);
		addItemYield(Items.bed);
		addItemYield(Items.filled_map);
		addItemYield(Items.map);
		addItemYield(Items.writable_book);
		addItemYield(Items.written_book);
		addItemYield(Items.carrot_on_a_stick);
		addItemYield(Items.enchanted_book);
		addItemYield(Items.name_tag);
		addItemYield(Items.sugar);
		addItemYield(Items.cake);
		addItemYield(Items.slime_ball);
		addItemYield(Items.paper);
		addItemYield(Items.sugar);
		addItemYield(Items.leather);
		addItemYield(Items.wooden_door);
		addItemYield(Items.lead);
		addItemYield(Items.wheat);
		addItemYield(Items.dye);
		addItemYield(Items.reeds);
		addItemYield(Items.feather);
		addMatterYield(new ItemStack(Items.dye, 1, 15));

		addItemYield(ModItems.woodenBoots);
		addItemYield(ModItems.woodenLeggings);
		addItemYield(ModItems.woodenChestplate);
		addItemYield(ModItems.woodenHelmet);
		addItemYield(ModItems.woodenWrench);
		addItemYield(ModItems.rot);
		addMatterYield(new ItemStack(ModItems.rot, 1, 1));
		addItemYield(ModItems.teaLeaves);
		addItemYield(ModItems.teaBag);
		addItemYield(ModItems.poop);
		addMatterYield(new ItemStack(ModItems.poop, 1, 1));
		addItemYield(ModItems.batNet);
		addItemYield(ModItems.pocketCritter, 3);
		addItemYield(ModItems.horsalyser);

		addMatterYield(new ItemStack(Blocks.ladder));
		addMatterYield(new ItemStack(Blocks.wooden_button));

		addOreYield("mobEgg", 2);
		addOreYield("itemSkull", 8);
		addOreYield("record", 4);
		addOreYield("plankWood", 4);
		addOreYield("slabWood", 2);
		addOreYield("stairWood", 2);
		addOreYield("treeSapling", 2);
		addOreYield("treeLeaves", 1);
		addOreYield("stickWood", 1);
		addOreYield("egg", 1);

		addMaterialYield(Material.cactus, 4);
		addMaterialYield(Material.leaves, 3);
		addMaterialYield(Material.plants, 3);
		addMaterialYield(Material.vine, 3);
		addMaterialYield(Material.web, 3);
		addMaterialYield(Material.grass, 4);
		addMaterialYield(Material.cloth, 3);
		addMaterialYield(Material.cake, 3);
		addMaterialYield(Material.carpet, 2);
		addMaterialYield(Material.gourd, 4);
		addMaterialYield(Material.wood, 4);
	}

	private static void addItemYield(Item matter, int yield) {
		if (matter != null)
			addMatterYield(new ItemStack(matter), yield);
	}

	private static void addItemYield(Item matter) {
		addItemYield(matter, 2);
	}

	public static void addMatterYield(ItemStack matter, int yield) {
		if (matter != null)
			matterYield.put(new UnsizedStack(matter), yield);
	}

	private static void addMatterYield(ItemStack matter) {
		addMatterYield(matter, 2);
	}

	private static void addOreYield(String ore, int yield) {
		int oreID = OreDictionary.getOreID(ore);
		if (!oreYield.containsKey(oreID))
			oreYield.put(oreID, yield);
	}

	private static void addMaterialYield(Material material, int yield) {
		if (!materialYield.containsKey(material))
			materialYield.put(material, yield);
	}

	public static boolean isOrganicMatter(ItemStack stack) {
		return getOrganicYield(stack) > 0;
	}

	private static boolean mapContainsKeys(HashMap<Integer, Integer> map, ItemStack stack) {
		for (int id : OreDictionary.getOreIDs(stack))
			if (map.containsKey(id))
				return true;
		return false;
	}

	private static int getValue(HashMap<Integer, Integer> map, ItemStack stack) {
		for (int id : OreDictionary.getOreIDs(stack)) {
			Integer value = map.get(id);
			if (value != null)
				return value;
		}
		return -1;
	}

	public static int getOrganicYield(ItemStack stack) {
		if (stack == null)
			return -1;
		if (stack.getItem() == Item.getItemFromBlock(Blocks.coal_block))
			return -1;

		if (matterYield.containsKey(new UnsizedStack(stack)))
			return matterYield.get(new UnsizedStack(stack));
		else if (mapContainsKeys(oreYield, stack))
			return getValue(oreYield, stack);

		int ret = -1;

		if (stack.getItem() instanceof ItemBlock) {
			Block block = Block.getBlockFromItem(stack.getItem());
			Integer matYield = materialYield.get(block.getMaterial());
			matYield = matYield == null ? -1 : matYield;
			ret = block instanceof BlockSlab ? (int) (matYield / 2.0F) : matYield;
		} else {
			Item item = stack.getItem();
			if (item instanceof ItemFood)
				ret = (int) (10 * ((ItemFood) item).func_150906_h(stack));
			else if (item instanceof ItemSeeds)
				ret = 1;
		}

		return ret;
	}
}