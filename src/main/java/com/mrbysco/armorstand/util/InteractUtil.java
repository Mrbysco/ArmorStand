package com.mrbysco.armorstand.util;

import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemArmor;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemTool;
import com.hypixel.hytale.server.core.asset.type.item.config.ItemUtility;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;

import javax.annotation.Nonnull;

public class InteractUtil {
	public static void handleHeldItem(@Nonnull InteractionContext context, @Nonnull Inventory inventory, @Nonnull ItemStack held) {
		Item item = held.getItem();
		if (item == null) {
			context.getState().state = InteractionState.Failed;
			return;
		}

		ItemArmor itemArmor = item.getArmor();
		if (itemArmor != null) {
			ItemContainer armorContainer = inventory.getArmor();
			if (armorContainer == null) {
				context.getState().state = InteractionState.Failed;
				return;
			}
			int targetSlot = itemArmor.getArmorSlot().ordinal();
			if (targetSlot < 0 || targetSlot >= armorContainer.getCapacity()) {
				context.getState().state = InteractionState.Failed;
				return;
			}
			boolean ok = InventoryUtil.moveHeldToContainer(context, armorContainer, targetSlot);
			if (!ok) context.getState().state = InteractionState.Failed;
			return;
		}

		ItemTool itemTool = item.getTool();
		ItemUtility itemUtility = item.getUtility();
		if (itemTool != null || itemUtility != null) {
			ItemContainer hotbar = inventory.getHotbar();
			boolean moved = InventoryUtil.moveHeldToContainer(context, hotbar, -1);
			if (!moved) {
				moved = InventoryUtil.moveHeldToContainer(context, hotbar, 0);
			}
			if (!moved) context.getState().state = InteractionState.Failed;
			return;
		}

		context.getState().state = InteractionState.Failed;
	}
}
