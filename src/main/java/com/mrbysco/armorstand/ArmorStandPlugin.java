package com.mrbysco.armorstand;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.mrbysco.armorstand.builder.BuilderInteractArmorStand;
import com.mrbysco.armorstand.interaction.SpawnArmorStandInteraction;
import com.mrbysco.armorstand.systems.ArmorStandSystems;

import javax.annotation.Nonnull;

//TODO: Implement posing once you can adjust model parts server-side
public class ArmorStandPlugin extends JavaPlugin {
	public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

	public ArmorStandPlugin(@Nonnull JavaPluginInit init) {
		super(init);
	}

	@Override
	protected void shutdown() {

	}

	@Override
	protected void setup() {
		LOGGER.atInfo().log("Setting up plugin " + this.getName());

		LOGGER.atInfo().log("Registering Spawn Armor Stand Interaction");
		this.getCodecRegistry(Interaction.CODEC).register("SpawnArmorStand", SpawnArmorStandInteraction.class, SpawnArmorStandInteraction.CODEC);
		LOGGER.atInfo().log("Registering Armor Stand Systems");
		this.getEntityStoreRegistry().registerSystem(new ArmorStandSystems.DropDeathItems());

		NPCPlugin.get().registerCoreComponentType("InteractArmorStand", BuilderInteractArmorStand::new);
	}
}