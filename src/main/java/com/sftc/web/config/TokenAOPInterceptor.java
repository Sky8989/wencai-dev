//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Enumeration;
//
//public class TokenAOPInterceptor extends HandlerInterceptorAdapter {
//
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response, Object handler) throws Exception {
//        Log.info(request);
//
//        String token = request.getParameter("token");
//
//        // token is not needed when debug
//        if(token == null) return true;  // !! remember to comment this when deploy on server !!
//
//        Enumeration paraKeys = request.getParameterNames();
//        String encodeStr = "";
//        while (paraKeys.hasMoreElements()) {
//            String paraKey = (String) paraKeys.nextElement();
//            if(paraKey.equals("token"))
//                break;
//            String paraValue = request.getParameter(paraKey);
//            encodeStr += paraValue;
//        }
//        encodeStr += Default.TOKEN_KEY;
//        Log.out(encodeStr);
//
//        if ( ! token.equals(DigestUtils.md5Hex(encodeStr))) {
//            response.setStatus(500);
//            return false;
//        }
//
//        return true;
//    }
//
//    public void postHandle(HttpServletRequest request,
//                           HttpServletResponse response, Object handler,
//                           ModelAndView modelAndView) throws Exception {
//        Log.info(request);
//    }
//
//    public void afterCompletion(HttpServletRequest request,
//                                HttpServletResponse response, Object handler, Exception ex)
//            throws Exception {
//
//    }
//}