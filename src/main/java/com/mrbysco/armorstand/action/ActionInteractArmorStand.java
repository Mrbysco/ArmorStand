package com.mrbysco.armorstand.action;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.InteractionManager;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.InteractionModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.npc.asset.builder.BuilderSupport;
import com.hypixel.hytale.server.npc.corecomponents.ActionBase;
import com.hypixel.hytale.server.npc.entities.NPCEntity;
import com.hypixel.hytale.server.npc.role.Role;
import com.hypixel.hytale.server.npc.sensorinfo.InfoProvider;
import com.mrbysco.armorstand.builder.BuilderInteractArmorStand;
import com.mrbysco.armorstand.util.InteractUtil;
import com.mrbysco.armorstand.util.InventoryUtil;

import javax.annotation.Nonnull;

public class ActionInteractArmorStand extends ActionBase {

	public ActionInteractArmorStand(@Nonnull BuilderInteractArmorStand builder, @Nonnull BuilderSupport support) {
		super(builder);
	}

	@Override
	public boolean canExecute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
		return super.canExecute(ref, role, sensorInfo, dt, store) && role.getStateSupport().getInteractionIterationTarget() != null;
	}

	@Override
	public boolean execute(@Nonnull Ref<EntityStore> ref, @Nonnull Role role, InfoProvider sensorInfo, double dt, @Nonnull Store<EntityStore> store) {
		super.execute(ref, role, sensorInfo, dt, store);
		InteractionManager interactionmanager = store.getComponent(ref, InteractionModule.get().getInteractionManagerComponent());
		assert interactionmanager != null;

		NPCEntity npcentity = store.getComponent(ref, NPCEntity.getComponentType());

		assert npcentity != null;

		Ref<EntityStore> targetRef = role.getStateSupport().getInteractionIterationTarget();
		if (targetRef == null) {
			return false;
		} else {
			Player player = store.getComponent(targetRef, Player.getComponentType());

			assert player != null;

			Inventory inventory = npcentity.getInventory();
			if (inventory == null) return false;

			ItemStack held = player.getInventory().getItemInHand();
			InteractionContext context = InteractionContext.forInteraction(interactionmanager, targetRef, InteractionType.Held, store);

			if (held != null) {
				InteractUtil.handleHeldItem(context, inventory, held);
			} else {
				if (InventoryUtil.removeFirstFromContainer(inventory.getHotbar(), context)) return true;
				if (InventoryUtil.removeFirstFromContainer(inventory.getUtility(), context)) return true;
				InventoryUtil.removeFirstFromContainer(inventory.getArmor(), context);
			}

			return true;
		}
	}
}
