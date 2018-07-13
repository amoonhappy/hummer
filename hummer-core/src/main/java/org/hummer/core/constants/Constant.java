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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    public static final String BUNDLE_KEY = "ApplicationResources" ;

    /**
     * The encryption algorithm key to be used for passwords
     */
    public static final String ENC_ALGORITHM = "algorithm" ;

    /**
     * A flag to indicate if passwords should be encrypted
     */
    public static final String ENCRYPT_PASSWORD = "encryptPassword" ;

    /**
     * Session scope attribute that holds the locale set by the user. By setting
     * this key to the same one that Struts uses, we get synchronization in
     * Struts w/o having to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE" ;

    /**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "admin" ;

    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme" ;

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
    public final static String FLAG_YES = "Y" ;

    public final static String FLAG_NO = "N" ;

    public final static String DOCUMENT_SYSTEM = "01" ;

    public final static String OPERATION_ALL = "ALL" ;

    // cookie
    // the value is "en" or "zh"
    public final static String COOKIE_LANGUAGE = "language" ;

    public final static String LANGUAGE_CN = "zh" ;

    public final static String LANGUAGE_EN = "en" ;

    // parameter
    public final static String PARAM_PAGE_OFFSET = "pager.offset" ;

    // request
    // public static final String REQUEST_BIZ_NAME = "request_biz_name";
    // public static final String REQUEST_FORM_ERRORS = "request_form_errors";
    public static final String REQUEST_FORM_SEARCHLIST = "fr_form_searchList" ;

    public final static String REQUEST_PAGE_OFFSET = PARAM_PAGE_OFFSET;

    public static final String REQUEST_NAV_MODULELIST = "fr_nav_moduleList" ;

    public static final String REQUEST_NAV_PATHLIST = "fr_nav_pathList" ;

    // session
    public static final String SESSION_SEARCHCOND = "fr_search_con" ;

    public static final String SESSION_ERECORD = "fr_erecord" ;

    public static final String SYSTEM_PATH_SEPARATOR = System.getProperty("file.separator" );

    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig" ;

    public static final String DAO_TYPE = "daoType" ;

    public static final String DAO_TYPE_HIBERNATE = "hibernate" ;

    public final static String SYSTEM_CONFIG = "system.config" ;

    public final static String PACKAGE_MODEL = "com.ial.hbs.model" ;

    public final static String EXCEPTION_TYPE_NORMAL = null;

    public final static String EXCEPTION_TYPE_NEWPAGE = "1" ;

    public final static String EXCEPTION_TYPE_ALERT = "2" ;

    // page
    public static final String PAGE_LANGUAGE = "language" ; // the value is

    // en,zh,...

    // session
    public static final String SESSION_PRINCIPAL_KEY = "fr_principal_key" ;

    public static final String SESSION_USER_INFO = "fr_user_info" ;

    public static final String SESSION_LAST_VISIT_INFO = "fr_last_visit_info" ;

    public static final String SESSION_OLD_OBJ = "fr_old_obj" ;

    public static final String SESSION_CHECK_OBJ = "fr_check_obj" ;

    // request
    public static final String REQUEST_FORBID_LST = "fr_forbid_lst" ;

    public static final String REQUEST_RUNTIME = "fr_runtime" ;

    public static final String REQUEST_BIZ_NAME = "fr_biz_name" ;

    public static final String REQUEST_FORM_ERRORS = "fr_form_errors" ;

    public static final String REQUEST_STRUTS_EXCEPTION = "fr_struts_exception" ;

    public static final String REQUEST_EXCEPTION_TYPE = "fr_ex_type" ;

    public static final String REQUEST_FORM_NAME = "fr_form_name" ;

    // FIXME: the value of REQUEST_ACTION_PATH should be "fr_action_path",
    // but I have to change many jsp that had been writen before.
    public final static String REQUEST_ACTION_PATH = "fr_action" ;

    // full text search
    public static final String ALL_IN_KEYWORD = "1" ; // ����ȫ��

    public static final String COMPLETE_MATCH_KEYWORD = "2" ; // ��ȫƥ��

    public static final String FULLTEXT_KEYWORD_SEPERATOR1 = "," ;

    public static final String FULLTEXT_KEYWORD_SEPERATOR2 = " " ;

    /**
     * Default Date Format
     */
    // public static final String DATE_FORMAT_DEFAULT =
    // SystemProperties.getProperty("date.simpleFormat", "yyyy-MM-dd");
    public static final String DATE_FORMAT_DEFAULT = "yyyy/MM/dd" ;

    /**
     * Full Date Format
     */
    public static final String DATE_FORMAT_FULL = System.getProperty("data.format", "yyyy-MM-dd HH:mm:ss" );

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

    private static final Map<String, String> TAB_ALIAS_NAME = new HashMap<>();

    public static String getTabAliasName(String key) {
        return TAB_ALIAS_NAME.get(key);
    }

    static {

        // 用户模块
        TAB_ALIAS_NAME.put("KnEmployee", "user01" );
        TAB_ALIAS_NAME.put("KnEmployeePosition", "user06" );
        TAB_ALIAS_NAME.put("KnResource", "res01" );
        TAB_ALIAS_NAME.put("KnOrganization", "org01" );

        TAB_ALIAS_NAME.put("XsBsFcontact", "user11" );    //常用联系人
        TAB_ALIAS_NAME.put("KnEmpHeadLastupdate", "user12" );    //常用联系人
        TAB_ALIAS_NAME.put("KnQrcodeNum", "user13" );    //二维码登陆

        // 职位
        TAB_ALIAS_NAME.put("KnPosition", "pos01" );

        // 分组
        TAB_ALIAS_NAME.put("KnGroup", "user02" );
        TAB_ALIAS_NAME.put("KnGroupEmp", "user03" );
        TAB_ALIAS_NAME.put("KnGroupOrg", "user04" );
        TAB_ALIAS_NAME.put("KnEmployeeOrganization", "user05" );

        // 角色
        TAB_ALIAS_NAME.put("KnRole", "role01" );
        TAB_ALIAS_NAME.put("KnRoleOrg", "role02" );
        TAB_ALIAS_NAME.put("KnRoleOrganization", "role03" );
        TAB_ALIAS_NAME.put("KnRoleResource", "role04" );
        TAB_ALIAS_NAME.put("KnEmployeeRole", "role05" );

        // 应用模块
        TAB_ALIAS_NAME.put("KnApplicationInfo", "app01" );
        TAB_ALIAS_NAME.put("KnApplicationVersion", "app02" );
        TAB_ALIAS_NAME.put("KnApplicationInfoPublicPack", "app03" );
        TAB_ALIAS_NAME.put("KnAppApprove", "app04" );
        TAB_ALIAS_NAME.put("XsBsFunction", "app05" );
        TAB_ALIAS_NAME.put("XsBsFunctionImg", "app06" );
        TAB_ALIAS_NAME.put("XsBsFunctionVersion", "app07" );
        TAB_ALIAS_NAME.put("XsBsFunctionCategory", "app08" );
        TAB_ALIAS_NAME.put("XsBsFunctionLabel", "app09" );
        TAB_ALIAS_NAME.put("XsBsSetting", "app10" );
        TAB_ALIAS_NAME.put("KnApplicationUpdateScope", "app11" );
        TAB_ALIAS_NAME.put("XsBsFunctionDiscontinuation", "app12" );
        TAB_ALIAS_NAME.put("XsBsFunctionUninstall", "app18" );

        TAB_ALIAS_NAME.put("XsBsApplicationInfo", "app13" );
        TAB_ALIAS_NAME.put("XsBsApplicationInfoImg", "app14" );
        TAB_ALIAS_NAME.put("XsBsApplicationInfoVersion", "app15" );
        TAB_ALIAS_NAME.put("XsBsApplicationInfoCategory", "app16" );
        TAB_ALIAS_NAME.put("XsBsApplicationInfoLabel", "app17" );

        TAB_ALIAS_NAME.put("XsBsLayout", "app19" );
        TAB_ALIAS_NAME.put("XsBsLayoutRole", "app20" );
        TAB_ALIAS_NAME.put("XsBsLayoutModuleApp", "app21" );

        TAB_ALIAS_NAME.put("XsBsApplicationComment", "app22" );// 应用评论表

        // 设备模块
        TAB_ALIAS_NAME.put("KnDeviceInfo", "device01" );
        TAB_ALIAS_NAME.put("KnDeviceAppLink", "device02" );

        // 配置模块
        TAB_ALIAS_NAME.put("KnRfConfig", "config01" );
        TAB_ALIAS_NAME.put("KnSkin", "config02" );
        TAB_ALIAS_NAME.put("KnEmoticon", "config03" );
        TAB_ALIAS_NAME.put("KnCertificate", "config04" );
        TAB_ALIAS_NAME.put("XsBsFeedback", "config05" );
        TAB_ALIAS_NAME.put("KnUserConfig", "config06" );
        TAB_ALIAS_NAME.put("KnPushMessage", "config07" );
        TAB_ALIAS_NAME.put("KnLdapInfo", "config08" );
        TAB_ALIAS_NAME.put("KnLdapOrg", "config09" );
        TAB_ALIAS_NAME.put("KnLdapConfig", "config10" );
        TAB_ALIAS_NAME.put("KnPushMessageEmpLink", "config11" );
        TAB_ALIAS_NAME.put("XsBsAttachment", "config12" );
        TAB_ALIAS_NAME.put("ImGroup", "config13" );
        TAB_ALIAS_NAME.put("ImGroupOrganization", "config14" );
        TAB_ALIAS_NAME.put("ImGroupEmployee", "config15" );

        // report
        TAB_ALIAS_NAME.put("RepDim", "rep01" );
        TAB_ALIAS_NAME.put("RepInd", "rep02" );
        TAB_ALIAS_NAME.put("RepModel", "rep03" );
        TAB_ALIAS_NAME.put("RepModelMapping", "rep04" );
        TAB_ALIAS_NAME.put("RepView", "rep05" );
        TAB_ALIAS_NAME.put("RepSerialNumber", "rep06" );
        TAB_ALIAS_NAME.put("RepDic", "rep07" );

        // behavior
        TAB_ALIAS_NAME.put("BehaviorBaseDevice", "behavior01" );
        TAB_ALIAS_NAME.put("BehaviorBaseEvent", "behavior02" );
        TAB_ALIAS_NAME.put("BehaviorBasePlatform", "behavior03" );
        TAB_ALIAS_NAME.put("BehaviorCollectApp", "behavior04" );
        TAB_ALIAS_NAME.put("BehaviorCollectErrorLog", "behavior05" );
        TAB_ALIAS_NAME.put("BehaviorCollectInterfaceLog", "behavior06" );
        TAB_ALIAS_NAME.put("BehaviorCollectOperateLog", "behavior07" );
        TAB_ALIAS_NAME.put("BehaviorCollectTimeDuration", "behavior08" );

        TAB_ALIAS_NAME.put("BehaviorCollectAppBak", "behavior09" );
        TAB_ALIAS_NAME.put("BehaviorCollectErrorLogBak", "behavior10" );
        TAB_ALIAS_NAME.put("BehaviorCollectInterfaceLogBak", "behavior11" );
        TAB_ALIAS_NAME.put("BehaviorCollectOperateLogBak", "behavior12" );
        TAB_ALIAS_NAME.put("BehaviorCollectTimeDurationBak", "behavior13" );

        // scheduler
        TAB_ALIAS_NAME.put("SchTask", "sch01" );
        TAB_ALIAS_NAME.put("SchTaskType", "sch02" );
        TAB_ALIAS_NAME.put("SchTaskExecuteLog", "sch03" );
    }
}
