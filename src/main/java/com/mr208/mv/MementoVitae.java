package com.mr208.mv;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MementoVitae.MOD_ID, name = MementoVitae.MOD_NAME, version = MementoVitae.MOD_VER)
public class MementoVitae {

    public static final String MOD_ID = "mv";
    public static final String MOD_NAME = "Memento Vitae";
    public static final String MOD_VER = "1.0.0";

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(DeathEvents.INSTANCE);
    }

    @net.minecraftforge.common.config.Config(modid = MOD_ID, name = MOD_NAME)
    public static class Config {

        @net.minecraftforge.common.config.Config.Comment({"Percentage of Experience lost on death"})
        @net.minecraftforge.common.config.Config.RangeInt(min = 0, max = 100)
        public static int xpLossOnDeath = 0;
    }

    public static class DeathEvents {

        public static DeathEvents INSTANCE = new DeathEvents();

        @SubscribeEvent
        public void onPlayerClone(PlayerEvent.Clone event) {

            if(event.isWasDeath()) {
                EntityPlayer deadBody = event.getOriginal();
                EntityPlayer cloneBody = event.getEntityPlayer();

                float xp = deadBody.experienceTotal * (((100f - (float)Config.xpLossOnDeath))/100f);

                cloneBody.addExperience((int) xp);
            }
        }

        @SubscribeEvent
        public void onLivingXPDrop(LivingExperienceDropEvent event) {

            EntityLivingBase entity = event.getEntityLiving();

            if(entity instanceof EntityPlayer) {
                event.setCanceled(true);
            }
        }
    }
}
