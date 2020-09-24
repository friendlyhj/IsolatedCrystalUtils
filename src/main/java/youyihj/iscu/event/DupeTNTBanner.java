package youyihj.iscu.event;

import net.minecraft.block.BlockSlime;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
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
        if (world.isRemote || !(entity instanceof EntityTNTPrimed)) return;
        BlockPos pos = entity.getPosition();
        BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(3, 3, 3)).forEach(blockPos -> {
            if (world.getBlockState(blockPos).getBlock() instanceof BlockSlime) {
                event.setCanceled(true);
                notifyAllPlayer(world);
                return;
            }
            TileEntity tileEntity = world.getTileEntity(blockPos);
            if (Objects.nonNull(tileEntity) && tileEntity instanceof TileEntityPiston) {
                event.setCanceled(true);
                notifyAllPlayer(world);
            }
        });
    }

    private static void notifyAllPlayer(World world) {
        world.playerEntities.forEach(player -> player.sendMessage(new TextComponentTranslation("iscu.message.tnt_duper")));
    }
}
