package com.mrbysco.armorstand;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import com.mrbysco.armorstand.builder.BuilderInteractArmorStand;
import com.mrbysco.armorstand.component.ArmorStandComponent;
import com.mrbysco.armorstand.interaction.SpawnArmorStandInteraction;
import com.mrbysco.armorstand.systems.ArmorStandSystems;

import javax.annotation.Nonnull;

//TODO: Implement posing once you can adjust model parts server-side
public class ArmorStandPlugin extends JavaPlugin {
	public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
	public static ComponentType<EntityStore, ArmorStandComponent> armorStandComponent;

	public ArmorStandPlugin(@Nonnull JavaPluginInit init) {
		super(init);
	}

	@Override
	protected void shutdown() {

	}

	@Override
	protected void setup() {
		IO.println("Setting up plugin " + this.getName());
		armorStandComponent = this.getEntityStoreRegistry().registerComponent(ArmorStandComponent.class, "ArmorStand", ArmorStandComponent.CODEC);
		IO.println("Registering Spawn Armor Stand Interaction");
		this.getCodecRegistry(Interaction.CODEC).register("SpawnArmorStand", SpawnArmorStandInteraction.class, SpawnArmorStandInteraction.CODEC);
		IO.println("Registering Armor Stand Systems");
		this.getEntityStoreRegistry().registerSystem(new ArmorStandSystems.DropDeathItems());

		NPCPlugin.get().registerCoreComponentType("InteractArmorStand", BuilderInteractArmorStand::new);
	}
}