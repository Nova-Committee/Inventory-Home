package nova.committee.inventoryhome.init.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import nova.committee.inventoryhome.InventoryHome;
import nova.committee.inventoryhome.common.tile.InventoryHomeTile;
import nova.committee.inventoryhome.util.RegistryUtil;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 10:09
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = InventoryHome.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTiles {
    public static BlockEntityType<InventoryHomeTile> GENERIC_13x6;

    @SubscribeEvent
    public static void registerTiles(RegistryEvent.Register<BlockEntityType<?>> event) {
        final IForgeRegistry<BlockEntityType<?>> registry = event.getRegistry();

        registry.registerAll(

                GENERIC_13x6 = RegistryUtil.registerBlockEntity(InventoryHomeTile::new, "generic1_9x6", ModBlocks.GENERIC_13x6)

        );
    }
}
