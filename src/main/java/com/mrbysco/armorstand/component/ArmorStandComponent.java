package com.mrbysco.armorstand.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.mrbysco.armorstand.ArmorStandPlugin;

import java.time.Instant;

public class ArmorStandComponent implements Component<EntityStore> {
	public static final BuilderCodec<ArmorStandComponent> CODEC = BuilderCodec.builder(ArmorStandComponent.class, ArmorStandComponent::new)
			.append(new KeyedCodec<>("SourceItem", Codec.STRING), (o, v) -> o.sourceItem = v, o -> o.sourceItem)
			.add()
			.build();
	private int numberOfHits = 0;
	private Instant lastHit;
	private String sourceItem = "Armor_Stand";

	public static ComponentType<EntityStore, ArmorStandComponent> getComponentType() {
		return ArmorStandPlugin.armorStandComponent;
	}

	private ArmorStandComponent() {
	}

	public ArmorStandComponent(String sourceItem) {
		this.sourceItem = sourceItem;
	}

	public int getNumberOfHits() {
		return this.numberOfHits;
	}

	public void setNumberOfHits(int numberOfHits) {
		this.numberOfHits = numberOfHits;
	}

	public Instant getLastHit() {
		return this.lastHit;
	}

	public void setLastHit(Instant lastHit) {
		this.lastHit = lastHit;
	}

	public String getSourceItem() {
		return this.sourceItem;
	}

	public void setSourceItem(String sourceItem) {
		this.sourceItem = sourceItem;
	}

	@Override
	public Component<EntityStore> clone() {
		return new ArmorStandComponent(this.sourceItem);
	}
}
