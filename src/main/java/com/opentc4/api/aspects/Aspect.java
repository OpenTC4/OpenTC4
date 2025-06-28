package com.opentc4.api.aspects;

import java.util.*;
import java.util.stream.Collectors;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.text.WordUtils;

public final class Aspect {

    private final String tag;
    private final List<Aspect> components;
    private final int color;
    private final int blend;
    private final ResourceLocation image;
    private final String chatColor;

    private static final Map<String, Aspect> ASPECTS = new LinkedHashMap<>();

    // === Aspect Registration ===
    // == Basic Aspects ===
    public static final Aspect AIR = register(new Aspect("aer", 0xFFFFBE, "e", 1));
    public static final Aspect EARTH = register(new Aspect("terra", 0x56AC20, "2", 1));
    public static final Aspect FIRE = register(new Aspect("ignis", 0xFF5A01, "c", 1));
    public static final Aspect WATER = register(new Aspect("aqua", 0x3CC2FF, "3", 1));
    public static final Aspect ORDER = register(new Aspect("ordo", 0xD5D4F8, "7", 1));
    public static final Aspect ENTROPY = register(new Aspect("perditio", 0x404040, "8", 771));
    // === Combination Aspects ===
    public static final Aspect VOID = register(new Aspect("vacuos", 0x888888, Arrays.asList(AIR, ENTROPY), 771));
    public static final Aspect LIGHT = register(new Aspect("lux", 0xFFFAC3, Arrays.asList(AIR, FIRE)));
    public static final Aspect WEATHER = register(new Aspect("tempestas", 0xFFFFFF, Arrays.asList(AIR, WATER)));
    public static final Aspect MOTION = register(new Aspect("motus", 0xCE9F94, Arrays.asList(AIR, ORDER)));
    public static final Aspect COLD = register(new Aspect("gelum", 0xE1F0FF, Arrays.asList(FIRE, ENTROPY)));
    public static final Aspect CRYSTAL = register(new Aspect("vitreus", 0x80FFFF, Arrays.asList(EARTH, ORDER)));
    public static final Aspect LIFE = register(new Aspect("victus", 0xD9FFB5, Arrays.asList(WATER, EARTH)));
    public static final Aspect POISON = register(new Aspect("venenum", 0x89AC20, Arrays.asList(WATER, ENTROPY)));
    public static final Aspect ENERGY = register(new Aspect("potentia", 0xC0C0FF, Arrays.asList(ORDER, FIRE)));
    public static final Aspect EXCHANGE = register(new Aspect("permutatio", 0x578357, Arrays.asList(ENTROPY, ORDER)));
    public static final Aspect METAL = register(new Aspect("metallum", 0xB5B5B5, Arrays.asList(EARTH, CRYSTAL)));
    public static final Aspect DEATH = register(new Aspect("mortuus", 0x888800, Arrays.asList(LIFE, ENTROPY)));
    public static final Aspect FLIGHT = register(new Aspect("volatus", 0xE7C5FF, Arrays.asList(AIR, MOTION)));
    public static final Aspect DARKNESS = register(new Aspect("tenebrae", 0x222222, Arrays.asList(VOID, LIGHT)));
    public static final Aspect SOUL = register(new Aspect("spiritus", 0xECBCB4, Arrays.asList(LIFE, DEATH)));
    public static final Aspect HEAL = register(new Aspect("sano", 0xFF6161, Arrays.asList(LIFE, ORDER)));
    public static final Aspect TRAVEL = register(new Aspect("iter", 0xE0039B, Arrays.asList(MOTION, EARTH)));
    public static final Aspect ELDRITCH = register(new Aspect("alienis", 0x804080, Arrays.asList(VOID, DARKNESS)));
    public static final Aspect MAGIC = register(new Aspect("praecantatio", 0x96A0FF, Arrays.asList(VOID, ENERGY)));
    public static final Aspect AURA = register(new Aspect("auram", 0xFFA0FF, Arrays.asList(MAGIC, AIR)));
    public static final Aspect TAINT = register(new Aspect("vitium", 0x800080, Arrays.asList(MAGIC, ENTROPY)));
    public static final Aspect SLIME = register(new Aspect("limus", 0x01F001, Arrays.asList(LIFE, WATER)));
    public static final Aspect PLANT = register(new Aspect("herba", 0x01AD01, Arrays.asList(LIFE, EARTH)));
    public static final Aspect TREE = register(new Aspect("arbor", 0x878040, Arrays.asList(AIR, PLANT)));
    public static final Aspect BEAST = register(new Aspect("bestia", 0x9F7B79, Arrays.asList(MOTION, LIFE)));
    public static final Aspect FLESH = register(new Aspect("corpus", 0xEEC8BD, Arrays.asList(DEATH, BEAST)));
    public static final Aspect UNDEAD = register(new Aspect("exanimis", 0x3A3A3A, Arrays.asList(MOTION, DEATH)));
    public static final Aspect MIND = register(new Aspect("cognitio", 0xFF9CFF, Arrays.asList(FIRE, SOUL)));
    public static final Aspect SENSES = register(new Aspect("sensus", 0x0F0F3F, Arrays.asList(AIR, SOUL)));
    public static final Aspect MAN = register(new Aspect("humanus", 0xFFA000, Arrays.asList(BEAST, MIND)));
    public static final Aspect CROP = register(new Aspect("messis", 0xE1A0A0, Arrays.asList(PLANT, MAN)));
    public static final Aspect MINE = register(new Aspect("perfodio", 0xDC7C78, Arrays.asList(MAN, EARTH)));
    public static final Aspect TOOL = register(new Aspect("instrumentum", 0x40405A, Arrays.asList(MAN, ORDER)));
    public static final Aspect HARVEST = register(new Aspect("meto", 0xEED3E2, Arrays.asList(CROP, TOOL)));
    public static final Aspect WEAPON = register(new Aspect("telum", 0xC06B10, Arrays.asList(TOOL, FIRE)));
    public static final Aspect ARMOR = register(new Aspect("tutamen", 0x00C000, Arrays.asList(TOOL, EARTH)));
    public static final Aspect HUNGER = register(new Aspect("fames", 0x99C2C5, Arrays.asList(LIFE, VOID)));
    public static final Aspect GREED = register(new Aspect("lucrum", 0xE6AC94, Arrays.asList(MAN, HUNGER)));
    public static final Aspect CRAFT = register(new Aspect("fabrico", 0x8080C0, Arrays.asList(MAN, TOOL)));
    public static final Aspect CLOTH = register(new Aspect("pannus", 0xEB49F2, Arrays.asList(TOOL, BEAST)));
    public static final Aspect MECHANISM = register(new Aspect("machina", 0x808080, Arrays.asList(MOTION, TOOL)));
    public static final Aspect TRAP = register(new Aspect("vinculum", 0x9A9A9A, Arrays.asList(MOTION, ENTROPY)));

    // === Constructors ===
    private Aspect(String tag, int color, String chatColor, int blend) {
        this.tag = tag;
        this.color = color;
        this.chatColor = chatColor;
        this.blend = blend;
        this.components = null;
        this.image = new ResourceLocation("opentc4", "textures/aspects/" + tag.toLowerCase() + ".png");
    }

    private Aspect(String tag, int color, List<Aspect> components) {
        this(tag, color, components, 1);
    }

    private Aspect(String tag, int color, List<Aspect> components, int blend) {
        this.tag = tag;
        this.color = color;
        this.components = components;
        this.blend = blend;
        this.image = new ResourceLocation("opentc4", "textures/aspects/" + tag.toLowerCase() + ".png");
        this.chatColor = null;
    }

    private static Aspect register(Aspect aspect) {
        if (ASPECTS.containsKey(aspect.tag)) throw new IllegalArgumentException(aspect.tag + " already registered!");
        ASPECTS.put(aspect.tag, aspect);
        return aspect;
    }

    // === Getters ===
    public String getTag() {
        return tag;
    }

    public Optional<List<Aspect>> getComponents() {
        return Optional.ofNullable(components);
    }

    public int getColor() {
        return color;
    }

    public int getBlend() {
        return blend;
    }

    public String getName() {
        return WordUtils.capitalizeFully(tag);
    }

    public String getLocalizedDescription() {
        return StatCollector.translateToLocal("tc.aspect." + tag);
    }

    public Optional<String> getChatColor() {
        return Optional.ofNullable(chatColor);
    }

    public ResourceLocation getImage() {
        return image;
    }

    public boolean isPrimal() {
        return components == null || components.size() != 2;
    }

    public static Aspect getAspect(String tag) {
        return ASPECTS.get(tag);
    }

    public static List<Aspect> getPrimalAspects() {
        return ASPECTS.values()
            .stream()
            .filter(Aspect::isPrimal)
            .collect(Collectors.toList());
    }

    public static List<Aspect> getCompoundAspects() {
        return ASPECTS.values()
            .stream()
            .filter(a -> !a.isPrimal())
            .collect(Collectors.toList());
    }
}
