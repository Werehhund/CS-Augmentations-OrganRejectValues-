package net.corespring.csaugmentations.Network.Packets;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class C2SToggleNVGPacket {
    private final boolean nvgEnabled;

    public C2SToggleNVGPacket(boolean nvgEnabled) {
        this.nvgEnabled = nvgEnabled;
    }

    public C2SToggleNVGPacket(FriendlyByteBuf buf) {
        this.nvgEnabled = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.nvgEnabled);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                handleToggleNVGPacket(this.nvgEnabled, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private void handleToggleNVGPacket(boolean nvgEnabled, ServerPlayer player) {
        player.getCapability(OrganCap.ORGAN_DATA).ifPresent(data -> {
            boolean hasEyes = data.hasCyberEyes(player);

            if (hasEyes) {
                boolean wasActive = data.isNVGToggleActive();
                data.setNVGToggleActive(nvgEnabled);

                if (wasActive && !nvgEnabled && data.hasNVGEffectApplied()) {
                    player.removeEffect(MobEffects.NIGHT_VISION);
                    data.setNVGEffectApplied(false);
                }
            } else {
                data.setNVGToggleActive(false);
            }
            data.updateOrganData();
        });
    }
}