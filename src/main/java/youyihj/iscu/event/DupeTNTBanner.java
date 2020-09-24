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
        DupeType dupeType = getDumpType(entity);
        if (dupeType.isValid() && check(world, entity.getPosition())) {
            event.setCanceled(true);
            notifyAllPlayer(world, dupeType);
        }
    }

    private static void notifyAllPlayer(World world, DupeType dupeType) {
        world.playerEntities.forEach(player -> {
            String key = null;
            switch (dupeType) {
                case TNT:
                    key = "iscu.message.tnt_duper";
                    break;
                case CARPET:
                    key = "iscu.message.carpet_duper";
                    break;
            }
            if (Objects.nonNull(key)) player.sendMessage(new TextComponentTranslation(key));
        });
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

    private static DupeType getDumpType(Entity entity) {
        if (entity instanceof EntityTNTPrimed) return DupeType.TNT;
        if (entity instanceof EntityItem) {
            Item item = ((EntityItem) entity).getItem().getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockCarpet) {
                return DupeType.CARPET;
            }
        }
        return DupeType.INVALID;
    }

    private enum DupeType {
        INVALID,
        TNT,
        CARPET;

        public boolean isValid() {
            return this != INVALID;
        }
    }
}
