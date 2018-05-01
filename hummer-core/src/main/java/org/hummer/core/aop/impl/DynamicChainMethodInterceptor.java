package org.hummer.core.aop.impl;

import org.hummer.core.aop.intf.Interceptor;
import org.hummer.core.aop.intf.MethodMatcher;
import org.hummer.core.util.Assert;

import java.lang.reflect.Method;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public abstract class DynamicChainMethodInterceptor implements MethodMatcher, Interceptor {

    /**
     * Regular expressions to match
     */
    private String[] patterns = new String[0];

    /**
     * Regaular expressions <strong>not</strong> to match
     */
    private String[] excludedPatterns = new String[0];

    protected abstract void initPatterns(String[] patterns);

    protected abstract void initExcludePatterns(String[] excludePatterns);

    /**
     * Convenience method when we have only a single pattern. Use either this
     * method or {@link #setPatterns}, not both.
     *
     * @see #setPatterns
     */
    public void setPattern(String pattern) {
        setPatterns(new String[]{pattern});
    }

    /**
     * Return the regular expressions for method matching.
     */
    public String[] getPatterns() {
        return this.patterns;
    }

    /**
     * Set the regular expressions defining methods to match. Matching will be
     * the union of all these; if any match, the pointcut matches.
     *
     * @param patterns regular expressions describing methods to match
     */
    void setPatterns(String[] patterns) {
        Assert.notEmpty(patterns, "'patterns' cannot be null or empty.");
        this.patterns = patterns;
        initPatterns(patterns);
    }

    /**
     * Convenience method when we have only a single exclusion pattern. Use
     * either this method or {@link #setExcludedPatterns}, not both.
     *
     * @see #setExcludedPatterns
     */
    public void setExcludedPattern(String excludedPattern) {
        setExcludedPatterns(new String[]{excludedPattern});
    }

    /**
     * Returns the regular expressions for exclusion matching.
     */
    public String[] getExcludedPatterns() {
        return this.excludedPatterns;
    }

    /**
     * Set the regular expressions defining methods to match for exclusion.
     * Matching will be the union of all these; if any match, the pointcut
     * matches.
     *
     * @param excludedPatterns regular expressions describing methods to match for exclusion
     */
    void setExcludedPatterns(String[] excludedPatterns) {
        Assert.notEmpty(excludedPatterns, "'excludedPatterns' cannot be null or empty.");
        this.excludedPatterns = excludedPatterns;
        initExcludePatterns(excludedPatterns);
    }

    /**
     * Does the pattern at the given index match this string?
     *
     * @param pattern      <code>String</code> pattern to match
     * @param patternIndex index of pattern from 0
     * @return <code>true</code> if there is a match, else <code>false</code>.
     */
    protected abstract boolean matches(String pattern, int patternIndex);

    /**
     * Does the exclusion pattern at the given index match this string?
     *
     * @param pattern      <code>String</code> pattern to match.
     * @param patternIndex index of pattern starting from 0.
     * @return <code>true</code> if there is a match, else <code>false</code>.
     */
    protected abstract boolean matchesExclusion(String pattern, int patternIndex);

    public boolean matches(Method method, Class targetClass) {
        // return true;
        String patt = method.getDeclaringClass().getName() + "." + method.getName();
        for (int i = 0; i < this.patterns.length; i++) {
            boolean matched = matches(patt, i);
            if (matched) {
                for (int j = 0; j < this.excludedPatterns.length; j++) {
                    boolean excluded = matchesExclusion(patt, j);
                    if (excluded) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

}
