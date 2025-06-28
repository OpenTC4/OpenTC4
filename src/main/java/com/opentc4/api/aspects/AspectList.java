package com.opentc4.api.aspects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class AspectList implements Serializable {

    private final LinkedHashMap<Aspect, Integer> aspects = new LinkedHashMap<>();

    public AspectList() {}

    public AspectList(AspectList other) {
        add(other);
    }

    public AspectList copy() {
        return new AspectList(this);
    }

    public int size() {
        return aspects.size();
    }

    public int visSize() {
        return aspects.values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
    }

    public Aspect[] getAspects() {
        return aspects.keySet()
            .toArray(new Aspect[0]);
    }

    public Aspect[] getPrimalAspects() {
        return aspects.keySet()
            .stream()
            .filter(Aspect::isPrimal)
            .toArray(Aspect[]::new);
    }

    public Aspect[] getAspectsSorted() {
        List<Aspect> sorted = new ArrayList<>(aspects.keySet());
        sorted.sort(Comparator.comparing(Aspect::getTag));
        return sorted.toArray(new Aspect[0]);
    }

    public Aspect[] getAspectsSortedAmount() {
        List<Aspect> sorted = new ArrayList<>(aspects.keySet());
        sorted.sort(
            Comparator.comparingInt(this::getAmount)
                .reversed());
        return sorted.toArray(new Aspect[0]);
    }

    public int getAmount(Aspect aspect) {
        return aspects.getOrDefault(aspect, 0);
    }

    public boolean reduce(Aspect aspect, int amount) {
        int current = getAmount(aspect);
        if (current >= amount) {
            setAmount(aspect, current - amount);
            return true;
        }
        return false;
    }

    public AspectList remove(Aspect aspect, int amount) {
        int remaining = getAmount(aspect) - amount;
        if (remaining <= 0) {
            aspects.remove(aspect);
        } else {
            aspects.put(aspect, remaining);
        }
        return this;
    }

    public AspectList remove(Aspect aspect) {
        aspects.remove(aspect);
        return this;
    }

    public AspectList add(Aspect aspect, int amount) {
        aspects.put(aspect, getAmount(aspect) + amount);
        return this;
    }

    public AspectList merge(Aspect aspect, int amount) {
        aspects.put(aspect, Math.max(getAmount(aspect), amount));
        return this;
    }

    public AspectList add(AspectList other) {
        for (Aspect aspect : other.getAspects()) {
            add(aspect, other.getAmount(aspect));
        }
        return this;
    }

    public AspectList merge(AspectList other) {
        for (Aspect aspect : other.getAspects()) {
            merge(aspect, other.getAmount(aspect));
        }
        return this;
    }

    private void setAmount(Aspect aspect, int amount) {
        if (amount <= 0) {
            aspects.remove(aspect);
        } else {
            aspects.put(aspect, amount);
        }
    }

    public void readFromNBT(NBTTagCompound tag) {
        readFromNBT(tag, "Aspects");
    }

    public void readFromNBT(NBTTagCompound tag, String label) {
        aspects.clear();
        NBTTagList list = tag.getTagList(label, 10); // 10 = NBTTagCompound

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            if (entry.hasKey("key")) {
                Aspect aspect = Aspect.getAspect(entry.getString("key"));
                int amount = entry.getInteger("amount");
                if (aspect != null && amount > 0) {
                    add(aspect, amount);
                }
            }
        }
    }

    public void writeToNBT(NBTTagCompound tag) {
        writeToNBT(tag, "Aspects");
    }

    public void writeToNBT(NBTTagCompound tag, String label) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<Aspect, Integer> entry : aspects.entrySet()) {
            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setString(
                "key",
                entry.getKey()
                    .getTag());
            entryTag.setInteger("amount", entry.getValue());
            list.appendTag(entryTag);
        }
        tag.setTag(label, list);
    }
}
