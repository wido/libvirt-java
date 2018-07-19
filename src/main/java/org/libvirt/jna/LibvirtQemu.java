package org.libvirt.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * The libvirt Qemu interface which is exposed via JNA
 */

public interface LibvirtQemu extends Library {

    LibvirtQemu INSTANCE = (LibvirtQemu) Native.loadLibrary(Platform.isWindows() ? "virt-qemu-0" : "virt-qemu", LibvirtQemu.class);

    CString virDomainQemuAgentCommand(DomainPointer virDomainPtr, String cmd, int timeout, int flags);
}
