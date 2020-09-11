package youyihj.iscu.tile;

import lykrast.prodigytech.common.capability.CapabilityHotAir;
import lykrast.prodigytech.common.capability.IHotAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import youyihj.iscu.block.HotAirChecker;

public class TileHotAirChecker extends TileEntity implements ITickable, IHotAir {
    private int airTemp;
    private int setTemp = 30;
    private int setTempAboutToSet;

    private static final int MAX_TEMP = 1200;
    private static final int MIN_TEMP = 30;
    private static final int TWEAK_VALUE = 5;

    @Override
    public int getOutAirTemperature() {
        return this.airTemp;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.airTemp = compound.getInteger("airTemp");
        this.setTemp = compound.getInteger("setTemp");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("airTemp", this.airTemp);
        compound.setInteger("setTemp", this.setTemp);
        return super.writeToNBT(compound);
    }

    public int tweakSetTemp(EntityPlayer player) {
        int newSetTemp = getTweakedValue(player, setTemp);
        this.setTempAboutToSet = newSetTemp;
        return newSetTemp;
    }

    private int getTweakedValue(EntityPlayer player, int originValue) {
        if (originValue == 0) return MIN_TEMP;
        if (player.isSneaking()) {
            // minus
            return originValue <= MIN_TEMP ? MAX_TEMP : originValue - TWEAK_VALUE;
        } else {
            // plus
            return originValue >= MAX_TEMP ? MIN_TEMP : originValue + TWEAK_VALUE;
        }
    }

    private int getAirTempFromDown() {
        TileEntity tileEntity = world.getTileEntity(this.pos.down());
        if (tileEntity != null) {
            IHotAir capability = tileEntity.getCapability(CapabilityHotAir.HOT_AIR, EnumFacing.UP);
            if (capability != null) {
                return capability.getOutAirTemperature();
            }
        }
        return 30;
    }

    @Override
    public void update() {
        if (world.isRemote) return;
        boolean pre = this.isActivated();
        this.airTemp = this.getAirTempFromDown();
        if (this.setTempAboutToSet != 0) {
            this.setTemp = this.setTempAboutToSet;
            this.setTempAboutToSet = 0;
        }
        boolean post = this.isActivated();
        if (pre != post) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(HotAirChecker.ACTIVATED, post));
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null && tileEntity instanceof TileHotAirChecker) {
                ((TileHotAirChecker) tileEntity).setTemp = this.setTemp;
            }
        }
    }

    public boolean isActivated() {
        return this.airTemp > this.setTemp;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityHotAir.HOT_AIR && (facing == EnumFacing.UP || facing == null) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability != CapabilityHotAir.HOT_AIR || facing != EnumFacing.UP && facing != null ? super.getCapability(capability, facing) : CapabilityHotAir.HOT_AIR.cast(this);
    }
}
