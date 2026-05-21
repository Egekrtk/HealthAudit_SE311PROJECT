// Acar Acay
// Kayra Yeşilkaya
// İbrahim Ege Kırtık
// Bager Ekin
// System Health and Audit Tool — SE311 Project #3
package healthaudit;

/*
 * COMPOSITE PATTERN — Purpose:
 * Model the part-whole hierarchy of computer hardware so that individual
 * components (CPU, Memory, NICCard, Disk) and composite components
 * (ISABus, Motherboard, Computer) can be treated uniformly through a
 * single HardwareComponent interface. The SystemChecker and SystemTask
 * layers traverse and operate on this tree without caring whether a
 * node is a leaf or a composite.
 */

import java.util.ArrayList;
import java.util.List;

/** Common abstraction for every node in the hardware tree (leaf or composite). */
interface HardwareComponent {
    void displayInfo();
}

/** Leaf — represents a single CPU chip. */
class CPU implements HardwareComponent {
    public void displayInfo() {
        System.out.println("    [CPU] Intel-class processor, 8 cores @ 3.4 GHz");
    }
}

/** Leaf — represents a memory module. */
class Memory implements HardwareComponent {
    public void displayInfo() {
        System.out.println("    [Memory] 16 GB DDR4 RAM");
    }
}

/** Leaf — represents a Network Interface Card. */
class NICCard implements HardwareComponent {
    public void displayInfo() {
        System.out.println("      [NICCard] Gigabit Ethernet controller");
    }
}

/** Leaf — represents a disk attached to the expansion bus. */
class Disk implements HardwareComponent {
    public void displayInfo() {
        System.out.println("      [Disk] 512 GB SSD, SATA III");
    }
}

/** Composite — the ISA bus holds expansion cards such as NICs and disks. */
class ISABus implements HardwareComponent {
    private List<HardwareComponent> children = new ArrayList<HardwareComponent>();

    public void addComponent(HardwareComponent c) {
        children.add(c);
    }

    public void displayInfo() {
        System.out.println("    [ISABus] 16-bit expansion bus");
        for (HardwareComponent c : children) {
            c.displayInfo();
        }
    }
}

/** Composite — the motherboard wires together CPU, Memory and the ISA bus. */
class Motherboard implements HardwareComponent {
    private CPU cpu;
    private Memory memory;
    private ISABus isaBus;

    public Motherboard(CPU cpu, Memory memory, ISABus isaBus) {
        this.cpu = cpu;
        this.memory = memory;
        this.isaBus = isaBus;
    }

    public void displayInfo() {
        System.out.println("  [Motherboard] ATX form factor");
        cpu.displayInfo();
        memory.displayInfo();
        isaBus.displayInfo();
    }
}

/** Composite root — the Computer owns a Motherboard and remembers OS-level system info. */
class Computer implements HardwareComponent {
    private Motherboard motherboard;
    private String systemInfo = "";

    public Computer(Motherboard motherboard) {
        this.motherboard = motherboard;
    }

    public void setSystemInfo(String s) {
        this.systemInfo = s;
    }

    public void displayInfo() {
        System.out.println("[Computer] Host machine");
        if (systemInfo != null && !systemInfo.isEmpty()) {
            System.out.println("  systemInfo => " + systemInfo);
        }
        motherboard.displayInfo();
    }
}
