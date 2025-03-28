package com.yrahcaz.sparkysessentialsmod.init;

import com.yrahcaz.sparkysessentialsmod.data.block.AndGateBlock;
import com.yrahcaz.sparkysessentialsmod.data.block.ExclusiveOrGateBlock;
import com.yrahcaz.sparkysessentialsmod.data.block.OrGateBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryObject<Block> AND_GATE = register("and_gate", () -> new AndGateBlock(), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> OR_GATE = register("or_gate", () -> new OrGateBlock(), CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<Block> XOR_GATE = register("xor_gate", () -> new ExclusiveOrGateBlock(), CreativeModeTab.TAB_REDSTONE);

    public static void init() {};

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> ret = Registrar.BLOCKS.register(name, block);
        Registrar.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(tab)));
        return ret;
    }
}
