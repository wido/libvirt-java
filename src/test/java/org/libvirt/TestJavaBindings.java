package org.libvirt;

import java.util.UUID;

import junit.framework.TestCase;

public class TestJavaBindings extends TestCase {

    int UUIDArray[] = { Integer.decode("0x00"), Integer.decode("0x4b"), Integer.decode("0x96"), Integer.decode("0xe1"), Integer.decode("0x2d"), Integer.decode("0x78"), Integer.decode("0xc3"), Integer.decode("0x0f"), Integer.decode("0x5a"), Integer.decode("0xa5"), Integer.decode("0xf0"), Integer.decode("0x3c"), Integer.decode("0x87"), Integer.decode("0xd2"), Integer.decode("0x1e"), Integer.decode("0x67") };

    public void testConnection() throws Exception {
        Connect conn = new Connect("test:///default", false);
        assertEquals("conn.getType()", "Test", conn.getType());
        assertEquals("conn.getURI()", "test:///default", conn.getURI());
        assertEquals("conn.getMaxVcpus(xen)", 32, conn.getMaxVcpus("xen"));
        assertNotNull("conn.getHostName()", conn.getHostName());
        assertNotNull("conn.getCapabilities()", conn.getCapabilities());
        assertTrue("conn.getLibVirVersion()", conn.getLibVirVersion() > 6000);
        assertEquals("conn.getLibVirVersion()", 2, conn.getVersion());
    }

    public void testNodeInfo() throws Exception {
        Connect conn = new Connect("test:///default", false);
        NodeInfo nodeInfo = conn.nodeInfo();
        assertEquals("nodeInfo.model", "i686", nodeInfo.model);
        assertEquals("nodeInfo.memory", 3145728, nodeInfo.memory);
        assertEquals("nodeInfo.cpus", 16, nodeInfo.cpus);
        assertEquals("nodeInfo.nodes", 2, nodeInfo.nodes);
        assertEquals("nodeInfo.sockets", 2, nodeInfo.sockets);
        assertEquals("nodeInfo.cores", 2, nodeInfo.cores);
        assertEquals("nodeInfo.threads", 2, nodeInfo.threads);
    }

    public void testNetworkCreate() throws Exception {
        Connect conn = new Connect("test:///default", false);

        Network network1 = conn.networkCreateXML("<network>" + "  <name>createst</name>" + "  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e68</uuid>" + "  <bridge name='createst'/>" + "  <forward dev='eth0'/>" + "  <ip address='192.168.66.1' netmask='255.255.255.0'>" + "    <dhcp>" + "      <range start='192.168.66.128' end='192.168.66.253'/>" + "    </dhcp>" + "  </ip>" + "</network>");
        Network network2 = conn.networkDefineXML("<network>" + "  <name>deftest</name>" + "  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e67</uuid>" + "  <bridge name='deftest'/>" + "  <forward dev='eth0'/>" + "  <ip address='192.168.88.1' netmask='255.255.255.0'>" + "    <dhcp>" + "      <range start='192.168.88.128' end='192.168.88.253'/>" + "    </dhcp>" + "  </ip>" + "</network>");
        assertEquals("Number of networks", 2, conn.numOfNetworks());
        assertEquals("Number of listed networks", 2, conn.listNetworks().length);
        assertEquals("Number of defined networks", 1, conn.numOfDefinedNetworks());
        assertEquals("Number of listed defined networks", 1, conn.listDefinedNetworks().length);
        this.validateNetworkData(network2);
        this.validateNetworkData(conn.networkLookupByName("deftest"));
        this.validateNetworkData(conn.networkLookupByUUID(UUIDArray));
        this.validateNetworkData(conn.networkLookupByUUIDString("004b96e1-2d78-c30f-5aa5-f03c87d21e67"));
        this.validateNetworkData(conn.networkLookupByUUID(UUID.fromString("004b96e1-2d78-c30f-5aa5-f03c87d21e67")));
        // this should throw an exception
        try {
            network1.create();
        } catch (LibvirtException e) {
            // eat it
        }
    }

    public void validateNetworkData(Network network) throws Exception {
        assertEquals("network.getName()", "deftest", network.getName());
        assertEquals("network.getBridgeName()", "deftest", network.getBridgeName());
        assertEquals("network.getUUIDString()", "004b96e1-2d78-c30f-5aa5-f03c87d21e67", network.getUUIDString());
        assertFalse("network.getAutostart()", network.getAutostart());
        assertNotNull("network.getConnect()", network.getConnect());
        assertNotNull("network.getUUID()", network.getUUID());
        assertNotNull("network.getXMLDesc()", network.getXMLDesc(0));
    }

    public void testDomainCreate() throws Exception {
        Connect conn = new Connect("test:///default", false);

        Domain dom1 = conn.domainDefineXML("<domain type='test' id='2'>" + "  <name>deftest</name>" + "  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e70</uuid>" + "  <memory>8388608</memory>" + "  <vcpu>2</vcpu>" + "  <os><type arch='i686'>hvm</type></os>" + "  <on_reboot>restart</on_reboot>" + "  <on_poweroff>destroy</on_poweroff>" + "  <on_crash>restart</on_crash>" + "</domain>");

        Domain dom2 = conn.domainCreateLinux("<domain type='test' id='3'>" + "  <name>createst</name>" + "  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e67</uuid>" + "  <memory>8388608</memory>" + "  <vcpu>2</vcpu>" + "  <os><type arch='i686'>hvm</type></os>" + "  <on_reboot>restart</on_reboot>" + "  <on_poweroff>destroy</on_poweroff>" + "  <on_crash>restart</on_crash>" + "</domain>", 0);

        assertEquals("Number of domains", 2, conn.numOfDomains());
        assertEquals("Number of listed domains", 2, conn.listDomains().length);
        assertEquals("Number of defined domains", 1, conn.numOfDefinedDomains());
        assertEquals("Number of listed defined domains", 1, conn.listDefinedDomains().length);
        this.validateDomainkData(dom2);
        this.validateDomainkData(conn.domainLookupByName("createst"));
        this.validateDomainkData(conn.domainLookupByUUID(UUIDArray));
        this.validateDomainkData(conn.domainLookupByUUIDString("004b96e1-2d78-c30f-5aa5-f03c87d21e67"));
        this.validateDomainkData(conn.domainLookupByUUID(UUID.fromString("004b96e1-2d78-c30f-5aa5-f03c87d21e67")));
    }

    public void validateDomainkData(Domain dom) throws Exception {
        assertEquals("dom.getName()", "createst", dom.getName());
        assertEquals("dom.getMaxMemory()", 8388608, dom.getMaxMemory());
        // Not supported by the test driver
        // assertEquals("dom.getMaxVcpus()", 2, dom2.getMaxVcpus()) ;
        assertEquals("dom.getOSType()", "linux", dom.getOSType());
        assertEquals("dom.getUUIDString()", "004b96e1-2d78-c30f-5aa5-f03c87d21e67", dom.getUUIDString());
        assertFalse("dom.getAutostart()", dom.getAutostart());
        assertNotNull("dom.getConnect()", dom.getConnect());
        assertNotNull("dom.getUUID()", dom.getUUID());
        assertNotNull("dom.getXMLDesc()", dom.getXMLDesc(0));
        assertNotNull("dom.getID()", dom.getID());

        // Execute the code Iterate over the parameters the easy way
        for (SchedParameter c : dom.getSchedulerParameters()) {
            System.out.println(c.getTypeAsString() + ":" + c.field + ":" + c.getValueAsString());
        }

        SchedUintParameter[] pars = new SchedUintParameter[1];
        pars[0] = new SchedUintParameter();
        pars[0].field = "weight";
        pars[0].value = 100;
        dom.setSchedulerParameters(pars);
    }

    // TODO GO BACK AND GET THIS
    public void testInterfaces() throws Exception {
        // Connect conn = new Connect("test:///default", false);
        // System.out.println("numOfInterfaces:" + conn.numOfInterfaces());
        // System.out.println("listDefinedInterfaces:" + conn.listInterfaces());
    }

    public void testAccessAfterClose() throws Exception {
        Connect conn = new Connect("test:///default", false);
        conn.close();
        try {
            conn.getHostName();
        } catch (LibvirtException e) {
            // eat it
        }
    }
}