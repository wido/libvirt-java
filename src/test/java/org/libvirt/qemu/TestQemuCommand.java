package org.libvirt.qemu;

import junit.framework.TestCase;

public final class TestQemuCommand extends TestCase {

    public void testCommandWithoutArguments() {
        QemuCommand command = QemuCommand.create(QemuCommand.Command.GUEST_INFO);
        assertEquals("{\"execute\": \"guest-info\"}", command.toString());
    }

    public void testCommandWithoutNonRequiredArguments() {
        try {
            QemuCommand command = QemuCommand.create(QemuCommand.Command.GUEST_INFO, "I am Not Required");
            fail("Expected a IllegalArgumentException which was not thrown");
        } catch(IllegalArgumentException e) {}
    }

    public void testRawCommand() {
        String rawcommand = "this is a raw string";
        QemuCommand command = QemuCommand.raw(rawcommand);
        assertEquals(rawcommand, command.toString());
    }
}
