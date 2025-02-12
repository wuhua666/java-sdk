package org.fisco.bcos.sdk.v3.model;

import java.util.HashMap;
import java.util.Map;

public enum EnumNodeVersion {
    UNKNOWN(-1),
    BCOS_3_0_0_RC4(4),
    BCOS_3_0_0(0x03000000),
    BCOS_3_1_0(0x03010000),
    BCOS_3_2_0(0x03020000);

    private final Integer version;
    private static final Map<Integer, EnumNodeVersion> versionLookupMap = new HashMap<>();

    static {
        versionLookupMap.put(4, BCOS_3_0_0_RC4);
        versionLookupMap.put(0x03000000, BCOS_3_0_0);
        versionLookupMap.put(0x03010000, BCOS_3_1_0);
        versionLookupMap.put(0x03020000, BCOS_3_2_0);
    }

    EnumNodeVersion(Integer version) {
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }

    public String getVersionString() {
        switch (this) {
            case BCOS_3_0_0_RC4:
                return "3.0.0-rc4";
            case BCOS_3_0_0:
                return "3.0.0";
            case BCOS_3_1_0:
                return "3.1.0";
            case BCOS_3_2_0:
                return "3.2.0";
            case UNKNOWN:
            default:
                return "0.0.0";
        }
    }

    public Version toVersionObj() {
        return getClassVersion(getVersionString());
    }

    public static EnumNodeVersion valueOf(int version) {
        EnumNodeVersion enumNodeVersion = versionLookupMap.get(version);
        if (enumNodeVersion == null) {
            return UNKNOWN;
        }
        return enumNodeVersion;
    }

    public static EnumNodeVersion.Version convertToVersion(int version) {
        return valueOf(version).toVersionObj();
    }

    // the object of node version
    public static class Version implements Comparable<Version> {
        private int major;
        private int minor;
        private int patch;
        private String ext = "";

        @Override
        public String toString() {
            return "Version [major="
                    + major
                    + ", minor="
                    + minor
                    + ", patch="
                    + patch
                    + ", ext="
                    + ext
                    + "]";
        }

        public String toVersionString() {
            String str = this.getMajor() + "." + this.getMinor() + "." + this.getPatch();
            if (ext != null && !ext.isEmpty()) {
                str += "-" + ext;
            }
            return str;
        }

        public int getMajor() {
            return major;
        }

        public void setMajor(int major) {
            this.major = major;
        }

        public int getMinor() {
            return minor;
        }

        public void setMinor(int minor) {
            this.minor = minor;
        }

        public int getPatch() {
            return patch;
        }

        public void setPatch(int patch) {
            this.patch = patch;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        @Override
        public int compareTo(Version v) {
            int thisCompactVersion = this.getMajor() * 100 + this.getMinor() * 10 + this.getPatch();
            int vCompactVersion = v.getMajor() * 100 + v.getMinor() * 10 + v.getPatch();
            if (thisCompactVersion > vCompactVersion) {
                return 1;
            } else if (thisCompactVersion < vCompactVersion) {
                return -1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;

            if (this.getClass() != obj.getClass()) return false;
            return this.compareTo((Version) obj) == 0;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    public static Version getClassVersion(String version) {
        // node version str format : "a.b.c" or "a.b.c-rcx"
        String[] s0 = version.trim().split("-");

        Version v = new Version();
        if (s0.length > 1) {
            v.setExt(s0[1]);
        }

        String[] s1 = s0[0].split("\\.");
        if (s1.length >= 3) {
            v.setMajor(Integer.parseInt(s1[0].trim()));
            v.setMinor(Integer.parseInt(s1[1].trim()));
            v.setPatch(Integer.parseInt(s1[2].trim()));
        } else { // invalid format
            throw new IllegalStateException(" invalid node version format, version: " + version);
        }
        return v;
    }
}
