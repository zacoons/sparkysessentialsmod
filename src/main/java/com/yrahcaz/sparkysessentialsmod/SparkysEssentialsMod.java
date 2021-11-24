package com.yrahcaz.sparkysessentialsmod;

import com.yrahcaz.sparkysessentialsmod.init.Registrar;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("sparkysessentialsmod")
public class SparkysEssentialsMod
{
    public static final String MOD_ID = "sparkysessentialsmod";

    public SparkysEssentialsMod() {
        Registrar.registerAll();

        MinecraftForge.EVENT_BUS.register(this);
    }
}
