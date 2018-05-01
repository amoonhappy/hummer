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
package org.hummer.core.constants;

/**
 * @author jeff.zhou
 */
public class SearchConstant {
    /*
     * Match the pattern anywhere in the string For Oracle Example: from user
     * where user.name like '%<name>%'
     */
    public static final int MATCH_MODEL_ANYWHERE = 1;

    /*
     * Match the entire string to the pattern For Oracle Example: from user
     * where user.name like '<name>'
     */
    public static final int MATCH_MODEL_EXACT = 2;

    /*
     * Match the start of the string to the pattern For Oracle Example: from
     * user where user.name like '%<name>'
     */
    public static final int MATCH_MODEL_START = 3;

    /*
     * Match the end of the string to the pattern For Oracle Example: from user
     * where user.name like '<name>%'
     */
    public static final int MATCH_MODEL_END = 4;

    // for criteria page handler
    public static final int QUERY_TYPE_CRITERIA = 1;

    // for query page handler
    public static final int QUERY_TYPE_SQL = 2;

    // for query page handler
    public static final int QUERY_TYPE_HQL = 3;

    // for sql mapping query page handler
    public static final int QUERY_TYPE_SQLNAME = 4;

    // for '>','>=','<','<='
    public static final int LARGE = 1;

    public static final int LARGE_EQUAL = 2;

    public static final int LESS = 3;

    public static final int LESS_EQUAL = 4;

    public static final String ORDER_BY_DESC = "desc";

    public static final String ORDER_BY_ASC = "asc";
}
