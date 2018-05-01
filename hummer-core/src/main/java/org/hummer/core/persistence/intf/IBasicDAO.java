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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.persistence.intf;

import org.hummer.core.model.intf.IBlobModel;
import org.hummer.core.model.intf.IClobModel;
import org.hummer.core.model.intf.IModel;
import org.hummer.core.pagination.Pager;
import org.hummer.core.persistence.impl.SearchCondition;

import javax.sql.RowSet;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author jeff.zhou
 */
public interface IBasicDAO {
    /**
     * Inject the pager handler If you want to use the pager handler your own
     * dao interface must implement the method
     *
     * @param pagerHandler
     */
    public void setPagerHandler(IPagerHandler pagerHandler);

    /**
     * this method is suitable for simple object query that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method
     *
     * @param o
     * @return a populated object (or null if id doesn't exist)
     */
    public IModel getObject(IModel o) throws Exception;

    /**
     * this method is suitable for simple object query that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method is locked is true dao will select for update
     *
     * @param o
     * @param locked
     * @return a populated object (or null if id doesn't exist)
     */
    public IModel getObject(IModel o, boolean locked) throws Exception;

    /**
     * this method is suitable for simple object update and insert that return
     * one instance,pay attention that the Model MUST have the getId or
     * getComp_id method
     *
     * @param o
     * @param isInsert if false when no id throw exception if true when id exist
     *                 throw exception
     * @return
     * @throws Exception
     */
    public IModel saveObject(IModel o, boolean isInsert) throws Exception;

    /**
     * Generic method to save an object - handles both update and insert.
     *
     * @param o the object to save
     */
    public IModel saveObject(IModel o) throws Exception;

    /**
     * Generic method to delete an object
     *
     * @param o the object to delete
     */
    public IModel deleteObject(IModel o) throws Exception;

    /**
     * Generic method to delete some objects, the where clause is generated
     * according to the inputted searchCondition
     *
     * @param searchCondition
     */
    public void deleteObject(SearchCondition searchCondition) throws Exception;

    /**
     * Generic method remove some objects
     *
     * @param c
     */
    public void removeObject(Collection<IModel> c) throws Exception;

    /**
     * excute hibernate ql please use searchBySQLMapping
     *
     * @param searchCondition
     * @return
     */
    public Pager searchByHQL(SearchCondition searchCondition) throws Exception;

    /**
     * excute native sql please use searchBySQLMapping
     *
     * @param searchCondition
     * @return
     * @deprecated
     */
    public Pager getObjectBySQL(SearchCondition searchCondition) throws Exception;

    /**
     * Generic method save some objects
     *
     * @param c
     */
    public void saveObject(Collection<IModel> c) throws Exception;

    /**
     * Generic method which search by the hibernate criteria api but not support
     * composite/single pk search
     *
     * @param o
     */
    public Pager searchByCriteria(SearchCondition o) throws Exception;

    /**
     * search objects by sql mapping name
     *
     * @param o
     */
    public Pager searchBySQLMapping(SearchCondition o) throws Exception;

    /**
     * Execute the sql via JDBC and the sql is store in the hibernate mapping
     * files
     *
     * @param searchCondition
     * @throws Exception
     */
    public int execSQLMapping(SearchCondition searchCondition) throws Exception;

    /**
     * Execute the update or delete sql via JDBC and return the updated or
     * deleted rows' number
     *
     * @param searchCondition
     * @throws Exception
     */
    public int execUpdateSQL(SearchCondition searchCondition) throws Exception;

    /**
     * Execute the SQL by setQuerySQL() and addQueryParam() and then return the
     * cached row set from Result set
     *
     * @param searchCondition
     * @return
     * @throws Exception
     */
    public RowSet execSQL(SearchCondition searchCondition) throws Exception;

    /**
     * this method is suitable for simple object update that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method <p/> if the id or comp_id is null, a BIZ_NO_IDENTIFIER
     * BusinessException will occur.
     *
     * @param o
     * @return
     * @throws Exception
     */
    public IModel updateObject(IModel o) throws Exception;

    /**
     * this method is suitable for a collection of simple objects update pay
     * attention that the Model MUST have the getId or getComp_id method <p/> if
     * the id or comp_id is null, a BIZ_NO_IDENTIFIER BusinessException will
     * occur.
     *
     * @param c
     * @throws Exception
     */
    public void updateObject(Collection<IModel> c) throws Exception;

    /**
     * this method is suitable for simple object insert that return one
     * instance,pay attention that the Model MUST have the getId or getComp_id
     * method If the Model is already in the database, will throw a
     * BIZ_DUPLICATE_ERROR exception
     *
     * @param o
     * @return
     * @throws Exception
     */
    public IModel addObject(IModel o) throws Exception;

    /**
     * this method is suitable for a collection simple objects
     * <p>
     * insert that return one instance,pay attention that the
     * <p>
     * Model MUST have the getId or getComp_id method
     * <p>
     * If the Model is already in the database, will throw a
     * <p>
     * BIZ_DUPLICATE_ERROR exception
     * <p>
     *
     * @param c
     * @throws Exception
     */
    public void addObject(Collection<IModel> c) throws Exception;

    /**
     * this method is suitable for increase one column's value of sequence table
     *
     * @param o         the model
     * @param getMethod the get method name of the increase column
     * @param setMethod the set method name of the increase column
     * @return
     * @throws Exception
     */
    public IModel increaseSeq(IModel o, String getMethod, String setMethod) throws Exception;

    /**
     * update the BlobModel set the input bytes to the Blob column and overwirte
     * the original Blob
     *
     * @param bmodel
     * @param input
     * @return
     * @throws Exception
     */
    public Serializable updateBlob(IBlobModel bmodel, byte[] input) throws Exception;

    /**
     * update the ClobModel set the input String to the Clob column and
     * overwirte the original Clob
     *
     * @param cmodel
     * @param input
     * @return
     * @throws Exception
     */
    public Serializable updateClob(IClobModel cmodel, String input) throws Exception;

    /**
     * this method is used to get the next sequence number via JDBC only
     * suiteable for Oracle
     *
     * @param sequenceName
     * @return
     */
    public Long getSequenceNumber(String sequenceName) throws Exception;
}
