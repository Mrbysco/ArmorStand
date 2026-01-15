package com.mrbysco.armorstand.builder;

import com.google.gson.JsonElement;
import com.hypixel.hytale.server.npc.asset.builder.BuilderDescriptorState;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.asset.builder.InstructionType;
import com.hypixel.hytale.server.npc.corecomponents.builders.BuilderActionBase;
import com.hypixel.hytale.server.npc.instructions.Action;
import com.mrbysco.armorstand.action.ActionInteractArmorStand;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class BuilderInteractArmorStand extends BuilderActionBase {
	@Nonnull
	@Override
	public String getShortDescription() {
		return "Interact with the Armor Stand";
	}

	@Nonnull
	@Override
	public String getLongDescription() {
		return this.getShortDescription();
	}

	@Nonnull
	public Action build(@Nonnull BuilderSupport builderSupport) {
		return new ActionInteractArmorStand(this, builderSupport);
	}

	@Nonnull
	@Override
	public BuilderDescriptorState getBuilderDescriptorState() {
		return BuilderDescriptorState.Stable;
	}

	@Nonnull
	public BuilderInteractArmorStand readConfig(@Nonnull JsonElement data) {
		this.requireInstructionType(EnumSet.of(InstructionType.Interaction));
		return this;
	}
}