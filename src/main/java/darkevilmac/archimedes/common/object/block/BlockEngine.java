package darkevilmac.archimedes.common.object.block;

import darkevilmac.archimedes.ArchimedesShipMod;
import darkevilmac.archimedes.common.tileentity.TileEntityEngine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEngine extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public float enginePower;
    public int engineFuelConsumption;

    public BlockEngine(Material material, float power, int fuelconsumption) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        enginePower = power;
        engineFuelConsumption = fuelconsumption;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityEngine(enginePower, engineFuelConsumption);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            Block block = worldIn.getBlockState(pos.north()).getBlock();
            Block block1 = worldIn.getBlockState(pos.south()).getBlock();
            Block block2 = worldIn.getBlockState(pos.west()).getBlock();
            Block block3 = worldIn.getBlockState(pos.east()).getBlock();
            EnumFacing enumfacing = state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && block.isFullBlock() && !block1.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            } else if (enumfacing == EnumFacing.SOUTH && block1.isFullBlock() && !block.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            } else if (enumfacing == EnumFacing.WEST && block2.isFullBlock() && !block3.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            } else if (enumfacing == EnumFacing.EAST && block3.isFullBlock() && !block2.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity != null) {
                player.openGui(ArchimedesShipMod.instance, 3, world, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }

        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IBlockState getStateForEntityRender(IBlockState state) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }


    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING);
    }
}
