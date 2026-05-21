// Acar Acay
// Kayra Yeşilkaya
// İbrahim Ege Kırtık
// Bager Ekin
// System Health and Audit Tool — SE311 Project #3
package healthaudit;

/*
 * FACADE PATTERN — Purpose:
 * Hide the wiring between Adapter, Composite, Template Method and
 * Command behind a single simple entry point. A system administrator
 * only needs SystemHealthFacade — choose OS + mode, optionally add
 * audit tasks, then call runFullAudit() to execute the whole pipeline.
 *
 * Pattern interactions wired here:
 *   Facade -> Template Method (SystemChecker) -> Adapter (SystemAPI) + Composite (Computer)
 *   Facade -> Command (TaskInvoker / SystemTask) -> Composite (Computer)
 */

/** Top-level cockpit that orchestrates the full health-and-audit pipeline. */
public class SystemHealthFacade {
    private SystemAPI api;
    private Computer system;
    private SystemChecker checker;
    private TaskInvoker invoker;

    public SystemHealthFacade(String osType, String mode) {
        // 1) Adapter selection
        if (osType.equalsIgnoreCase("linux")) {
            this.api = new LinuxAdapter(new LinuxAPI());
        } else if (osType.equalsIgnoreCase("windows")) {
            this.api = new WindowsAdapter(new WindowsAPI());
        } else if (osType.equalsIgnoreCase("macos")) {
            this.api = new MacOSAdapter(new MacOSAPI());
        } else {
            throw new IllegalArgumentException("Unknown osType: " + osType);
        }

        // 2) Composite hierarchy: Computer -> Motherboard -> CPU, Memory, ISABus -> { NICCard, Disk }
        CPU cpu = new CPU();
        Memory memory = new Memory();
        ISABus isaBus = new ISABus();
        isaBus.addComponent(new NICCard());
        isaBus.addComponent(new Disk());
        Motherboard motherboard = new Motherboard(cpu, memory, isaBus);
        this.system = new Computer(motherboard);

        // 3) Template Method selection
        if (mode.equalsIgnoreCase("local")) {
            this.checker = new LocalMachineChecker(this.api, this.system);
        } else if (mode.equalsIgnoreCase("remote")) {
            this.checker = new RemoteServerChecker(this.api, this.system);
        } else {
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }

        // 4) Command invoker
        this.invoker = new TaskInvoker();

        System.out.println("[SystemHealthFacade] initialised osType=" + osType + ", mode=" + mode);
    }

    /** Exposed so callers can target commands at the Composite root we own. */
    public Computer getSystem() {
        return system;
    }

    /** Register an audit command to be executed during runFullAudit(). */
    public void addAuditTask(SystemTask task) {
        invoker.addTask(task);
    }

    /** Run the unified pipeline: template-method health check, then queued commands. */
    public void runFullAudit() {
        System.out.println("\n############ [SystemHealthFacade] runFullAudit() ############");
        checker.runCheck();
        invoker.executeAll();
        System.out.println("############ [SystemHealthFacade] full audit complete ########\n");
    }

    /** Demo driver — exercises three scenarios end-to-end. */
    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println(" SE311 Project #3 — System Health and Audit Tool — DEMO");
        System.out.println("================================================================");

        // Scenario 1: Linux local machine
        System.out.println("\n>>>>>> SCENARIO 1: Linux / Local Machine <<<<<<");
        SystemHealthFacade facade1 = new SystemHealthFacade("linux", "local");
        facade1.addAuditTask(new SecurityAuditor(facade1.getSystem()));
        facade1.addAuditTask(new ResourceOptimizer(facade1.getSystem()));
        facade1.runFullAudit();

        // Scenario 2: Windows remote server
        System.out.println("\n>>>>>> SCENARIO 2: Windows / Remote Server <<<<<<");
        SystemHealthFacade facade2 = new SystemHealthFacade("windows", "remote");
        facade2.addAuditTask(new SecurityAuditor(facade2.getSystem()));
        facade2.addAuditTask(new ResourceOptimizer(facade2.getSystem()));
        facade2.runFullAudit();

        // Scenario 3: macOS local machine
        System.out.println("\n>>>>>> SCENARIO 3: macOS / Local Machine <<<<<<");
        SystemHealthFacade facade3 = new SystemHealthFacade("macos", "local");
        facade3.addAuditTask(new SecurityAuditor(facade3.getSystem()));
        facade3.addAuditTask(new ResourceOptimizer(facade3.getSystem()));
        facade3.runFullAudit();

        System.out.println("================================================================");
        System.out.println(" DEMO COMPLETE");
        System.out.println("================================================================");
    }
}
