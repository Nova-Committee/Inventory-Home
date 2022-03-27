package nova.committee.inventoryhome.util;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import nova.committee.inventoryhome.core.ISecurity;

public class SecurityUtil {

    public static boolean canUseSecuredBlock(BlockPos pos, Player player, boolean printError) {

        BlockEntity tileEntity = player.level.getBlockEntity(pos);

        if (tileEntity instanceof ISecurity) {

            ISecurity security = (ISecurity) tileEntity;

            if (security.getSecurityProfile().isOwner(player.getName().getString()) || player.isCreative()) {
                return true;
            } else if (printError) printErrorMessage(pos, player);

            return false;
        }

        return true;
    }

    public static void printErrorMessage(BlockPos pos, Player player) {

        if (player.level.isClientSide) {
            player.sendMessage(new TranslatableComponent("msg.security.not_you").withStyle(ChatFormatting.RED), Util.NIL_UUID);
        }
    }
}
