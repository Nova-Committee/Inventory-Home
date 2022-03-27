package nova.committee.inventoryhome.init.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nova.committee.inventoryhome.common.tile.InventoryHomeTile;
import nova.committee.inventoryhome.core.SaveFileData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.minecraft.world.level.GameRules.RULE_KEEPINVENTORY;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 8:50
 * Version: 1.0
 */
@Mod.EventBusSubscriber
public class PlayerHandler {
    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getLevel().getGameRules().getBoolean(RULE_KEEPINVENTORY)) {
                Collection<ItemStack> all = new ArrayList<>();
                all.addAll(player.getInventory().items);
                all.addAll(player.getInventory().armor);
                all.addAll(player.getInventory().offhand);

                List<SaveFileData.SaveEntity> list = new ArrayList<>(SaveFileData.homeMap.values());
                BlockPos pos = SaveFileData.getPoseFromList(list, player.getUUID());
                InventoryHomeTile blockEntity = (InventoryHomeTile) player.getLevel().getBlockEntity(pos);
                if (blockEntity != null) {
                    for (ItemStack itemStack : all) {
                        blockEntity.add(itemStack);
                    }
                    blockEntity.markForUpdate();
                }

            }

        }

    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (!player.getLevel().getGameRules().getBoolean(RULE_KEEPINVENTORY)) {
                event.setCanceled(true);
            }
        }
    }


}
