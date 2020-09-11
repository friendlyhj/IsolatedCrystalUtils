package youyihj.iscu.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import youyihj.iscu.IsolatedCrystalUtils;

import javax.annotation.Nullable;

public class WarpObsidian extends Block {
    public WarpObsidian() {
        super(Material.ROCK);
        this.setUnlocalizedName(IsolatedCrystalUtils.MODID + ".warp_obsidian");
        this.setRegistryName("warp_obsidian");
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.blockHardness = 50.0f;
        this.blockResistance = 8000.0f;
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return 3;
    }

    @Nullable
    @Override
    public String getHarvestTool(IBlockState state) {
        return "pickaxe";
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItemStack = playerIn.getHeldItem(hand);
        if (playerIn.dimension == 0 && heldItemStack.getItem() == Items.ENDER_EYE) {
            if (!worldIn.isRemote) {
                heldItemStack.shrink(1);
                playerIn.changeDimension(1);
            }
            return true;
        }
        return false;
    }
}
