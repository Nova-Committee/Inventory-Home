package nova.committee.inventoryhome.init.registry;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/24 9:49
 * Version: 1.0
 */
public class ModTabs {

    public static CreativeModeTab tab = new CreativeModeTab("tabInventoryHome") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.GENERIC_13x6.asItem());
        }

    };
}
