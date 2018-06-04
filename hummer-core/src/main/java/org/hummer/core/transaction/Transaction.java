package org.hummer.core.transaction;

public class Transaction {
    private Isolation isolationlevel = Isolation.DEFAULT;
    private int timeout = -1;
    private boolean readonly = false;
    private Propagation propagation = Propagation.REQUIRED;


    public Propagation getPropagation() {
        return propagation;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }

    public boolean needNewTransaction() {
        boolean ret = false;
        if (Propagation.REQUIRES_NEW.value() == this.propagation.value()) {
            ret = true;
        }
        return ret;
    }

    public int getTimeout() {

        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public int getIsolationLevel() {
        return isolationlevel.value();
    }

    public void setIsolationlevel(Isolation isolationlevel) {
        this.isolationlevel = isolationlevel;
    }
}
