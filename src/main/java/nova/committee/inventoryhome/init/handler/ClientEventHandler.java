package nova.committee.inventoryhome.init.handler;


import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import nova.committee.inventoryhome.InventoryHome;
import nova.committee.inventoryhome.client.render.InventoryHomeRender;
import nova.committee.inventoryhome.client.screen.InventoryHomeScreen;
import nova.committee.inventoryhome.init.registry.ModContainers;
import nova.committee.inventoryhome.init.registry.ModTiles;

@Mod.EventBusSubscriber(modid = InventoryHome.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {

        MenuScreens.register(ModContainers.GENERIC_13x6, InventoryHomeScreen::new);
        BlockEntityRenderers.register(ModTiles.GENERIC_13x6, InventoryHomeRender::new);

    }


}
