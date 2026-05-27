package com.mrbysco.armorstand.interaction;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.vector.Rotation3f;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.blockhitbox.BlockBoundingBoxes;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.PropComponent;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.prefab.PrefabCopyableComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.npc.INonPlayerCharacter;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.NPCPlugin;
import it.unimi.dsi.fastutil.Pair;
import org.joml.Vector3d;
import org.joml.Vector3i;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpawnArmorStandInteraction extends SimpleBlockInteraction {
	public static final BuilderCodec<SpawnArmorStandInteraction> CODEC = BuilderCodec.builder(
					SpawnArmorStandInteraction.class, SpawnArmorStandInteraction::new, SimpleBlockInteraction.CODEC
			)
			.documentation("Spawns an Armor Stand when right-clicked on a block")
			.append(new KeyedCodec<>("Light", Codec.BOOLEAN), (interaction, isLight) ->
							interaction.isLight = isLight,
					interaction -> interaction.isLight).add()
			.build();

	public boolean isLight = false;

	@Override
	protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer,
	                                 @Nonnull InteractionType type, @Nonnull InteractionContext context,
	                                 @Nullable ItemStack itemStack, @Nonnull Vector3i targetBlock,
	                                 @Nonnull CooldownHandler cooldownHandler) {
		if (itemStack == null) return;

		Ref<EntityStore> ref = context.getEntity();
		Vector3d vector3d = new Vector3d(targetBlock);
		vector3d.add(0.5, 0.5, 0.5);

		Rotation3f rotation = new Rotation3f();
		HeadRotation headRotation = commandBuffer.getComponent(ref, HeadRotation.getComponentType());
		if (headRotation != null) {
			//Invert the yaw by 180 degrees to make the armor stand face the player
			rotation.setYaw(headRotation.getRotation().yaw() + (float) (Math.PI / 180.0) * 180.0F);
		}

		WorldChunk worldchunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z));
		if (worldchunk != null) {
			BlockType blockType = worldchunk.getBlockType(targetBlock);
			@SuppressWarnings("removal") int i = worldchunk.getRotationIndex(targetBlock.x, targetBlock.y, targetBlock.z);
			BlockBoundingBoxes.RotatedVariantBoxes variantBoxes = BlockBoundingBoxes.getAssetMap()
					.getAsset(blockType.getHitboxTypeIndex())
					.get(i);
			vector3d.add(0.0, variantBoxes.getBoundingBox().max.y - 0.5, 0.0);

			commandBuffer.run(_ -> this.spawnArmorStand(world.getEntityStore().getStore(), vector3d, rotation));
		}
	}

	private void spawnArmorStand(@Nonnull Store<EntityStore> store, @Nonnull Vector3d position, @Nonnull Rotation3f rotation) {
		String npcType = this.isLight ? "Armor_Stand_Light" : "Armor_Stand";
		Pair<Ref<EntityStore>, INonPlayerCharacter> pair = NPCPlugin.get().spawnNPC(store, npcType, null, position, rotation);
		if (pair != null) {
			Ref<EntityStore> ref = pair.first();
			store.ensureComponent(ref, UUIDComponent.getComponentType());
			store.ensureComponent(ref, NetworkId.getComponentType());
			store.ensureComponent(ref, PropComponent.getComponentType());
			store.ensureComponent(ref, PrefabCopyableComponent.getComponentType());
		}
	}

	@Override
	protected void simulateInteractWithBlock(@Nonnull InteractionType type,
	                                         @Nonnull InteractionContext interactionContext,
	                                         @Nullable ItemStack itemStack, @Nonnull World world,
	                                         @Nonnull Vector3i targetBlock) {

	}
}
