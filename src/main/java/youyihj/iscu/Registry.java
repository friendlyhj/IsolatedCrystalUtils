package youyihj.iscu;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import youyihj.iscu.block.HotAirChecker;
import youyihj.iscu.block.WarpObsidian;
import youyihj.iscu.tile.TileHotAirChecker;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class Registry {
    public static final WarpObsidian WARP_OBSIDIAN = new WarpObsidian();
    public static final ItemBlock WARP_OBSIDIAN_ITEM = new ItemBlock(WARP_OBSIDIAN) {
        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
            tooltip.add(I18n.format("iscu.tooltip.warp_obsidian"));
        }
    };
    public static final HotAirChecker HOT_AIR_CHECKER = new HotAirChecker();
    public static final ItemBlock HOT_AIR_CHECKER_ITEM = new ItemBlock(HOT_AIR_CHECKER);

    @SubscribeEvent
    public static void onBlockRegistry(Register<Block> event) {
        event.getRegistry().register(WARP_OBSIDIAN);
        event.getRegistry().register(HOT_AIR_CHECKER);
        GameRegistry.registerTileEntity(TileHotAirChecker.class, new ResourceLocation(IsolatedCrystalUtils.MODID, "hot_air_checker"));
    }

    @SubscribeEvent
    public static void onItemRegistry(Register<Item> event) {
        WARP_OBSIDIAN_ITEM.setRegistryName(WARP_OBSIDIAN_ITEM.getBlock().getRegistryName());
        HOT_AIR_CHECKER_ITEM.setRegistryName(HOT_AIR_CHECKER_ITEM.getBlock().getRegistryName());
        event.getRegistry().register(WARP_OBSIDIAN_ITEM);
        event.getRegistry().register(HOT_AIR_CHECKER_ITEM);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(WARP_OBSIDIAN_ITEM, 0, new ModelResourceLocation(
                WARP_OBSIDIAN_ITEM.getRegistryName(), "inventory"
        ));
        ModelLoader.setCustomModelResourceLocation(HOT_AIR_CHECKER_ITEM, 0, new ModelResourceLocation(
                HOT_AIR_CHECKER_ITEM.getRegistryName(), "inventory"
        ));
    }
}
