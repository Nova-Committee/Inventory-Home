package nova.committee.inventoryhome.common.tile;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nova.committee.inventoryhome.core.ISecurity;
import nova.committee.inventoryhome.core.SaveFileData;
import nova.committee.inventoryhome.core.SecurityProfile;
import nova.committee.inventoryhome.init.registry.ModTiles;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 7:39
 * Version: 1.0
 */
public class InventoryHomeTile extends BaseContainerBlockEntity implements ISecurity, LidBlockEntity {
    private static final BaseComponent CONTAINER_TITLE = new TranslatableComponent("info.tile");
    private final SecurityProfile profile = new SecurityProfile();
    private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(13 * 6, ItemStack.EMPTY);
    private final ChestLidController chestLidController = new ChestLidController();
    private boolean active = false;

    public InventoryHomeTile(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModTiles.GENERIC_13x6, pWorldPosition, pBlockState);
    }

    public static void lidAnimateTick(Level pLevel, BlockPos pPos, BlockState pState, InventoryHomeTile pBlockEntity) {
        pBlockEntity.chestLidController.tickLid();
    }

    public NonNullList<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        SaveFileData.addElements(getSecurityProfile().getOwnerName(), this.getBlockPos());
        markForUpdate();
    }

    public void markForUpdate() {
        if (level != null) {
            setChanged();
            level.blockEvent(getBlockPos(), getBlockState().getBlock(), 1, 1);
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
        }
    }

    @Override
    public SecurityProfile getSecurityProfile() {
        return profile;
    }

    @Override
    public void load(CompoundTag pTag) {
        getSecurityProfile().readFromNBT(pTag);
        ContainerHelper.loadAllItems(pTag, this.itemStacks);
        this.active = pTag.getBoolean("Active");
        super.load(pTag);

    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        getSecurityProfile().writeToNBT(pTag);
        ContainerHelper.saveAllItems(pTag, this.itemStacks);
        pTag.putBoolean("Active", this.active);
        super.saveAdditional(pTag);
    }

    @Override
    protected Component getDefaultName() {
        return CONTAINER_TITLE;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return ChestMenu.sixRows(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return 13 * 6;
    }

    @Override
    public boolean isEmpty() {
        return this.getItemStacks().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.getItemStacks().get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItemStacks(), pIndex, pCount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.getItemStacks(), pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        this.getItemStacks().set(pIndex, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(pPlayer.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    public int getFreeSlot() {
        for (int i = 0; i < this.itemStacks.size(); ++i) {
            if (this.itemStacks.get(i).isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private int addResource(int p_191973_1_, ItemStack p_191973_2_) {
        Item item = p_191973_2_.getItem();
        int i = p_191973_2_.getCount();
        ItemStack itemstack = this.getItem(p_191973_1_);
        if (itemstack.isEmpty()) {
            itemstack = p_191973_2_.copy();
            itemstack.setCount(0);
            if (p_191973_2_.hasTag()) {
                itemstack.setTag(p_191973_2_.getTag().copy());
            }

            this.setItem(p_191973_1_, itemstack);
        }

        int j = i;
        if (i > itemstack.getMaxStackSize() - itemstack.getCount()) {
            j = itemstack.getMaxStackSize() - itemstack.getCount();
        }

        if (j > this.getMaxStackSize() - itemstack.getCount()) {
            j = this.getMaxStackSize() - itemstack.getCount();
        }

        if (j == 0) {
            return i;
        } else {
            i = i - j;
            itemstack.grow(j);
            itemstack.setPopTime(5);
            return i;
        }
    }

    public boolean add(ItemStack pItemStack) {
        return this.add(-1, pItemStack);
    }

    public boolean add(int pSlot, ItemStack pStack) {
        if (pStack.isEmpty()) {
            return false;
        } else {
            try {
                if (pStack.isDamaged()) {
                    if (pSlot == -1) {
                        pSlot = this.getFreeSlot();
                    }

                    if (pSlot >= 0) {
                        this.itemStacks.set(pSlot, pStack.copy());
                        this.itemStacks.get(pSlot).setPopTime(5);
                        pStack.setCount(0);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    int i;
                    do {
                        i = pStack.getCount();
                        if (pSlot == -1) {
                            pSlot = this.getFreeSlot();
                            pStack.setCount(this.addResource(pSlot, pStack));
                        } else {
                            pStack.setCount(this.addResource(pSlot, pStack));
                        }
                    } while (!pStack.isEmpty() && pStack.getCount() < i);

                    return true;
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Adding item to inventory");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being added");
                crashreportcategory.setDetail("Registry Name", () -> String.valueOf(pStack.getItem().getRegistryName()));
                crashreportcategory.setDetail("Item Class", () -> pStack.getItem().getClass().getName());
                crashreportcategory.setDetail("Item ID", Item.getId(pStack.getItem()));
                crashreportcategory.setDetail("Item data", pStack.getDamageValue());
                crashreportcategory.setDetail("Item name", () -> {
                    return pStack.getHoverName().getString();
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    @Override
    public void clearContent() {
        this.getItemStacks().clear();
    }

    @Override
    public boolean triggerEvent(int pId, int pType) {
        if (pId == 1) {
            this.chestLidController.shouldBeOpen(pType > 0);
            return true;
        } else {
            return super.triggerEvent(pId, pType);
        }
    }

    @Override
    public float getOpenNess(float pPartialTicks) {
        return this.chestLidController.getOpenness(pPartialTicks);
    }
}
