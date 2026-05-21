// Acar Acay
// Kayra Yeşilkaya
// İbrahim Ege Kırtık
// Bager Ekin
// System Health and Audit Tool — SE311 Project #3
package healthaudit;

/*
 * COMMAND PATTERN — Purpose:
 * Encapsulate each audit operation (security audit, resource optimisation)
 * as an independent object that can be queued, logged or executed later.
 * The TaskInvoker holds the list and fires every command in order. Each
 * command keeps a reference to the Composite root (Computer) so the
 * Command and Composite patterns are connected.
 */

import java.util.ArrayList;
import java.util.List;

/** Common contract for any executable audit task. */
interface SystemTask {
    void execute();
}

/** Concrete command — scans for open ports and unpatched vulnerabilities. */
class SecurityAuditor implements SystemTask {
    private Computer target;

    public SecurityAuditor(Computer target) {
        this.target = target;
    }

    public void execute() {
        System.out.println("[SecurityAuditor] executing on Composite target:");
        target.displayInfo();
        System.out.println("[SecurityAuditor] scanning open TCP/UDP ports...");
        System.out.println("[SecurityAuditor] checking for unpatched CVEs against installed packages...");
        System.out.println("[SecurityAuditor] result: 2 informational findings, 0 critical");
    }
}

/** Concrete command — detects memory-hungry processes and proposes optimisations. */
class ResourceOptimizer implements SystemTask {
    private Computer target;

    public ResourceOptimizer(Computer target) {
        this.target = target;
    }

    public void execute() {
        System.out.println("[ResourceOptimizer] executing on Composite target:");
        target.displayInfo();
        System.out.println("[ResourceOptimizer] identifying memory-hungry processes...");
        System.out.println("[ResourceOptimizer] suggestion: throttle 'background-indexer' (1.2 GB RSS)");
        System.out.println("[ResourceOptimizer] suggestion: cap browser tab cache");
    }
}

/** Invoker — owns a queue of commands and runs them in submission order. */
class TaskInvoker {
    private List<SystemTask> tasks = new ArrayList<SystemTask>();

    public void addTask(SystemTask t) {
        tasks.add(t);
    }

    public void executeAll() {
        System.out.println("[TaskInvoker] dispatching " + tasks.size() + " task(s)");
        for (SystemTask t : tasks) {
            t.execute();
        }
    }
}
