package nova.committee.inventoryhome.common.net.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import nova.committee.inventoryhome.common.menu.InventoryHomeContainer;
import nova.committee.inventoryhome.common.tile.InventoryHomeTile;

import java.util.function.Supplier;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 13:51
 * Version: 1.0
 */
public class SetActivePacket extends IPacket {
    private boolean active;

    public SetActivePacket(boolean active) {
        this.active = active;
    }

    public SetActivePacket(FriendlyByteBuf buf) {
        this.active = buf.readBoolean();
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeBoolean(active);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer entity = ctx.get().getSender();
            if (entity != null) {
                if (entity.containerMenu instanceof InventoryHomeContainer) {
                    InventoryHomeContainer container = (InventoryHomeContainer) entity.containerMenu;
                    ((InventoryHomeTile) container.tile).setActive(this.active);
                }
            }
        });
        ctx.get().setPacketHandled(true);

    }
}
