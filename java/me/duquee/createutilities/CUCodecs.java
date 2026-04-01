package me.duquee.createutilities;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler.NetworkKey;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CUCodecs {

    public static final Codec<UUID> UUID_CODEC = RecordCodecBuilder.create((instance) ->
            instance.group( Codec.LONG.fieldOf("mostSigBits").forGetter(UUID::getMostSignificantBits),
                            Codec.LONG.fieldOf("leastSigBits").forGetter(UUID::getLeastSignificantBits))
                    .apply(instance, UUID::new));

    public static final Codec<GameProfile> GAME_PROFILE_CODEC = RecordCodecBuilder.create((instance) ->
            instance.group( UUID_CODEC.fieldOf("uuid").forGetter(GameProfile::getId),
                            Codec.STRING.fieldOf("name").forGetter(GameProfile::getName))
                    .apply(instance, GameProfile::new));

    public static final Codec<NetworkKey> NETWORK_KEY_CODEC = RecordCodecBuilder.create((instance) ->
            instance.group( GAME_PROFILE_CODEC.fieldOf("owner").forGetter((key) -> key.owner),
                            ItemStack.CODEC.fieldOf("frequency1").forGetter((key) -> key.frequencies.get(true).getStack()),
                            ItemStack.CODEC.fieldOf("frequency2").forGetter((key) -> key.frequencies.get(false).getStack()))
                    .apply(instance, (owner, f1, f2) -> new NetworkKey(owner, Frequency.of(f1), Frequency.of(f2))));

}
