package nova.committee.inventoryhome.common.block;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import nova.committee.inventoryhome.common.tile.InventoryHomeTile;
import nova.committee.inventoryhome.util.SecurityUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 7:39
 * Version: 1.0
 */
public class InventoryHomeBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public InventoryHomeBlock() {
        super(Properties.of(Material.WOOD)
                .strength(2.5F)
                .sound(SoundType.WOOD));
        setRegistryName("block_home");
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    public static DoubleBlockCombiner.Combiner<InventoryHomeTile, Float2FloatFunction> opennessCombiner(final LidBlockEntity pLid) {
        return new DoubleBlockCombiner.Combiner<InventoryHomeTile, Float2FloatFunction>() {

            @Override
            public Float2FloatFunction acceptDouble(InventoryHomeTile pFirst, InventoryHomeTile pSecond) {
                return (p_51638_) -> {
                    return Math.max(pFirst.getOpenNess(p_51638_), pSecond.getOpenNess(p_51638_));
                };
            }

            @Override
            public Float2FloatFunction acceptSingle(InventoryHomeTile pSingle) {
                return pSingle::getOpenNess;
            }

            public Float2FloatFunction acceptNone() {
                return pLid::getOpenNess;
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new InventoryHomeTile(pPos, pState);
    }


    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AABB;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection().getOpposite();
        boolean flag = pContext.isSecondaryUseActive();
        Direction direction1 = pContext.getClickedFace();
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(direction.getClockWise()));
        if (direction1.getAxis().isHorizontal() && flag) {
            Direction direction2 = blockstate.getValue(FACING);
            if (direction2.getAxis() != direction1.getAxis()) {
                direction = direction2;
            }
        }

        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof InventoryHomeTile) {
                ((InventoryHomeTile) blockentity).setCustomName(pStack.getHoverName());
            }
        }

    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof Container) {
                Containers.dropContents(pLevel, pPos, (Container) blockentity);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InventoryHomeTile tileEntity = (InventoryHomeTile) pLevel.getBlockEntity(pPos);
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (SecurityUtil.canUseSecuredBlock(pPos, pPlayer, true)) {

                NetworkHooks.openGui((ServerPlayer) pPlayer, tileEntity, buf -> {
                    buf.writeBlockPos(pPos);
                });
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}
