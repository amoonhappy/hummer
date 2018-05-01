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
 * @author <a href="mailto:jeff_myth@yahoo.com.cn">Jeff Zhou</a> Date: 2005-11-21 21:15:47
 * @version 1.0
 */
package org.hummer.core.util;

import org.apache.commons.lang.StringUtils;

import java.util.StringTokenizer;

public class StringUtil {
    public static boolean isNumeric(String input) {
        return StringUtils.isNumeric(input);
    }

    public static boolean isEmpty(String input) {
        return StringUtils.isEmpty(input);
    }

    public static String substring(String s1, int i) {
        return StringUtils.substring(s1, i);
    }

    public static int lastIndexOf(String s1, String s2) {
        return StringUtils.lastIndexOf(s1, s2);
    }

    /**
     * Joins up the pieces resulted from dividing the given string with the
     * given separator
     *
     * @param inString  The pass in string
     * @param separator The separator use to cut off the string into pieces
     * @return The resultant array of string
     */
    public static String[] joinArray(String inString, String separator) {
        String[] arrString;
        StringTokenizer st = new StringTokenizer(inString, separator);
        arrString = new String[st.countTokens()];

        for (int i = 0; i < arrString.length; i++) {
            arrString[i] = (st.nextToken()).trim();
        }

        return arrString;
    }

    /**
     * Check that the given CharSequence is neither {@code null} nor of length
     * 0. Note: Will return {@code true} for a CharSequence that purely consists
     * of whitespace.
     *
     *
     * <pre class="code">
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be {@code null})
     * @return {@code true} if the CharSequence is not null and has length
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check whether the given CharSequence has actual text. More specifically,
     * returns {@code true} if the string not {@code null}, its length is
     * greater than 0, and it contains at least one non-whitespace character.
     *
     *
     * <pre class="code">
     * StringUtils.hasText(null) = false
     * StringUtils.hasText("") = false
     * StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true
     * StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be {@code null})
     * @return {@code true} if the CharSequence is not {@code null}, its length
     * is greater than 0, and it does not contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Replace all occurences of a substring within a string with another
     * string.
     *
     * @param inString   String to examine
     * @param oldPattern String to replace
     * @param newPattern String to insert
     * @return a String with the replacements
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0; // our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

}
