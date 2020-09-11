package youyihj.iscu.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import youyihj.iscu.IsolatedCrystalUtils;
import youyihj.iscu.tile.TileHotAirChecker;

import javax.annotation.Nullable;

public class HotAirChecker extends Block {
    public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

    public HotAirChecker() {
        super(Material.IRON);
        this.setUnlocalizedName(IsolatedCrystalUtils.MODID + ".hot_air_checker");
        this.setRegistryName("hot_air_checker");
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.blockHardness = 5.0f;
        this.blockResistance = 50.0f;
    }

    @Nullable
    @Override
    public String getHarvestTool(IBlockState state) {
        return "pickaxe";
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return side != null;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (side == EnumFacing.UP || side == EnumFacing.DOWN) return 0;
        else return blockState.getValue(ACTIVATED) ? 15 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVATED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVATED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVATED, meta == 1);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this.getStrongPower(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileHotAirChecker();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = playerIn.getHeldItem(hand);
        if (itemStack.getItem() != Items.AIR) return false;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileHotAirChecker && !worldIn.isRemote) {
            TileHotAirChecker hotAirChecker = (TileHotAirChecker) tileEntity;
            // playerIn.sendStatusMessage(new TextComponentString(tileEntity.getTileData().toString()), false);
            playerIn.sendStatusMessage(new TextComponentTranslation("iscu.message.tweak_hot_air_checker", hotAirChecker.tweakSetTemp(playerIn)), false);
            hotAirChecker.update();
        }
        return true;
    }
}
