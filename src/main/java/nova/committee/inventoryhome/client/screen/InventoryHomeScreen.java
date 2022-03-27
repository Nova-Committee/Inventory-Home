package nova.committee.inventoryhome.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import nova.committee.inventoryhome.InventoryHome;
import nova.committee.inventoryhome.common.menu.InventoryHomeContainer;
import nova.committee.inventoryhome.common.tile.InventoryHomeTile;
import nova.committee.inventoryhome.core.SaveFileData;
import nova.committee.inventoryhome.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/25 13:16
 * Version: 1.0
 */
public class InventoryHomeScreen extends AbstractContainerScreen<InventoryHomeContainer> implements MenuAccess<InventoryHomeContainer> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(InventoryHome.MOD_ID, "textures/gui/generic_54.png");
    private final int containerRows;
    private final InventoryHomeTile tile;
    private Button active;

    public InventoryHomeScreen(InventoryHomeContainer pChestMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pChestMenu, pPlayerInventory, pTitle);
        this.passEvents = false;
        this.containerRows = pChestMenu.getRowCount();
        this.imageHeight = 114 + this.containerRows * 18;
        this.inventoryLabelY = this.imageHeight - 94;
        this.tile = this.getMenu().tile;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(active = new ExtendedButton(100, 5, 20, 18,
                new TranslatableComponent("info.active"), (b) -> this.getMenu().setActiveButton(!tile.isActive())));
    }


    @Override
    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        List<SaveFileData.SaveEntity> list = new ArrayList<>(SaveFileData.homeMap.values());
        BlockPos pos = SaveFileData.getPoseFromList(list, UserUtil.getUUIDByName(tile.getSecurityProfile().getOwnerName()));
        active.active = tile.getBlockPos() != pos;
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(pMatrixStack, i, j, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        this.blit(pMatrixStack, i, j + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }
}
