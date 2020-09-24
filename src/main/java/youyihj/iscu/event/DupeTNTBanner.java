package youyihj.iscu.event;

import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockSlime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import youyihj.iscu.IsolatedCrystalUtils;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = IsolatedCrystalUtils.MODID)
public class DupeTNTBanner {
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        Entity entity = event.getEntity();
        if (world.isRemote) return;
        if (entity instanceof EntityTNTPrimed && check(world, entity.getPosition())) {
            event.setCanceled(true);
            notifyAllPlayer(world);
        } else if (entity instanceof EntityItem) {
            Item item = ((EntityItem) entity).getItem().getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockCarpet) {
                event.setCanceled(check(world, entity.getPosition()));
                notifyAllPlayer(world);
            }
        }
    }

    private static void notifyAllPlayer(World world) {
        world.playerEntities.forEach(player -> player.sendMessage(new TextComponentTranslation("iscu.message.tnt_duper")));
    }

    private static boolean check(World world, BlockPos pos) {
        for (BlockPos blockPos : BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(3, 3, 3))) {
            if (world.getBlockState(blockPos).getBlock() instanceof BlockSlime) {
                return true;
            }
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (Objects.nonNull(tileEntity) && tileEntity instanceof TileEntityPiston) {
                return true;
            }
        }
        return false;
    }
}
