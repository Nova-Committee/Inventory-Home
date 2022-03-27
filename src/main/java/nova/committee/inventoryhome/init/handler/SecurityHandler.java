package nova.committee.inventoryhome.init.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nova.committee.inventoryhome.common.block.InventoryHomeBlock;
import nova.committee.inventoryhome.common.tile.InventoryHomeTile;
import nova.committee.inventoryhome.core.ISecurity;
import nova.committee.inventoryhome.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 9:04
 * Version: 1.0
 */
public class SecurityHandler {

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        BlockEntity tileEntity = event.getWorld().getBlockEntity(event.getBlockSnapshot().getPos());
        if (event.getEntity() instanceof Player player && tileEntity instanceof InventoryHomeTile inventoryHomeTile && event.getPlacedBlock().getBlock() instanceof InventoryHomeBlock) {
            ISecurity security = (ISecurity) tileEntity;

            security.getSecurityProfile().setOwner(player);
            inventoryHomeTile.markForUpdate();
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!SecurityUtil.canUseSecuredBlock(event.getPos(), event.getPlayer(), true)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onBlockExploded(ExplosionEvent event) {
        List<BlockPos> affectedBlocks = event.getExplosion().getToBlow();
        List<BlockPos> securedBlocksFound = new ArrayList<>();

        for (BlockPos pos : affectedBlocks) {

            BlockEntity tileEntity = event.getWorld().getBlockEntity(pos);

            if (tileEntity instanceof InventoryHomeTile) {
                securedBlocksFound.add(pos);
            }
        }
        affectedBlocks.removeAll(securedBlocksFound);
    }
}
