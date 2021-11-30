package me.johnnydevo.bettercoalmod.items;

import net.minecraft.item.BowItem;

public class CarbonBow extends BowItem {
    public CarbonBow(Properties p) {
        super(p);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 25;
    }
}
