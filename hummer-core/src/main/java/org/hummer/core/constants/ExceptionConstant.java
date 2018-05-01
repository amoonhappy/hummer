/**
 * <p>Open Source Architecture Project -- Hummer            </p>
 * <p>Class Description                                     </p>
 * <p>Define all the exception error code constants</p>
 * <p>                                                      </p>
 * <p>Change History                                        </p>
 * <p>Author      Date          Description                 </p>
 * <p>Jeff.Zhou   2005-02-28    Add the database error code for Spring framework</p>
 * <p>                          all number is negative</p>
 *
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-11-21 23:08:49
 * @version 1.0
 */
package org.hummer.core.constants;

public class ExceptionConstant {
    public static final int SUCCESS = 0;

    public static final int SYS_RUNTIME_ERROR = -1;

    // database exception start with 10000
    // database error including sqlexception hibernate exception etc
    public static final int DB_HIBERNATE_ERROR = -100000;

    public static final int DB_ACCESS_ERROR = -100001;

    public static final int DB_SQL_ERROR = -100002;

    // sql exception start with 11000
    public static final int DB_SQL_BAD_SQL_GRAMMAR = -110000;

    public static final int DB_SQL_DATA_INTEGRITY_VIOLATION = -110001;

    public static final int DB_SQL_UNCATEGORIZED_SQL_ERROR = -110002;

    // hibernate exception start with 12000
    public static final int DB_HB_STALE_OBJECT_STATE_ERROR = -120000;

    // spring dao exception start with 13000
    public static final int DB_SP_DATA_ACCESS_ERROR = -130000;

    public static final int DB_SP_CLEANUP_FAILURE_AFTER_DATA_ACCESS = -130001;

    public static final int DB_SP_OBJECT_OPTIMISTIC_LOCKING_FAILURE = -130002;

    public static final int DB_SP_CANNOT_ACQUIRE_LOCK = -130003;

    public static final int DB_SP_CANNOT_SERIALIZE_TRANSACTION = -130004;

    public static final int DB_SP_OPTIMISTIC_LOCKING_FAILURE = -130005;

    public static final int DB_SP_CONCURRENCY_FAILURE = -130006;

    public static final int DB_SP_CANNOT_GET_JDBC_CONNECTION = -130007;

    public static final int DB_SP_DATA_ACCESS_RESOURCE_FAILURE = -130008;

    public static final int DB_SP_DATA_INTEGRITY_VIOLATION = -130009;

    public static final int DB_SP_HIBERNATE_OBJECT_RETRIEVAL_FAILURE = -130010;

    public static final int DB_SP_OBJECT_RETRIEVAL_FAILURE = -130011;

    public static final int DB_SP_LOB_RETRIEVAL_FAILURE = -130012;

    public static final int DB_SP_DATA_RETRIEVAL_FAILURE = -130013;

    public static final int DB_SP_DEADLOCK_LOSE = -130014;

    public static final int DB_SP_INCORRECT_RESULT_SIZE = -130015;

    public static final int DB_SP_INVALID_DATA_ACCESS_API_USAGE = -130016;

    public static final int DB_SP_JDBC_UPDATE_AFFECTED_INCORRECT_NUMBER_OF_ROWS = -130017;

    public static final int DB_SP_BAD_SQL_GRAMMAR = -130018;

    public static final int DB_SP_HIBERNATE_QUERY_EXCEPTION = -130019;

    public static final int DB_SP_INCORRECT_UPDATE_SEMANTICS = -130020;

    public static final int DB_SP_MISMATCH_DATA_ACCESS = -130021;

    public static final int DB_SP_HIBERNATE_JDBC_EXCEPTION = -130022;

    public static final int DB_SP_HIBERNATE_SYSTEM_EXCEPTION = -130023;

    public static final int DB_SP_SQL_WARNING = -130024;

    public static final int DB_SP_UNCATEGORIZED_SQLEXCEPTION = -130025;

    public static final int DB_SP_UNCATEGORIZED_DATA_ACCESS_EXCEPTION = -130026;

    // other database exception start with 14000
    public static final int DB_OTHER_DATA_ACCESS_ERROR = -140000;

    /**
     * Business Logic exception start with 300000
     */

    // seach condition not integrated
    // 300000-309999Common Business Exception
    public static final int BIZ_NO_IDENTIFIER = -300000;

    public static final int BIZ_DUPLICATE_ERROR = -300001;

    public static final int BIZ_SEARCH_CONDITION_ERROR = -300002;

    public static final int BIZ_DELETE_CONDITION_ERROR = -300003;

    public static final int BIZ_SAVE_CONDITION_ERROR = -300004;

    public static final int BIZ_GET_CONDITION_ERROR = -300005;

    public static final int BIZ_SAVEORUPDATE_CONDITION_ERROR = -300006;

    public static final int BIZ_SAVE_CONDITION_FORMAT_ERROR = -300007;

    public static final int BIZ_RECORD_ALREADY_EXIST = -300008;

    public static final int BIZ_RECORD_USED_BY_OTHERS = -300009;

    public static final int BIZ_RECORD_NOT_EXIST = -300010;

    // 310000-319999 Module1 Exception
    public static final int BIZ_MODULE1_ERROR = -310000;

    public static final int BIZ_UA_NOT_FOUND_USER_ACCOUNT = -310102;

    public static final int BIZ_UA_AMEND__OLD_PASSWORD_NOT_MATCH = -310103;

    public static final int BIZ_UA_AMEND__NEW_PASSWORD_NOT_MATCH = -310104;

    public static final int BIZ_UA_AMEND_PASSWORD_IS_SAME_WITH_BEFORE = -310105;

    public static final int BIZ_UA_GROUP_ASSIGN_USER_NO_SELECT = -310106;

    public static final int BIZ_UA_AMEND_NEW_PASSWORD_CANNOT_BE_EMPTY = -310107;

    // UaUser Module end

    // 320000-329999 Module2 Exception
    public static final int BIZ_MODULE2_ERROR = -320000;

    // 330000-339999 Module3 Exception
    public static final int BIZ_MODULE3_ERROR = -330000;

    // 340000-349999 Module4 Exception
    public static final int BIZ_MODULE4_ERROR = -340000;

    // 350000-359999 Module5 Exception
    // 360000-369999 Module6 Exception
    // 370000-379999 Module7 Exception
    // 380000-389999 Module8 Exception
    // 390000-399999 Module9 Exception
    // 400000-409999 Module10 Exception
    // if the display of error information is a newweb page , its error code should
    // be larger than NEW_PAGE_ERROR_CODE
    public static final int NEW_PAGE_ERROR_CODE = 1000000;
}
