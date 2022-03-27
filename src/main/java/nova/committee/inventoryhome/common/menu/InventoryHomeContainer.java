package nova.committee.inventoryhome.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nova.committee.inventoryhome.common.net.PacketHandler;
import nova.committee.inventoryhome.common.net.packets.SetActivePacket;
import nova.committee.inventoryhome.common.tile.InventoryHomeTile;
import nova.committee.inventoryhome.init.registry.ModContainers;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/25 13:05
 * Version: 1.0
 */
public class InventoryHomeContainer extends AbstractContainerMenu {

    public final InventoryHomeTile tile;
    private final Container container;
    private final int containerRows;
    private final Level level;

    public InventoryHomeContainer(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, Container pContainer, int pRows, FriendlyByteBuf buf) {
        this(pType, pContainerId, pPlayerInventory, pContainer, pRows, getTileEntity(pPlayerInventory, buf));
    }


    public InventoryHomeContainer(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, Container pContainer, int pRows, InventoryHomeTile entity) {
        super(pType, pContainerId);
        checkContainerSize(pContainer, pRows * 9);
        this.container = pContainer;
        this.containerRows = pRows;
        this.tile = entity;
        this.level = pPlayerInventory.player.level;
        pContainer.startOpen(pPlayerInventory.player);
        int i = (this.containerRows - 4) * 18;

        for (int j = 0; j < this.containerRows; ++j) {
            for (int k = 0; k < 13; ++k) {
                this.addSlot(new Slot(pContainer, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(pPlayerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(pPlayerInventory, i1, 8 + i1 * 18, 161 + i));
        }

    }

    private static InventoryHomeTile getTileEntity(Inventory inventory, FriendlyByteBuf buffer) {
        final BlockEntity tileAtPos = inventory.player.level.getBlockEntity(buffer.readBlockPos());
        return (InventoryHomeTile) tileAtPos;
    }

    public static InventoryHomeContainer sixRows(int pId, Inventory pPlayer, FriendlyByteBuf buffer) {
        return new InventoryHomeContainer(ModContainers.GENERIC_13x6, pId, pPlayer, new SimpleContainer(9 * 6), 6, buffer);
    }

    public boolean setActiveButton(boolean active) {
        if (level.isClientSide) {
            PacketHandler.INSTANCE.sendToServer(new SetActivePacket(active));
        } else {
            this.tile.setActive(active);
        }
        return true;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.container.stillValid(pPlayer);
    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < this.containerRows * 13) {
                if (!this.moveItemStackTo(itemstack1, this.containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.container.stopOpen(pPlayer);
    }

    public Container getContainer() {
        return this.container;
    }

    @OnlyIn(Dist.CLIENT)
    public int getRowCount() {
        return this.containerRows;
    }
}
