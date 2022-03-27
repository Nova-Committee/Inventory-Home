package nova.committee.inventoryhome.init.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import nova.committee.inventoryhome.InventoryHome;
import nova.committee.inventoryhome.common.block.InventoryHomeBlock;
import nova.committee.inventoryhome.util.RegistryUtil;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 10:12
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = InventoryHome.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {
    public static Block GENERIC_13x6;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();

        registry.registerAll(

                GENERIC_13x6 = new InventoryHomeBlock()

        );
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        registry.registerAll(
                RegistryUtil.blockItem(GENERIC_13x6)
        );
    }
}
