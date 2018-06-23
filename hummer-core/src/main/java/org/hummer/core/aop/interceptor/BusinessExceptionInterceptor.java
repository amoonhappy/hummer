package org.hummer.core.aop.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.hummer.core.constants.ExceptionConstant;
import org.hummer.core.context.impl.ContextHolder;
import org.hummer.core.context.impl.RequestContext;
import org.hummer.core.exception.BusinessException;
import org.hummer.core.message.intf.IMessage;
import org.hummer.core.service.impl.ReturnValue;
import org.hummer.core.util.Log4jUtils;
import org.slf4j.Logger;

/**
 * @author jeff.zhou
 */
public class BusinessExceptionInterceptor implements org.aopalliance.intercept.MethodInterceptor {
    private static final Logger log = Log4jUtils.getLogger(BusinessExceptionInterceptor.class);

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        ReturnValue value;
        Object target = methodInvocation.getThis();
        Object returnValue = null;
        int errorCode = 0;
        IMessage error = null;

        try {
            returnValue = methodInvocation.proceed();
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                error = businessException.getError();
                log.error("Business Exception", businessException);
                // } else if (e instanceof CommitException) {
                // CommitException commitException = (CommitException) e;
                // errorCode = commitException.getErrorCode();
                // log.error("Commit Exception", commitException);
            } else {
                errorCode = ExceptionConstant.SYS_RUNTIME_ERROR;
                log.error("System Internal Error", e);
            }
        }

        if (returnValue instanceof ReturnValue) {
            value = (ReturnValue) returnValue;
        }

        RequestContext context = ContextHolder.getRequestContext();

        // if there is a request context, the system will process the error
        // messages
        if ((context != null)) {
//            //HttpServletRequest request = context.getRequest();
//
//            // if error code had been evaluated,that is there are exception
//            // throw out.
//            if (errorCode < 0 || (error != null && error.getCode() < 0)) {
//                if (target instanceof IService) {
//                    ActionMessages errors = (ActionMessages) request.getAttribute(Constant.REQUEST_FORM_ERRORS);
//
//                    if (errors == null) {
//                        errors = new ActionMessages();
//                    }
//                    disposeMessage(errors, error);
//                    request.setAttribute(Constant.REQUEST_FORM_ERRORS, errors);
//                }
//
//                // if successful
//            } else {
//                if (returnValue instanceof ReturnValue) {
//                    ActionMessages messages = getMessages(request);
//                    disposeMessages(messages, ((ReturnValue) returnValue).getMessages());
//                    request.setAttribute(Globals.MESSAGE_KEY, messages);
//
//                } else {
//                    return returnValue;
//                }
//            }
        }

        return returnValue;
    }

//    private void disposeMessages(ActionMessages errors, Messages messages) {
//        if (messages != null && messages.getSize() > 0) {
//            for (int i = 0; i < messages.getSize(); i++) {
//                disposeMessage(errors, messages.getMessage(i));
//            }
//        }
//    }

//    private void disposeMessage(ActionMessages errors, IMessage message) {
//        if (message != null) {
//            String key = message.getMessageKey();
//            ActionMessage error;
//
//            final List<Object> params = message.getParams();
//            if ((params == null) || (params.size() == 0)) {
//                error = new ActionMessage(key);
//            } else {
//                error = new ActionMessage(key, params.toArray(new Object[0]));
//            }
//
//            errors.add(key, error);
//        }
//    }
//
//    private ActionMessages getMessages(HttpServletRequest request) {
//        ActionMessages messages;
//        HttpSession session = request.getSession();
//
//        if (request.getAttribute(Globals.MESSAGE_KEY) != null) {
//            messages = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
//        } else if (session.getAttribute(Globals.MESSAGE_KEY) != null) {
//            messages = (ActionMessages) session.getAttribute(Globals.MESSAGE_KEY);
//            session.removeAttribute(Globals.MESSAGE_KEY);
//        } else {
//            messages = new ActionMessages();
//        }
//
//        return messages;
//    }
}
