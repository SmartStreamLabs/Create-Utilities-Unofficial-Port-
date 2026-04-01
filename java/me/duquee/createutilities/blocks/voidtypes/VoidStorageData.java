package me.duquee.createutilities.blocks.voidtypes;

import me.duquee.createutilities.blocks.voidtypes.motor.VoidMotorNetworkHandler.NetworkKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class VoidStorageData<T> extends SavedData {

    protected final Map<NetworkKey, T> storages = new HashMap<>();

    public T computeStorageIfAbsent(NetworkKey key, Function<NetworkKey, T> function) {
        return storages.computeIfAbsent(key, function);
    }

    public @NotNull CompoundTag save(@NotNull CompoundTag tag,
                                     HolderLookup.Provider registries,
                                     Predicate<T> isEmpty,
                                     BiFunction<HolderLookup.Provider, T, CompoundTag> serializeNBT) {
        ListTag entries = new ListTag();
        storages.forEach((key, inventory) -> {
            if (isEmpty.test(inventory))
                return;

            CompoundTag entry = new CompoundTag();
            entry.put("Key", key.serialize(registries));
            entry.put("Value", serializeNBT.apply(registries, inventory));
            entries.add(entry);
        });
        tag.put("Entries", entries);
        return tag;
    }

    public static <T, S extends VoidStorageData<T>> S load(CompoundTag tag,
                                                           HolderLookup.Provider registries,
                                                           Supplier<S> storageDataSupplier,
                                                           Function<NetworkKey, T> storageSupplier,
                                                           TriConsumer<HolderLookup.Provider, T, CompoundTag> deserializeNBT) {
        S data = storageDataSupplier.get();
        ListTag entries = tag.getList("Entries", Tag.TAG_COMPOUND);
        entries.forEach(rawEntry -> {
            CompoundTag entry = (CompoundTag) rawEntry;
            NetworkKey key = NetworkKey.deserialize(registries, entry.getCompound("Key"));
            T inventory = storageSupplier.apply(key);
            deserializeNBT.accept(registries, inventory, entry.getCompound("Value"));
            data.storages.put(key, inventory);
        });
        return data;
    }

    @FunctionalInterface
    public interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }

}
