package com.mrbysco.armorstand.util;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.inventory.transaction.MoveTransaction;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.item.ItemComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.List;

public class InventoryUtil {
	public static void dropInventory(CommandBuffer<EntityStore> commandBuffer, Ref<EntityStore> ref, TransformComponent transformcomponent) {
		try {
			var entity = EntityUtils.getEntity(ref, commandBuffer);
			if (entity instanceof LivingEntity livingEntity) {
				Inventory inventory = livingEntity.getInventory();
				if (inventory == null) return;

				List<ItemStack> stacks = inventory.dropAllItemStacks();
				if (stacks == null || stacks.isEmpty()) return;

				for (ItemStack stack : stacks) {
					if (stack == null) continue;
					Holder<EntityStore> dropHolder = ItemComponent.generateItemDrop(
							commandBuffer,
							stack,
							transformcomponent.getPosition(),
							transformcomponent.getRotation(),
							0.0F,
							2.0F,
							0.0F
					);
					if (dropHolder != null) commandBuffer.addEntity(dropHolder, AddReason.SPAWN);
				}
			}
		} catch (Exception ignored) {
			// Ignored
		}
	}

	public static boolean moveHeldToContainer(@Nonnull InteractionContext context, ItemContainer dest, int targetSlot) {
		if (dest == null) return false;
		ItemStack held = context.getHeldItem();
		if (held == null) return false;

		byte activeSlot = context.getHeldItemSlot();
		MoveTransaction<?> tx;
		if (targetSlot >= 0) {
			tx = context.getHeldItemContainer().moveItemStackFromSlotToSlot(activeSlot, held.getQuantity(), dest, (byte) targetSlot);
		} else {
			tx = context.getHeldItemContainer().moveItemStackFromSlot(activeSlot, held.getQuantity(), dest);
		}
		return tx != null && tx.succeeded();
	}

	public static boolean removeFirstFromContainer(ItemContainer src, @Nonnull InteractionContext context) {
		if (src == null) return false;
		for (byte i = 0; i < src.getCapacity(); i++) {
			ItemStack stack = src.getItemStack(i);
			if (stack == null) continue;
			MoveTransaction<?> tx = src.moveItemStackFromSlot(i, stack.getQuantity(), context.getHeldItemContainer());
			if (tx == null || !tx.succeeded()) {
				context.getState().state = InteractionState.Failed;
			}
			return true;
		}
		return false;
	}
}
