package org.hummer.core.config.impl;

import java.util.HashSet;
import java.util.Set;

public class SupportedAppInfos {
    static SupportedAppInfos supportedAppInfos = new SupportedAppInfos();
    static Set<SupportedAppInfo> appInfos = new HashSet<>();

    public static void appReg(String appName, String version) {
        SupportedAppInfo appInfo = supportedAppInfos.new SupportedAppInfo(appName, version);
        appInfos.add(appInfo);
    }

    public static Set<SupportedAppInfo> getRegAppInfos() {
        return appInfos;
    }

    public class SupportedAppInfo {
        private String appName;
        private String version;

        public SupportedAppInfo(String appName, String version) {
            super();
            this.appName = appName;
            this.version = version;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((appName == null) ? 0 : appName.hashCode());
            result = prime * result + ((version == null) ? 0 : version.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SupportedAppInfo other = (SupportedAppInfo) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (appName == null) {
                if (other.appName != null)
                    return false;
            } else if (!appName.equals(other.appName))
                return false;
            if (version == null) {
                if (other.version != null)
                    return false;
            } else if (!version.equals(other.version))
                return false;
            return true;
        }

        /**
         * Returns a string representation of the object. In general, the
         * {@code toString} method returns a string that
         * "textually represents" this object. The result should
         * be a concise but informative representation that is easy for a
         * person to read.
         * It is recommended that all subclasses override this method.
         * <p>
         * The {@code toString} method for class {@code Object}
         * returns a string consisting of the name of the class of which the
         * object is an instance, the at-sign character `{@code @}', and
         * the unsigned hexadecimal representation of the hash code of the
         * object. In other words, this method returns a string equal to the
         * value of:
         * <blockquote>
         * <pre>
         * getClass().getName() + '@' + Integer.toHexString(hashCode())
         * </pre></blockquote>
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return this.appName + "[" + this.version + "]";
        }

        private SupportedAppInfos getOuterType() {
            return SupportedAppInfos.this;
        }
    }
}
