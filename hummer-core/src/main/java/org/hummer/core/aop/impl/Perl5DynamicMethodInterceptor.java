package org.hummer.core.aop.impl;

import org.apache.oro.text.regex.*;
import org.hummer.core.util.Log4jUtils;
import org.hummer.core.util.StringUtil;
import org.slf4j.Logger;

/**
 * iFOP Spacee for Developer Party
 *
 * @author jeff.zhou
 */
public abstract class Perl5DynamicMethodInterceptor extends DynamicChainMethodInterceptor {
    private static Logger log = Log4jUtils.getLogger(Perl5DynamicMethodInterceptor.class);

    private String xmlPattern = null;

    private String xmlExcludePattern = null;

    private transient Pattern[] patterns = new Pattern[0];

    private transient Pattern[] compiledExclusionPatterns = new Pattern[0];

    private transient PatternMatcher matcher;

    public String getXmlExcludePattern() {
        return xmlExcludePattern;
    }

    public void setXmlExcludePattern(String xmlExcludePattern) {
        this.xmlExcludePattern = xmlExcludePattern;
        if (log.isDebugEnabled()) {
            log.debug("xmlExcludePattern=" + xmlExcludePattern);
        }
        if (!StringUtil.isEmpty(xmlExcludePattern)) {
            super.setExcludedPatterns(StringUtil.joinArray(xmlExcludePattern, ","));
        }
    }

    //TODO: AOP Exclude Logic to be added
    public String getXmlPattern() {
        return xmlPattern;
    }

    public void setXmlPattern(String xmlPattern) {
        this.xmlPattern = xmlPattern;
        if (log.isDebugEnabled()) {
            log.debug("xmlPattern=" + xmlPattern);
        }
        if (!StringUtil.isEmpty(xmlPattern)) {
            super.setPatterns(StringUtil.joinArray(xmlPattern, ","));
        }
    }

    @Override
    protected void initExcludePatterns(String[] excludePatterns) {
        this.compiledExclusionPatterns = compilePatterns(excludePatterns);
        this.matcher = new Perl5Matcher();
    }

    @Override
    protected void initPatterns(String[] patterns) {
        this.patterns = compilePatterns(patterns);
        this.matcher = new Perl5Matcher();
    }

    /**
     * Compiles the supplied pattern sources into a {@link Pattern} array.
     */
    private Pattern[] compilePatterns(String[] source) {
        Perl5Compiler compiler = new Perl5Compiler();
        Pattern[] destination = new Pattern[source.length];
        for (int i = 0; i < source.length; i++) {
            // compile the pattern to be thread-safe
            try {
                destination[i] = compiler.compile(source[i], Perl5Compiler.READ_ONLY_MASK);
            } catch (MalformedPatternException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }
        return destination;
    }

    /**
     * Returns <code>true</code> if the {@link Pattern} at index
     * <code>patternIndex</code> matches the supplied candidate
     * <code>String</code>.
     */
    protected boolean matches(String pattern, int patternIndex) {
        try {
            return this.matcher.matches(pattern, this.patterns[patternIndex]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Initializes the {@link Pattern ORO representation} of the supplied
     * exclusion patterns.
     */
    protected void initExcludedPatternRepresentation(String[] excludedPatterns) throws IllegalArgumentException {
        this.compiledExclusionPatterns = compilePatterns(excludedPatterns);
    }

    /**
     * Returns <code>true</code> if the exclusion {@link Pattern} at index
     * <code>patternIndex</code> matches the supplied candidate
     * <code>String</code>.
     */
    protected boolean matchesExclusion(String pattern, int patternIndex) {
        try {
            return this.matcher.matches(pattern, this.compiledExclusionPatterns[patternIndex]);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

}
