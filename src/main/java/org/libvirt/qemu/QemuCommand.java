package org.libvirt.qemu;

import java.lang.IllegalArgumentException;

public class QemuCommand {

    public enum Command {

        GUEST_INFO("guest-info", false),
        GUEST_SYNC_DELIMITED("guest-sync-delimited", true),
        GUEST_SYNC("guest-sync", true),
        GUEST_PING("guest-ping", false),
        GUEST_GET_TIME("guest-get-time", false),
        GUEST_SET_TIME("guest-set-time", true),
        GUEST_SHUTDOWN("guest-shutdown", false),
        GUEST_FILE_OPEN("guest-file-open", true),
        GUEST_FILE_CLOSE("guest-file-close", true),
        GUEST_FILE_READ("guest-file-read", true),
        GUEST_FILE_WRITE("guest-file-write", true),
        GUEST_FILE_SEEK("guest-file-seek", true),
        GUEST_FILE_FLUSH("guest-file-flush", true),
        GUEST_FSFREEZE_STATUS("guest-fsfreeze-status", false),
        GUEST_FSFREEZE_FREEZE("guest-fsfreeze-freeze", false),
        GUEST_FSFREEZE_LIST("guest-fsfreeze-freeze-list", true),
        GUEST_FSFREEZE_THAW("guest-fsfreeze-thaw", false),
        GUEST_FSTRIM("guest-fstrim", true),
        GUEST_SUSPEND_DISK("guest-suspend-disk", false),
        GUEST_SUSPEND_RAM("guest-suspend-ram", false),
        GUEST_SUSPEND_HYBRID("guest-suspend-hybrid", false),
        GUEST_GET_NETWORK_INTERFACES("guest-network-get-interfaces", false),
        GUEST_GET_VCPUS("guest-get-vcpus", false),
        GUEST_SET_VCPUS("guest-set-vcpus", true),
        GUEST_GET_FSINFO("guest-get-fsinfo", false),
        GUEST_SET_USER_PASSWORD("guest-set-user-password", true),
        GUEST_GET_MEMORY_BLOCKS("guest-get-memory-blocks", false),
        GUEST_SET_MEMORY_BLOCKS("guest-set-memory-blocks", true),
        GUEST_GET_MEMORY_BLOCK_INFO("guest-get-memory-block-info", false),
        GUEST_EXEC_STATUS("guest-exec-status", false),
        GUEST_EXEC("guest-exec", true);

        private final String command;
        private final Boolean requiresArguments;

        Command(String command, Boolean requiresArguments) {
            this.command = command;
            this.requiresArguments = requiresArguments;
        }

        private String getCommand() {
            return command;
        }

        private Boolean requiresArguments() {
            return requiresArguments;
        }
    }

    Command command;
    String arguments;
    String raw;

    public static QemuCommand create(Command cmd) {
        return new QemuCommand(cmd, null);
    }

    public static QemuCommand create(Command cmd, String args) {
        return new QemuCommand(cmd, args);
    }

    public static QemuCommand raw(String jsonObject) {
        return new QemuCommand(jsonObject);
    }

    private QemuCommand(Command command, String arguments) {
        if (command.requiresArguments() && arguments == null) {
            throw new IllegalArgumentException(command.getCommand() + " requires a argument");
        } else if(!command.requiresArguments() && arguments != null) {
            throw new IllegalArgumentException(command.getCommand() + " does not take a argument");
        }

        this.command = command;
        this.arguments = arguments;
    }

    private QemuCommand(String raw) {
        this.raw = raw;
    }

    public String toString() {
        if (this.raw != null) {
            return this.raw;
        }

        String json = "";

        json += "{";
        json += "\"execute\": \"" + command.getCommand() + "\"";

        if (arguments != null && arguments.length() > 0) {
            json += "\"arguments\": " + arguments + "\"";
        }

        json += "}";
        return json;
    }
}
