package nova.committee.inventoryhome.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class SecurityProfile {

    private String ownerName = "";

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwner(Player player) {
        ownerName = player.getName().getString();
    }

    public boolean hasOwner() {
        return ownerName.isEmpty();
    }

    public boolean isOwner(String ownerName) {
        return this.ownerName.equalsIgnoreCase(ownerName);
    }

    public void readFromNBT(CompoundTag nbt) {

        if (!nbt.getString("ownerName").isEmpty()) {
            ownerName = nbt.getString("ownerName");
        }
    }

    public void writeToNBT(CompoundTag nbt) {

        if (!ownerName.isEmpty()) {
            nbt.putString("ownerName", ownerName);
        }
    }
}
