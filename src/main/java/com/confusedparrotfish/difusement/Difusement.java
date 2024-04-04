package com.confusedparrotfish.difusement;

import org.slf4j.Logger;

import com.confusedparrotfish.difusement.block.blocks;
import com.confusedparrotfish.difusement.entity.block.block_entities;
import com.confusedparrotfish.difusement.entity.block.renderer.difusement_altar_ber;
import com.confusedparrotfish.difusement.entity.block.renderer.obsidian_pedestal_ber;
import com.confusedparrotfish.difusement.item.items;
import com.mojang.logging.LogUtils;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
// import org.slf4j.Logger;

@Mod(Difusement.MODID)
public class Difusement {
    public static final String MODID = "difusement";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Difusement() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // modEventBus.addListener(this::clientsetup);
        modEventBus.addListener((EntityRenderersEvent.RegisterRenderers event)->{
            event.registerBlockEntityRenderer(block_entities.DIFUSEMENT_ALTAR_BET.get(), difusement_altar_ber::new);
            event.registerBlockEntityRenderer(block_entities.OBSIDIAN_PEDESTAL_BET.get(), obsidian_pedestal_ber::new);
            LOGGER.info("Registered Block Entity Renderers (Tile Entity Renderers) for difusement");
        });

        blocks.blocks.register(modEventBus);
        items.items.register(modEventBus);
        block_entities.entities.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    
    // private void clientsetup(final FMLClientSetupEvent event) { 
    //     //     ItemBlockRenderTypes.setRenderLayer(blocks.DIFUSEMENT_ALTAR.get(),RenderType.cutout());
    //     BlockEntityRendererRegistry.register(block_entities.DIFUSEMENT_ALTAR_BET.get(), difusement_altar_ber::new);
    // }
}
