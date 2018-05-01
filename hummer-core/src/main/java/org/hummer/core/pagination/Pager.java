/**
 * <p>Open Source Architecture Project -- Hummer            </p>
 * <p>Class Description                                     </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author    Date      Description                       </p>
 * <p>                                                      </p>
 * <p>                                                      </p>
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-6
 * @version 1.0
 */
package org.hummer.core.pagination;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author jeff.zhou
 */
public class Pager implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3257565113857816117L;

    // total row count of search result
    protected long totalRowCount;

    // start row num
    protected int startRowNo;

    // return result
    protected Collection<Object> result;

    // page size
    protected int pageSize;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Collection<Object> getResult() {
        return result;
    }

    public void setResult(Collection<Object> result) {
        this.result = result;
    }

    public boolean hasNextPage() {
        return (startRowNo + getRowCount()) <= totalRowCount;
    }

    public long getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(long totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public int getStartRowNo() {
        return startRowNo;
    }

    public void setStartRowNo(int startRowNo) {
        this.startRowNo = startRowNo;
    }

    public int getRowCount() {
        return (result == null) ? 0 : result.size();
    }
}