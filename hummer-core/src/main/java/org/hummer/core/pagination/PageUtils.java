package org.hummer.core.pagination;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hummer.core.dto.BasicDto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;

    static final int DEFAULT_CURR_PAGE = 1;
    static final int DEFAULT_PAGE_SIZE = 10;

    //总记录数
    private int totalCount;
    //每页记录数
    private int pageSize;
    //总页数
    private int totalPage;
    //当前页数
    private int currPage;
    //列表数据
    private List<?> list;

    private Map<String, Object> param;//参数


    public PageUtils() {
        super();
    }

    public PageUtils(int totalCount, int pageSize, int totalPage, int currPage, List<?> list,
                     Map<String, Object> param) {
        super();
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.currPage = currPage;
        this.list = list;
        this.param = param;
    }

    /**
     * 分页
     *
     * @param list       列表数据
     * @param totalCount 总记录数
     * @param pageSize   每页记录数
     * @param currPage   当前页数
     */
    public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public static void page(BasicDto baseDto) {
        if (baseDto != null) {
            int currPage = baseDto.getPage() <= 0 ? DEFAULT_CURR_PAGE : baseDto.getPage();
            int pageSize = baseDto.getLimit() <= 0 ? DEFAULT_PAGE_SIZE : baseDto.getLimit();
            PageHelper.startPage(currPage, pageSize, true);
        }
    }

    public PageUtils(List list) {
        this(new PageInfo(list));
    }

    public PageUtils(PageInfo pageInfo) {
        this.totalCount = (int) pageInfo.getTotal();
        this.pageSize = pageInfo.getPageSize();
        this.currPage = pageInfo.getPageNum();
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
        this.list = pageInfo.getList();
    }
}