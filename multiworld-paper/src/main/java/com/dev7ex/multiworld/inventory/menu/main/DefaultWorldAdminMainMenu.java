package com.dev7ex.multiworld.inventory.menu.main;

import com.dev7ex.multicommon.api.inventory.itemstack.ItemStackBuilder;
import com.dev7ex.multicommon.api.inventory.slot.EmptyInventorySlot;
import com.dev7ex.multicommon.api.inventory.slot.InventorySlot;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.inventory.menu.WorldInventoryMenu;
import com.dev7ex.multiworld.api.inventory.menu.main.WorldAdminMainMenu;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.util.PaginationUtil;
import com.dev7ex.multiworld.api.world.WorldContext;
import com.dev7ex.multiworld.api.world.WorldRegistry;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * @author Dev7ex
 * @since 19.06.2025
 */
public class DefaultWorldAdminMainMenu extends WorldInventoryMenu implements WorldAdminMainMenu {

    private static final int ROWS = 6;
    private static final int AMOUNT_PER_PAGE = 36;
    private static final int NAVIGATION_ROW_START = DefaultWorldAdminMainMenu.AMOUNT_PER_PAGE + 9;
    private static final int CLOSE_MENU_SLOT = DefaultWorldAdminMainMenu.NAVIGATION_ROW_START;
    private static final int PREVIOUS_PAGE_SLOT = DefaultWorldAdminMainMenu.NAVIGATION_ROW_START + 1;
    private static final int CREATE_WORLD_SLOT = DefaultWorldAdminMainMenu.NAVIGATION_ROW_START + 3;
    private static final int OVERVIEW_SLOT = DefaultWorldAdminMainMenu.NAVIGATION_ROW_START + 4;
    private static final int IMPORT_WORLD_SLOT = DefaultWorldAdminMainMenu.NAVIGATION_ROW_START + 5;
    private static final int NEXT_PAGE_SLOT = DefaultWorldAdminMainMenu.NAVIGATION_ROW_START + 7;

    private final WorldLocalizationRegistry localizationRegistry = MultiWorldPlugin.getInstance().getLocalizationRegistry();
    private final WorldRegistry worldProvider = MultiWorldPlugin.getInstance().getWorldRegistry();

    private final int page;

    public DefaultWorldAdminMainMenu(@NotNull final Player player, @NotNull final Component title, final int page) {
        super(player, title.replaceText(builder -> builder.match("%current_page%").replacement(String.valueOf(page))), DefaultWorldAdminMainMenu.ROWS);
        this.page = page;

        this.populateWorlds(player);
        this.populateNavigationBar(player);
    }

    @Override
    public void onClick(@NotNull final InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onClose(@NotNull final InventoryCloseEvent event) {
        super.getPlayer().playSound(Sound.sound().type(SoundEventKeys.BLOCK_BARREL_CLOSE).build());
    }

    @Override
    public void onDrag(@NotNull final InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onOpen(@NotNull final InventoryOpenEvent event) {
        this.onUpdate();
        super.getPlayer().playSound(Sound.sound().type(SoundEventKeys.BLOCK_BARREL_OPEN).build());
    }

    @Override
    public void onUpdate() {
        this.populateWorlds(super.getPlayer());
    }

    @Override
    public void populateNavigationBar(@NotNull final Player player) {
        for (int slot = DefaultWorldAdminMainMenu.AMOUNT_PER_PAGE; slot < DefaultWorldAdminMainMenu.AMOUNT_PER_PAGE + 9; slot++) {
            super.setSlot(slot, new ItemStackBuilder(Material.BLACK_STAINED_GLASS_PANE)
                    .editMeta(ItemMeta.class, itemMeta -> itemMeta.displayName(Component.text(" ")))
                    .build());
        }

        super.setSlot(DefaultWorldAdminMainMenu.CLOSE_MENU_SLOT, new InventorySlot(new ItemStackBuilder(Material.SLIME_BALL)
                .editMeta(ItemMeta.class, itemMeta -> {
                    itemMeta.displayName(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.ITEM_NAVIGATION_CLOSE_NAME));
                    itemMeta.lore(this.localizationRegistry.getItemLore(player, WorldLocalizationEntry.ITEM_NAVIGATION_CLOSE_LORE));

                }).build(), event -> player.closeInventory()));

        if (this.page > 1) {
            super.setSlot(DefaultWorldAdminMainMenu.PREVIOUS_PAGE_SLOT, new InventorySlot(new ItemStackBuilder(Material.ARROW)
                    .editMeta(ItemMeta.class, itemMeta -> {
                        itemMeta.displayName(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.ITEM_NAVIGATION_PREVIOUS_NAME));
                        itemMeta.lore(this.localizationRegistry.getItemLore(player, WorldLocalizationEntry.ITEM_NAVIGATION_PREVIOUS_LORE));
                    }).build(), event -> {

                player.openInventory(new DefaultWorldAdminMainMenu(player, super.getTitle(), this.page - 1).getInventory());
                player.playSound(Sound.sound().type(SoundEventKeys.UI_BUTTON_CLICK).build());
            }));
        }

        super.setSlot(DefaultWorldAdminMainMenu.CREATE_WORLD_SLOT, new InventorySlot(new ItemStackBuilder(Material.CRAFTING_TABLE)
                .editMeta(ItemMeta.class, itemMeta -> {
                    itemMeta.displayName(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.INVENTORY_WORLD_MAIN_ADMIN_ITEM_CREATE_WORLD_NAME));
                    itemMeta.lore(this.localizationRegistry.getItemLore(player, WorldLocalizationEntry.INVENTORY_WORLD_MAIN_ADMIN_ITEM_CREATE_WORLD_LORE));
                })
                .build(), event -> {

            /*final WorldCreateMenu createMenu = new DefaultWorldCreateMenu(player,
                    this.translationProvider.getComponent(player, WorldTranslationProperty.INVENTORY_WORLD_CREATE_TITLE),
                    this);

            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            player.openInventory(createMenu.getInventory());
            player.playSound(Sound.sound().type(SoundEventKeys.UI_BUTTON_CLICK).build());*/

        }));

        super.setSlot(DefaultWorldAdminMainMenu.OVERVIEW_SLOT, new InventorySlot(new ItemStackBuilder(Material.PAPER)
                .editMeta(ItemMeta.class, itemMeta -> {
                    final Map<String, String> placeholders = Map.of(
                            "%current_page%", String.valueOf(this.page),
                            "%maximal_pages%", String.valueOf(PaginationUtil.getPageCount(this.getWorlds(), DefaultWorldAdminMainMenu.AMOUNT_PER_PAGE))
                    );
                    itemMeta.displayName(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.ITEM_NAVIGATION_OVERVIEW_NAME));
                    itemMeta.lore(this.localizationRegistry.getItemLore(player, WorldLocalizationEntry.ITEM_NAVIGATION_OVERVIEW_LORE, placeholders));

                }).build(), event -> player.playSound(Sound.sound().type(SoundEventKeys.BLOCK_NOTE_BLOCK_BASS).build())));

        super.setSlot(DefaultWorldAdminMainMenu.IMPORT_WORLD_SLOT, new InventorySlot(new ItemStackBuilder(Material.CHEST_MINECART)
                .editMeta(ItemMeta.class, itemMeta -> {
                    itemMeta.displayName(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.INVENTORY_WORLD_MAIN_ADMIN_ITEM_IMPORT_WORLD_NAME));
                    itemMeta.lore(this.localizationRegistry.getItemLore(player, WorldLocalizationEntry.INVENTORY_WORLD_MAIN_ADMIN_ITEM_IMPORT_WORLD_LORE));
                })
                .build(), event -> {


        }));

        final int maxPages = PaginationUtil.getPageCount(this.getWorlds(), DefaultWorldAdminMainMenu.AMOUNT_PER_PAGE);
        if (this.page < maxPages) {
            super.setSlot(DefaultWorldAdminMainMenu.NEXT_PAGE_SLOT, new InventorySlot(new ItemStackBuilder(Material.ARROW)
                    .editMeta(ItemMeta.class, itemMeta -> {
                        itemMeta.displayName(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.ITEM_NAVIGATION_NEXT_NAME));
                        itemMeta.lore(this.localizationRegistry.getItemLore(player, WorldLocalizationEntry.ITEM_NAVIGATION_NEXT_LORE));
                    }).build(), event -> {
                player.openInventory(new DefaultWorldAdminMainMenu(player, super.getTitle(), this.page + 1).getInventory());
                player.playSound(Sound.sound().type(SoundEventKeys.UI_BUTTON_CLICK).build());
            }));
        }
    }

    @Override
    public void populateWorlds(@NotNull final Player player) {
        final List<WorldContext> worlds = this.getWorlds();
        final List<WorldContext> visibleWorlds = PaginationUtil.splitPage(worlds, DefaultWorldAdminMainMenu.AMOUNT_PER_PAGE, this.page);

        for (int slot = 0; slot < DefaultWorldAdminMainMenu.AMOUNT_PER_PAGE; slot++) {
            super.setSlot(slot, new EmptyInventorySlot());
        }

        for (int index = 0; index < visibleWorlds.size(); index++) {
            final WorldContext world = visibleWorlds.get(index);

            final Map<String, String> placeholders = Map.of(
                    "%creator_name%", world.getCreatorName(),
                    "%registration_timestamp%",
                    this.localizationRegistry.getDateTimeFormat(player)
                            .format(Instant.ofEpochMilli(world.getRegistrationTimestamp())
                                    .atZone(ZoneId.systemDefault())),
                    "%loaded%", world.isLoaded() ? "§atrue" : "§cfalse",
                    "%world_player_count%", String.valueOf(0),
                    "%world_group_display_name%", world.getGroup().getDisplayName()
            );
            final ItemStack itemStack = new ItemStackBuilder(world.getIcon())
                    .editMeta(ItemMeta.class, itemMeta -> {
                        itemMeta.displayName(this.localizationRegistry.getComponent(player, WorldLocalizationEntry.INVENTORY_WORLD_MAIN_ADMIN_ITEM_WORLD_NAME)
                                .replaceText(builder -> builder.match("%world_colored_display_name%").replacement(world.getColoredDisplayName())));
                        itemMeta.lore(this.localizationRegistry.getItemLore(player, WorldLocalizationEntry.INVENTORY_WORLD_MAIN_ADMIN_ITEM_WORLD_LORE, placeholders));
                    })
                    .build();

            super.setSlot(index, new InventorySlot(itemStack, event -> {


            }));
        }
    }

    @Override
    public List<WorldContext> getWorlds() {
        return this.worldProvider.getWorlds();
    }

}
