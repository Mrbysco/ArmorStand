package com.mrbysco.armorstand.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.systems.NPCSystems;
import com.mrbysco.armorstand.util.InventoryUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.Set;

public class ArmorStandSystems {

	public static class DropDeathItems extends DeathSystems.OnDeathSystem {
		@Nonnull
		private static final Query<EntityStore> QUERY = Query.and(
				NPCEntity.getComponentType(), TransformComponent.getComponentType(), HeadRotation.getComponentType(), Query.not(Player.getComponentType())
		);

		@Nonnull
		private static final Set<Dependency<EntityStore>> DEPENDENCIES = Set.of(
				new SystemDependency<>(Order.BEFORE, NPCSystems.OnDeathSystem.class)
		);

		@Nonnull
		@Override
		public Query<EntityStore> getQuery() {
			return QUERY;
		}

		@NonNullDecl
		@Override
		public Set<Dependency<EntityStore>> getDependencies() {
			return DEPENDENCIES;
		}

		@Override
		public void onComponentAdded(
				@Nonnull Ref<EntityStore> ref,
				@Nonnull DeathComponent component,
				@Nonnull Store<EntityStore> store,
				@Nonnull CommandBuffer<EntityStore> commandBuffer
		) {
			NPCEntity npcentity = commandBuffer.getComponent(ref, NPCEntity.getComponentType());

			assert npcentity != null;

			String npcTypeID = npcentity.getNPCTypeId();

			if (npcTypeID.equals("Armor_Stand")) {
				TransformComponent transformcomponent = store.getComponent(ref, TransformComponent.getComponentType());
				assert transformcomponent != null;

				InventoryUtil.dropInventory(commandBuffer, ref, transformcomponent);
			}
		}

	}
}
