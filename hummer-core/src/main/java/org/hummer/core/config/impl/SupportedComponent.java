package org.hummer.core.config.impl;

import java.util.HashSet;
import java.util.Set;

public class SupportedComponent {
    static SupportedComponent supportedComponent = new SupportedComponent();
    static Set<SupportedComponentInfo> supportedComponentInfos = new HashSet<>();

    public static void regComponent(String compName, String compVersion) {
        SupportedComponentInfo supportedComponentInfo = supportedComponent.new SupportedComponentInfo(compName, compVersion);
        supportedComponentInfos.add(supportedComponentInfo);
    }

    public static Set<SupportedComponentInfo> getSupportedComponentInfos() {
        return supportedComponentInfos;
    }

    public class SupportedComponentInfo {
        private String compName;
        private String compVersion;

        public SupportedComponentInfo(String compName, String compVersion) {
            super();
            this.compName = compName;
            this.compVersion = compVersion;
        }

        public String getCompName() {
            return compName;
        }

        public void setCompName(String compName) {
            this.compName = compName;
        }

        public String getCompVersion() {
            return compVersion;
        }

        public void setCompVersion(String compVersion) {
            this.compVersion = compVersion;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((compName == null) ? 0 : compName.hashCode());
            result = prime * result + ((compVersion == null) ? 0 : compVersion.hashCode());
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
            SupportedComponentInfo other = (SupportedComponentInfo) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (compName == null) {
                if (other.compName != null)
                    return false;
            } else if (!compName.equals(other.compName))
                return false;
            if (compVersion == null) {
                if (other.compVersion != null)
                    return false;
            } else if (!compVersion.equals(other.compVersion))
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
            return this.compName + "[" + this.compVersion + "]";
        }

        private SupportedComponent getOuterType() {
            return SupportedComponent.this;
        }
    }
}
