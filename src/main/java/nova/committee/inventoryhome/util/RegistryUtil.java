package nova.committee.inventoryhome.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;
import nova.committee.inventoryhome.init.registry.ModTabs;

import java.util.Objects;
import java.util.function.Function;

public class RegistryUtil {

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(BlockEntityType.BlockEntitySupplier<T> factory, String registryName, Block... block) {
        return (BlockEntityType<T>) BlockEntityType.Builder.of(factory, block).build(null).setRegistryName(registryName);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(BlockEntityType.BlockEntitySupplier<T> factory, ResourceLocation registryName, Block... block) {
        return (BlockEntityType<T>) BlockEntityType.Builder.of(factory, block).build(null).setRegistryName(registryName);
    }

    public static Item blockItem(Block block) {
        return blockItem(block, new Item.Properties().tab(ModTabs.tab));
    }

    private static Item blockItem(Block block, Item.Properties properties) {
        return new BlockItem(block, properties).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
    }

    private static <T extends Enum<T> & StringRepresentable> Block[] registerEnumBlock(IForgeRegistry<Block> registry, T[] types, Function<String, String> nameFactory, Function<T, Block> factory) {
        Block[] blocks = new Block[types.length];
        for (T type : types) {
            blocks[type.ordinal()] = factory.apply(type).setRegistryName(nameFactory.apply(type.getSerializedName()));
        }
        registry.registerAll(blocks);
        return blocks;
    }

    private static void registerEnumBlockItems(IForgeRegistry<Item> registry, Block[] blocks) {
        for (Block block : blocks) {
            registry.register(blockItem(block));
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends AbstractContainerMenu> MenuType<T> registerContainer(String name, IContainerFactory<T> containerFactory) {
        return (MenuType<T>) new MenuType<>(containerFactory).setRegistryName(name);
    }


}
