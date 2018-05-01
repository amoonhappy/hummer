/*
  <p>Open Source Architecture Project -- hummer-common            </p>
  <p>Class Description                                     </p>
  <p>                                                      </p>
  <p>                                                      </p>
  <p>Change History                                        </p>
  <p>Author    Date      Description                       </p>
  <p>                                                      </p>
  <p>                                                      </p>
  Copyright (c) 2004-2007 hummer-common, All rights reserved

  @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a>
 * Date: 2005-12-7
 * @version 1.0
 */
package org.hummer.core.constants;

import org.hummer.core.context.impl.LocaleUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

// import com.col.architect.common.SystemProperties;

/**
 * <br>
 * <br>
 * <ul>
 * <li>
 * <li>
 * </ul>
 * <br>
 *
 * @author jeff
 * @version <br>
 * 1.0.0 2007-6-23
 * @since 1.0.0
 */
public class Constant {
    public final static int MAX_INT = Integer.MAX_VALUE;

    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /**
     * The encryption algorithm key to be used for passwords
     */
    public static final String ENC_ALGORITHM = "algorithm";

    /**
     * A flag to indicate if passwords should be encrypted
     */
    public static final String ENCRYPT_PASSWORD = "encryptPassword";

    /**
     * Session scope attribute that holds the locale set by the user. By setting
     * this key to the same one that Struts uses, we get synchronization in
     * Struts w/o having to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE";

    /**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "admin";

    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme";

    // pagination
    // public static final String PAGINATION_SMALL_SIZE =
    // SystemProperties.getProperty("page.size.small", "5");

    // public static final String PAGINATION_MEDIUM_SIZE =
    // SystemProperties.getProperty("page.size.medium", "10");

    // public static final String PAGINATION_LARGE_SIZE =
    // SystemProperties.getProperty("page.size.large", "15");

    // public static final String PAGINATION_MAX_INDEXD_PAGE_SIZE =
    // SystemProperties.getProperty("page.maxindexed.size", "10");

    // hardcode
    public final static String FLAG_YES = "Y";

    public final static String FLAG_NO = "N";

    public final static String DOCUMENT_SYSTEM = "01";

    public final static String OPERATION_ALL = "ALL";

    // cookie
    // the value is "en" or "zh"
    public final static String COOKIE_LANGUAGE = "language";

    public final static String LANGUAGE_CN = "zh";

    public final static String LANGUAGE_EN = "en";

    // parameter
    public final static String PARAM_PAGE_OFFSET = "pager.offset";

    // request
    // public static final String REQUEST_BIZ_NAME = "request_biz_name";
    // public static final String REQUEST_FORM_ERRORS = "request_form_errors";
    public static final String REQUEST_FORM_SEARCHLIST = "fr_form_searchList";

    public final static String REQUEST_PAGE_OFFSET = PARAM_PAGE_OFFSET;

    public static final String REQUEST_NAV_MODULELIST = "fr_nav_moduleList";

    public static final String REQUEST_NAV_PATHLIST = "fr_nav_pathList";

    // session
    public static final String SESSION_SEARCHCOND = "fr_search_con";

    public static final String SESSION_ERECORD = "fr_erecord";

    public static final String SYSTEM_PATH_SEPARATOR = System.getProperty("file.separator");

    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";

    public static final String DAO_TYPE = "daoType";

    public static final String DAO_TYPE_HIBERNATE = "hibernate";

    public final static String SYSTEM_CONFIG = "system.config";

    public final static String PACKAGE_MODEL = "com.ial.hbs.model";

    public final static String EXCEPTION_TYPE_NORMAL = null;

    public final static String EXCEPTION_TYPE_NEWPAGE = "1";

    public final static String EXCEPTION_TYPE_ALERT = "2";

    // page
    public static final String PAGE_LANGUAGE = "language"; // the value is

    // en,zh,...

    // session
    public static final String SESSION_PRINCIPAL_KEY = "fr_principal_key";

    public static final String SESSION_USER_INFO = "fr_user_info";

    public static final String SESSION_LAST_VISIT_INFO = "fr_last_visit_info";

    public static final String SESSION_OLD_OBJ = "fr_old_obj";

    public static final String SESSION_CHECK_OBJ = "fr_check_obj";

    // request
    public static final String REQUEST_FORBID_LST = "fr_forbid_lst";

    public static final String REQUEST_RUNTIME = "fr_runtime";

    public static final String REQUEST_BIZ_NAME = "fr_biz_name";

    public static final String REQUEST_FORM_ERRORS = "fr_form_errors";

    public static final String REQUEST_STRUTS_EXCEPTION = "fr_struts_exception";

    public static final String REQUEST_EXCEPTION_TYPE = "fr_ex_type";

    public static final String REQUEST_FORM_NAME = "fr_form_name";

    // FIXME: the value of REQUEST_ACTION_PATH should be "fr_action_path",
    // but I have to change many jsp that had been writen before.
    public final static String REQUEST_ACTION_PATH = "fr_action";

    // full text search
    public static final String ALL_IN_KEYWORD = "1"; // ����ȫ��

    public static final String COMPLETE_MATCH_KEYWORD = "2"; // ��ȫƥ��

    public static final String FULLTEXT_KEYWORD_SEPERATOR1 = ",";

    public static final String FULLTEXT_KEYWORD_SEPERATOR2 = " ";

    /**
     * Default Date Format
     */
    // public static final String DATE_FORMAT_DEFAULT =
    // SystemProperties.getProperty("date.simpleFormat", "yyyy-MM-dd");
    public static final String DATE_FORMAT_DEFAULT = "yyyy/MM/dd";

    /**
     * Full Date Format
     */
    public static final String DATE_FORMAT_FULL = System.getProperty("data.format", "yyyy-MM-dd HH:mm:ss");

    /**
     * Default Date Formatter
     */
    public static final DateFormat DATE_FORMATTER_DEFAULT = new SimpleDateFormat(DATE_FORMAT_DEFAULT);

    /**
     * Full Date Formatter
     */
    public static final DateFormat DATE_FORMATTER_FULL = new SimpleDateFormat(DATE_FORMAT_FULL);

    /**
     * Default Locale
     */
    public static final Locale LOCALE_DEFAULT = LocaleUtil.getLocale();

}
