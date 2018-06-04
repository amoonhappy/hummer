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
package org.hummer.core.util;

import org.apache.commons.lang3.StringUtils;
import org.hummer.core.constants.Constant;
import org.hummer.core.constants.ExceptionConstant;
import org.hummer.core.exception.BusinessException;
import org.slf4j.Logger;

import java.sql.*;
import java.util.List;

/**
 * @author jeff.zhou
 */
public class JDBCUtil {
    protected transient static final Logger log = Log4jUtils.getLogger(JDBCUtil.class);

    /**
     * this mothods replace the parameters in the prepared statement
     *
     * @param ps
     * @param parameters
     * @return
     * @throws SQLException
     */
    public static PreparedStatement proccessParam(PreparedStatement ps, List<Object> parameters) throws SQLException {
        StringBuffer loginfo = new StringBuffer("sql parameters[");
        loginfo.append(parameters.size()).append("]={");

        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                if ((parameters.get(i) == null) || "".equals(parameters.get(i))) {
                    ps.setString(i + 1, null);
                    loginfo.append(",").append(i).append("=").append(parameters.get(i));
                } else if (parameters.get(i) instanceof java.util.Date) {
                    Date date = (Date) parameters.get(i);
                    ps.setTimestamp(i + 1, new Timestamp(((java.util.Date) parameters.get(i)).getTime()));
                    loginfo.append(",").append(i).append("=").append(DateUtil.date2String(date));
                } else {
                    ps.setObject(i + 1, parameters.get(i));
                    loginfo.append(",").append(i).append("=").append(parameters.get(i));
                }
            }
        }

        loginfo.append("}");
        log.info(loginfo.toString());

        return ps;
    }

    /**
     * this method replace the \n in the sql which is getted from *.hbm.xml
     *
     * @param sql
     * @return
     */
    public static String trimSQL(String sql) {
        if (StringUtils.isEmpty(sql)) {
            return "";
        }

        return StringUtils.replace(sql, "\n", " ");
    }

    public static int execUpdate(Connection con, String execSQL, List<Object> queryParam) throws SQLException,
            BusinessException {
        PreparedStatement preparedStatement = null;

        if ((con == null) || con.isClosed()) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.DB_ACCESS_ERROR);
        }

        if (StringUtils.isEmpty(execSQL)) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
        }

        preparedStatement = proccessParam(con.prepareStatement(execSQL), queryParam);

        return preparedStatement.executeUpdate();
    }

    public static ResultSet execQuery(Connection con, String execSQL, List<Object> queryParam) throws SQLException,
            BusinessException {
        PreparedStatement preparedStatement = null;

        if ((con == null) || con.isClosed()) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.DB_ACCESS_ERROR);
        }

        if (StringUtils.isEmpty(execSQL)) {
            throw ExceptionUtil.getBusinessException(ExceptionConstant.BIZ_SAVE_CONDITION_ERROR);
        }

        preparedStatement = proccessParam(con.prepareStatement(execSQL), queryParam);

        return preparedStatement.executeQuery();
    }

    public static String processMultiKeywords(String searchType, String searchText) {
        String ret = searchText;

        if (StringUtils.equals(searchType, Constant.ALL_IN_KEYWORD)) {
            ret = parseKeyword(ret, "AND");
        } else if (StringUtils.equals(searchType, Constant.COMPLETE_MATCH_KEYWORD)) {
            ret = StringUtils.replace(ret, ",", "chr(44)");
            ret = StringUtils.replace(ret, " ", "chr(32)");
        }

        return ret;
    }

    // public static String getContainsKeywords(String keyword, String linker) {
    // StringBuffer retValue = newweb StringBuffer();
    //
    // String[] keywords = newweb String[] { parseKeyword(keyword, linker) };
    //
    // for (int i = 0; i < keywords.length; i++) {
    // if (i != 0) {
    // retValue.append(" ").append(linker).append(" ");
    // }
    //
    // retValue.append("{").append(keywords[i]).append("}");
    // }
    //
    // log.info("[at getContainsKeywords()]");
    // log.info("orginal keyword=" + keyword);
    // log.info("processed keyword=" + retValue.toString());
    //
    // return retValue.toString();
    // }

    /**
     * Reconstruct the keyword with the special linker through replacing the old
     * seperator
     *
     * @param keyword Constant.ADVSEARCH_KEYWORD_SEPERATOR1 and
     *                Constant.ADVSEARCH_KEYWORD_SEPERATOR2
     * @return The keyword use the special linker
     */
    public static String parseKeyword(String keyword, String linker) {
        if (keyword == null) {
            return keyword;
        }

        StringBuffer sb = new StringBuffer();
        String[] strs = StringUtil.joinArray(keyword, Constant.FULLTEXT_KEYWORD_SEPERATOR1);

        if ((strs != null) && (strs.length > 0)) {
            for (int i = 0; i < strs.length; i++) {
                String[] strBlank = StringUtil.joinArray(strs[i], Constant.FULLTEXT_KEYWORD_SEPERATOR2);

                if ((strBlank != null) && (strBlank.length > 0)) {
                    for (int j = 0; j < strBlank.length; j++) {
                        sb.append("{");
                        sb.append(strBlank[j]);
                        sb.append("}");
                        sb.append(" " + linker + " ");
                    }
                }
            }
        }

        // remove the last AND
        if (sb.length() > 0) {
            int iIndex = sb.lastIndexOf(" " + linker + " ");

            if (iIndex >= 0) {
                sb.replace(iIndex, iIndex + linker.length() + 2, "");
            }
        }

        log.info("[at parseKeyword()]");
        log.info("Original keyword=[{}]", keyword);
        log.info("Processed keyword=[{}]", sb.toString());

        return sb.toString();
    }
}
