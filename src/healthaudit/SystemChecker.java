// Acar Acay
// Kayra Yeşilkaya
// İbrahim Ege Kırtık
// Bager Ekin
// System Health and Audit Tool — SE311 Project #3
package healthaudit;

/*
 * TEMPLATE METHOD PATTERN — Purpose:
 * Pin down the invariant 5-step health-check algorithm
 * (setup -> collectData -> checkData -> analyze -> generateReport)
 * as a final method on the abstract base class. Subclasses
 * (LocalMachineChecker / RemoteServerChecker) only customise setup().
 * This class also wires together the Adapter (SystemAPI) and the
 * Composite (Computer) — they meet here.
 */

/** Skeleton of the health-check workflow; subclasses vary only the setup step. */
abstract class SystemChecker {
    protected SystemAPI api;
    protected Computer system;

    public SystemChecker(SystemAPI api, Computer system) {
        this.api = api;
        this.system = system;
    }

    /** Template method — the 5-step algorithm. Subclasses cannot reorder it. */
    public final void runCheck() {
        System.out.println("\n========== [SystemChecker] runCheck() START ==========");
        setup();
        collectData();
        checkData();
        analyze();
        generateReport();
        System.out.println("========== [SystemChecker] runCheck() END ============\n");
    }

    /** Step 1 — varies between Local and Remote subclasses. */
    protected abstract void setup();

    /** Step 2 — pull metrics via the Adapter and store them on the Composite root. */
    protected void collectData() {
        System.out.println("[SystemChecker] Step 2: collecting data via SystemAPI...");
        String sys = api.getSystemData();
        String mem = api.getMemoryUsage();
        String proc = api.getProcessUsage();
        String disk = api.getDiskUsage();
        String combined = sys + " | " + mem + " | " + proc + " | " + disk;
        system.setSystemInfo(combined);
        System.out.println("[SystemChecker] Composite snapshot:");
        system.displayInfo();
    }

    /** Step 3 — sanity-check the collected payload. */
    protected void checkData() {
        System.out.println("[SystemChecker] Step 3: checking data...");
        String sample = api.getSystemData();
        if (sample != null && !sample.isEmpty()) {
            System.out.println("[SystemChecker] data valid");
        } else {
            System.out.println("[SystemChecker] data invalid");
        }
    }

    /** Step 4 — announces the analysis phase; real audit work is done by Commands. */
    protected void analyze() {
        System.out.println("[SystemChecker] Step 4: Analysis phase starting");
    }

    /** Step 5 — produces the final textual report. */
    protected void generateReport() {
        System.out.println("[SystemChecker] Step 5: generating report");
        System.out.println("[SystemChecker] === REPORT === health-check completed successfully");
    }
}

/** Concrete checker — opens local resource handles before the standard steps run. */
class LocalMachineChecker extends SystemChecker {
    public LocalMachineChecker(SystemAPI api, Computer system) {
        super(api, system);
    }

    protected void setup() {
        System.out.println("[LocalMachineChecker] Step 1: acquiring local resource handles");
    }
}

/** Concrete checker — opens a network socket before the standard steps run. */
class RemoteServerChecker extends SystemChecker {
    public RemoteServerChecker(SystemAPI api, Computer system) {
        super(api, system);
    }

    protected void setup() {
        System.out.println("[RemoteServerChecker] Step 1: opening socket connection to remote host");
    }
}
