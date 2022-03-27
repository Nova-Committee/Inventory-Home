package nova.committee.inventoryhome.init.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import nova.committee.inventoryhome.InventoryHome;
import nova.committee.inventoryhome.common.menu.InventoryHomeContainer;
import nova.committee.inventoryhome.util.RegistryUtil;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/27 10:05
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = InventoryHome.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {
    public static MenuType<InventoryHomeContainer> GENERIC_13x6;

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        final IForgeRegistry<MenuType<?>> registry = event.getRegistry();

        registry.registerAll(

                GENERIC_13x6 = RegistryUtil.registerContainer("generic1_13x6", InventoryHomeContainer::sixRows)

        );
    }
}
