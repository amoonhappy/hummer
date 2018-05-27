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
package org.hummer.core.util;

import org.slf4j.Logger;

import java.util.*;

/**
 * @author jeff.zhou
 */
public class CollectionUtil {
    private static final Logger log = Log4jUtils.getLogger(CollectionUtil.class);

    public static Collection<Object> union(Collection<Object> original, Collection<Object> external) {
        if ((original == null) || (original.size() == 0)) {
            return external;
        } else if ((external == null) || (external.size() == 0)) {
            return original;
        } else {
            original.addAll(external);

            return original;
        }
    }

    /**
     * Method to convert a ResourceBundle to a Map object.
     *
     * @param rb a given resource bundle
     * @return Map a populated map
     */
    public static Map<String, String> convertBundleToMap(ResourceBundle rb) {
        Map<String, String> map = new HashMap<String, String>();

        for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements(); ) {
            String key = keys.nextElement();
            map.put(key, rb.getString(key));
        }

        return map;
    }

    public static List<?> collectionCopy(Collection<?> srcList) {
        if (srcList == null)
            return new ArrayList<Object>();
        List<?> ret = null;
        try {
            ret = (List<?>) SerializableUtil.cloneSerializable(srcList);
            // srcbyte = SerializableUtils.serializeToByteArray(srcList);
            // ret = (List) SerializableUtils.deserializeFromByteArray(srcbyte);
        } catch (RuntimeException e) {
            log.error("Unable to clone the Collection", e);
        }
        return ret;
    }

    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Collection collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Return {@code true} if the supplied Map is {@code null} or empty.
     * Otherwise, return {@code false}.
     *
     * @param map the Map to check
     * @return whether the given Map is empty
     */
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

}
