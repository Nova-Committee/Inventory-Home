package nova.committee.inventoryhome;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nova.committee.inventoryhome.common.net.PacketHandler;
import nova.committee.inventoryhome.core.SaveFileData;
import nova.committee.inventoryhome.init.handler.PlayerHandler;
import nova.committee.inventoryhome.init.handler.SecurityHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("inventoryhome")
public class InventoryHome {

    public static final String MOD_ID = "inventoryhome";
    public static final Logger LOGGER = LogManager.getLogger();

    public InventoryHome() {
        PacketHandler.registerMessage();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        MinecraftForge.EVENT_BUS.register(new SecurityHandler());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        SaveFileData.init();
    }

}
