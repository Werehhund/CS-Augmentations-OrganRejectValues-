package net.corespring.csaugmentations.Block.Crops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public abstract class TwoTallCropBlock extends CropBlock {
    public static final int MAX_AGE = 15;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);
    private static final int TEXTURE_COUNT = 8;

    public TwoTallCropBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return;

        int currentAge = getAge(state);
        if (currentAge >= MAX_AGE) return;

        float growthSpeed = getGrowthSpeed(this, level, pos);
        if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / growthSpeed) + 1) == 0)) {
            if (currentAge < TEXTURE_COUNT - 1) {
                level.setBlock(pos, getStateForAge(currentAge + 1), 2);
            } else if (currentAge == TEXTURE_COUNT - 1) {
                tryPlaceTopBlock(level, pos, 1);
            } else {
                level.setBlock(pos, getStateForAge(currentAge + 1), 2);
            }
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        int age = getAge(state);
        if (age >= TEXTURE_COUNT) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            return belowState.is(this) && getAge(belowState) >= TEXTURE_COUNT - 1;
        }
        return super.canSurvive(state, level, pos);
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int currentAge = getAge(state);
        int bonemealBoost = getBonemealAgeIncrease(level);
        int targetAge = Math.min(currentAge + bonemealBoost, MAX_AGE);

        if (currentAge < TEXTURE_COUNT - 1) {
            int newBottomAge = Math.min(targetAge, TEXTURE_COUNT - 1);
            level.setBlock(pos, getStateForAge(newBottomAge), 2);

            if (newBottomAge == TEXTURE_COUNT - 1) {
                int remainingGrowth = targetAge - newBottomAge;
                if (remainingGrowth > 0) {
                    tryPlaceTopBlock(level, pos, remainingGrowth);
                }
            }
        } else if (currentAge == TEXTURE_COUNT - 1) {
            tryPlaceTopBlock(level, pos, bonemealBoost);
        } else {
            level.setBlock(pos, getStateForAge(targetAge), 2);
        }
    }

    private void tryPlaceTopBlock(Level level, BlockPos basePos, int growthAmount) {
        BlockPos topPos = basePos.above();
        BlockState topState = level.getBlockState(topPos);
        Block currentBlock = topState.getBlock();

        if (currentBlock instanceof TwoTallCropBlock) {
            int currentTopAge = getAge(topState);
            int newTopAge = Math.min(currentTopAge + growthAmount, MAX_AGE);
            if (newTopAge > currentTopAge) {
                level.setBlock(topPos, getStateForAge(newTopAge), 2);
            }
        } else if (topState.isAir() || topState.canBeReplaced()) {
            int initialTopAge = TEXTURE_COUNT;
            int targetTopAge = Math.min(initialTopAge + growthAmount, MAX_AGE);
            level.setBlock(topPos, getStateForAge(targetTopAge), 2);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean moved) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, moved);
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return level.random.nextInt(3) + 2;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
        };

        return SHAPE_BY_AGE[this.getAge(pState)];
    }
}