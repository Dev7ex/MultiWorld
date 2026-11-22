
package com.dev7ex.multiworld.command.world.group;

import com.dev7ex.multicommon.api.command.PaperCommand;
import com.dev7ex.multicommon.api.command.PaperCommandArgument;
import com.dev7ex.multicommon.api.command.PaperCommandProperties;
import com.dev7ex.multicommon.api.command.completer.EmptyTabCompleter;
import com.dev7ex.multiworld.MultiWorldPlugin;
import com.dev7ex.multiworld.api.localization.WorldLocalizationEntry;
import com.dev7ex.multiworld.api.localization.WorldLocalizationRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroup;
import com.dev7ex.multiworld.api.world.group.WorldGroupManager;
import com.dev7ex.multiworld.api.world.group.WorldGroupRegistry;
import com.dev7ex.multiworld.api.world.group.WorldGroupRepository;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dev7ex
 * @since 09.07.2025
 */
@PaperCommandProperties(name = "create", permission = "multiworld.command.world.group.create")
public class CreateCommand extends PaperCommand implements EmptyTabCompleter {

    private final WorldLocalizationRegistry localizationRegistry;
    private final WorldGroupManager groupManager;
    private final WorldGroupRepository groupRepository;
    private final WorldGroupRegistry groupRegistry;

    public CreateCommand(@NotNull final MultiWorldPlugin plugin) {
        super(plugin);
        this.localizationRegistry = plugin.getLocalizationRegistry();
        this.groupManager = plugin.getGroupManager();
        this.groupRepository = plugin.getGroupRepository();
        this.groupRegistry = plugin.getGroupRegistry();
    }

    @Override
    public void execute(@NotNull final CommandSender commandSender, @NotNull final PaperCommandArgument[] arguments) {
        commandSender.sendMessage("Arguments: " + arguments.length + " " + java.util.Arrays.toString(java.util.Arrays.stream(arguments).map(PaperCommandArgument::asString).toArray()));

        if (arguments.length != 4) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GROUP_CREATE_USAGE));
            return;
        }
        final String groupName = arguments[2].asString();
        final String idArgument = arguments[3].asString();

        if (groupName.isBlank()) {
            commandSender.sendMessage(this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GROUP_CREATE_USAGE));
            return;
        }
        final int id;

        try {
            id = Integer.parseInt(idArgument);

        } catch (final NumberFormatException exception) {
            commandSender.sendMessage(
                    this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_VALIDATION_INVALID_NUMBER)
                            .replaceText(builder -> builder.matchLiteral("%input%").replacement(idArgument))
            );
            return;
        }
        if (id < 0) {
            commandSender.sendMessage(
                    this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_VALIDATION_INVALID_NUMBER)
                            .replaceText(builder -> builder.matchLiteral("%input%").replacement(idArgument))
            );
            return;
        }
        final java.util.Optional<WorldGroup> existingGroupByName = this.groupRegistry.find(groupName);

        if (existingGroupByName.isPresent()) {
            final WorldGroup group = existingGroupByName.get();

            commandSender.sendMessage(
                    this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_GROUP_ALREADY_EXISTS_NAME)
                            .replaceText(builder -> builder.matchLiteral("%group_name%").replacement(groupName))
                            .replaceText(builder -> builder.matchLiteral("%group_display_name%").replacement(group.getDisplayName()))
                            .replaceText(builder -> builder.matchLiteral("%colored_group_display_name%").replacement(group.getColoredDisplayName()))
            );
            return;
        }
        if (this.groupRegistry.find(id).isPresent()) {
            commandSender.sendMessage(
                    this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.MESSAGE_WORLD_GROUP_ALREADY_EXISTS_ID)
                            .replaceText(builder -> builder.matchLiteral("%group_id%").replacement(String.valueOf(id)))
            );
            return;
        }
        final WorldGroup group = this.groupManager.create(id, groupName, commandSender.getName());

        this.groupRepository.insert(group); // TODO: MOVE
        this.groupRegistry.register(group);

        commandSender.sendMessage(
                this.localizationRegistry.getComponent(commandSender, WorldLocalizationEntry.COMMAND_WORLD_GROUP_CREATE_SUCCESSFULLY_CREATED)
                        .replaceText(builder -> builder.matchLiteral("%group_id%").replacement(String.valueOf(group.getId())))
                        .replaceText(builder -> builder.matchLiteral("%group_name%").replacement(group.getName()))
                        .replaceText(builder -> builder.matchLiteral("%group_display_name%").replacement(group.getDisplayName()))
                        .replaceText(builder -> builder.matchLiteral("%colored_group_display_name%").replacement(group.getColoredDisplayName()))
        );
    }

}
