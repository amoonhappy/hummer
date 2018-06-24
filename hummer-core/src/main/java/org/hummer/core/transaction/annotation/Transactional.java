package org.hummer.core.transaction.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {
    Propagation propagation() default Propagation.REQUIRED;

    Isolation isolation() default Isolation.DEFAULT;

    int timeout() default -1;

    boolean readOnly() default false;

    //TODO: below attribute is not implemented yet
    Class[] rollbackFor() default {};

    String[] rollbackForClassName() default {};

    Class[] noRollbackFor() default {};

    String[] noRollbackForClassName() default {};
}