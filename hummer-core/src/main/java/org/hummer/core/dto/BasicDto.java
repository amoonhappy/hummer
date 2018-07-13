package org.hummer.core.dto;

import java.io.Serializable;
import java.util.List;

public class BasicDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer page;

    private Integer limit;

    private String sidx;

    private String order;

    private String queryTitle;

    private List<String> roleOrgIds;

    private String dtoEmployeeId;

    public String getDtoEmployeeId() {
        return dtoEmployeeId;
    }

    public void setDtoEmployeeId(String dtoEmployeeId) {
        this.dtoEmployeeId = dtoEmployeeId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getQueryTitle() {
        return queryTitle;
    }

    public void setQueryTitle(String queryTitle) {
        this.queryTitle = queryTitle;
    }

    public List<String> getRoleOrgIds() {
        return roleOrgIds;
    }

    public void setRoleOrgIds(List<String> roleOrgIds) {
        this.roleOrgIds = roleOrgIds;
    }

}
