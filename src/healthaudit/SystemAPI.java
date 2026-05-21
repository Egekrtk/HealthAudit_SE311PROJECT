// Acar Acay
// Kayra Yeşilkaya
// İbrahim Ege Kırtık
// Bager Ekin
// System Health and Audit Tool — SE311 Project #3
package healthaudit;

/*
 * ADAPTER PATTERN — Purpose:
 * Three different operating systems expose hardware/process information
 * through incompatible native APIs. The SystemAPI interface defines the
 * unified vocabulary the rest of the program speaks (getSystemData,
 * getMemoryUsage, getProcessUsage, getDiskUsage), and each *Adapter
 * class wraps the raw OS-specific adaptee so the SystemChecker layer
 * never has to branch on the OS at runtime.
 */

/** Unified OS-agnostic facade for metric collection. */
interface SystemAPI {
    String getSystemData();
    String getMemoryUsage();
    String getProcessUsage();
    String getDiskUsage();
}

/* ============================== LINUX ============================== */

/** Adaptee — simulated raw Linux kernel calls. */
class LinuxAPI {
    public String uname() {
        System.out.println("[LinuxAPI] invoking uname()");
        return "Linux 6.5.0-generic x86_64";
    }

    public String fopenProcMeminfo() {
        System.out.println("[LinuxAPI] reading /proc/meminfo");
        return "MemTotal: 16384 MB, MemAvailable: 9123 MB";
    }

    public String fopenProcStat() {
        System.out.println("[LinuxAPI] reading /proc/stat");
        return "cpu 12345 0 6789 98765, running procs: 312";
    }

    public String statvfs() {
        System.out.println("[LinuxAPI] invoking statvfs()");
        return "/: 512 GB total, 211 GB free, 58% used";
    }
}

/** Adapter — translates LinuxAPI calls into the unified SystemAPI shape. */
class LinuxAdapter implements SystemAPI {
    private LinuxAPI linux;

    public LinuxAdapter(LinuxAPI linux) {
        this.linux = linux;
    }

    public String getSystemData() {
        return "System: " + linux.uname();
    }

    public String getMemoryUsage() {
        return "Memory: " + linux.fopenProcMeminfo();
    }

    public String getProcessUsage() {
        return "Process: " + linux.fopenProcStat();
    }

    public String getDiskUsage() {
        return "Disk: " + linux.statvfs();
    }
}

/* ============================= WINDOWS ============================= */

/** Adaptee — simulated raw Windows Win32 calls. */
class WindowsAPI {
    public String GetSystemInfo() {
        System.out.println("[WindowsAPI] invoking GetSystemInfo()");
        return "Windows 11 Pro 23H2 x64";
    }

    public String GlobalMemoryStatusEx() {
        System.out.println("[WindowsAPI] invoking GlobalMemoryStatusEx()");
        return "Total: 32 GB, In Use: 47%";
    }

    public String GetProcessTimes() {
        System.out.println("[WindowsAPI] invoking GetProcessTimes()");
        return "Processes: 254, CPU user time: 42%";
    }

    public String GetDiskFreeSpace() {
        System.out.println("[WindowsAPI] invoking GetDiskFreeSpace()");
        return "C:\\ 1 TB total, 412 GB free, 60% used";
    }
}

/** Adapter — translates WindowsAPI calls into the unified SystemAPI shape. */
class WindowsAdapter implements SystemAPI {
    private WindowsAPI windows;

    public WindowsAdapter(WindowsAPI windows) {
        this.windows = windows;
    }

    public String getSystemData() {
        return "System: " + windows.GetSystemInfo();
    }

    public String getMemoryUsage() {
        return "Memory: " + windows.GlobalMemoryStatusEx();
    }

    public String getProcessUsage() {
        return "Process: " + windows.GetProcessTimes();
    }

    public String getDiskUsage() {
        return "Disk: " + windows.GetDiskFreeSpace();
    }
}

/* ============================== MACOS ============================== */

/** Adaptee — simulated raw macOS Darwin calls. */
class MacOSAPI {
    public String sysctlbynameKernOsproductversion() {
        System.out.println("[MacOSAPI] invoking sysctlbyname(kern.osproductversion)");
        return "macOS 14.4 Sonoma arm64";
    }

    public String hostStatistics64() {
        System.out.println("[MacOSAPI] invoking host_statistics64()");
        return "wired: 4 GB, active: 6 GB, free: 2 GB";
    }

    public String procPidinfo() {
        System.out.println("[MacOSAPI] invoking proc_pidinfo()");
        return "Processes: 489, top consumer: WindowServer 12%";
    }

    public String getfsstat() {
        System.out.println("[MacOSAPI] invoking getfsstat()");
        return "/System/Volumes/Data: 1 TB total, 305 GB free, 70% used";
    }
}

/** Adapter — translates MacOSAPI calls into the unified SystemAPI shape. */
class MacOSAdapter implements SystemAPI {
    private MacOSAPI mac;

    public MacOSAdapter(MacOSAPI mac) {
        this.mac = mac;
    }

    public String getSystemData() {
        return "System: " + mac.sysctlbynameKernOsproductversion();
    }

    public String getMemoryUsage() {
        return "Memory: " + mac.hostStatistics64();
    }

    public String getProcessUsage() {
        return "Process: " + mac.procPidinfo();
    }

    public String getDiskUsage() {
        return "Disk: " + mac.getfsstat();
    }
}
