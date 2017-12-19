package com.svlada.common;

import com.svlada.common.utils.ApplicationSupport;
import com.svlada.component.repository.PartnerRepository;
import com.svlada.component.repository.UserRepository;
import com.svlada.entity.Partner;
import com.svlada.entity.User;
import com.svlada.security.model.UserContext;
import com.svlada.security.model.token.JwtToken;
import com.svlada.security.model.token.JwtTokenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
public class WebUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebUtil.class);

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }

    public static boolean isContentTypeJson(SavedRequest request) {
        return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    }

    //当前登录的用户
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setCurrentUser(User user){
        userThreadLocal.set(user);
        LOGGER.info("当前线程的ID:{},设置用户信息 nickeName :{} || jwtToken:{}",Thread.currentThread().getId(),user.getNickName(),user.getJwtToken());
    }

    public static User getCurrentTreadUser() {
        User user = userThreadLocal.get();
        if (user!=null){
            LOGGER.info("当前线程的ID:{},获取用户信息 nickeName :{} || jwtToken:{}",Thread.currentThread().getId(),user.getNickName(),user.getJwtToken());
        }else {
            LOGGER.info("当前线程的ID:{},当前线程没有存储用户信息!",Thread.currentThread().getId());
        }
        return user;
    }

    public static User getCurrentUser(){
        /*JwtHeaderTokenExtractor tokenExtractor = ApplicationSupport.getBean(JwtHeaderTokenExtractor.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String tokenPayload = request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM);
        if (StringUtils.isEmpty(tokenPayload)){
            LOGGER.info("非后台请求，从当前线程取当前用户信息！");*/
            return getCurrentTreadUser();
        /*}else {
            LOGGER.info("后台请求，从头信息中获取当前用户信息！");
            RawAccessJwtToken token = new RawAccessJwtToken(tokenExtractor.extract(tokenPayload));
            return token.loadUserToMemory();
        }*/
    }

    public static void removeUser(){
        User user = userThreadLocal.get();
        if (user!=null){
            LOGGER.info("当前线程的ID:{},获取用户信息,准备移除 nickeName :{} || jwtToken:{}",Thread.currentThread().getId(),user.getNickName(),user.getJwtToken());
            userThreadLocal.remove();
        }else {
            LOGGER.error("无当前用户信息！");
        }

    }

    public static String createTokenByOpenId(String openId){
        UserRepository userRepository = ApplicationSupport.getBean(UserRepository.class);
        JwtTokenFactory jwtTokenFactory = ApplicationSupport.getBean(JwtTokenFactory.class);
        User user = userRepository.findOneByOpenId(openId);
        SimpleGrantedAuthority role_admin = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(role_admin);
        UserContext userContext = UserContext.create(user.getOpenId(), authorities);
        JwtToken jwtToken = jwtTokenFactory.createAccessJwtToken(userContext);
        String token = jwtToken.getToken();
        user.setJwtToken(token);
        userRepository.save(user);
        return token;
    }

    /**
     * 建立分享人与被分享人之间的关系：条件，分享人必须是会员
     */
    public static String bindShare(String openId, Map<String,Object> params){
        LOGGER.info("进入分享逻辑,params:{}",params);
        UserRepository userRepository = ApplicationSupport.getBean(UserRepository.class);
        PartnerRepository partnerRepository = ApplicationSupport.getBean(PartnerRepository.class);
        String shareOpenId =  params.get("shareOpenId")==null?openId:(String)params.get("shareOpenId");
        User shareUser = userRepository.findOneByOpenId(shareOpenId);//分享链接的人
        if (shareUser == null || shareUser.getMember()==null ||shareUser.getMember()==false) {
            LOGGER.error("分享人不存在或者说分享人非会员！");
        }
        Long userId = shareUser.getId();
        User currentUser = WebUtil.getCurrentUser();//被分享的人
        if (currentUser.getId().equals(userId)){//进入链接的人与分享的人是同一个人
            LOGGER.info("自己进入自己分享的链接！");
        }else {
            //查看该用户是否已经有过合作伙伴
            Partner oneByUserId = partnerRepository.findFirstByUserId(currentUser.getId());//分享给我的人
            if (oneByUserId!=null){//已经有合作伙伴了
                LOGGER.info("当前用户已经被分享成为合作伙伴了，再次分享无效!");
            }else {
                Partner partner = partnerRepository.findOneByUserIdAndShareUserId(currentUser.getId(),shareUser.getId());
                Partner exist = partnerRepository.findOneByUserIdAndShareUserId(shareUser.getId(), currentUser.getId());
                if (partner == null && exist==null ) {//没有，则添加分享人为合作伙伴
                    if (shareUser.getMember()!=null & shareUser.getMember()){
                        LOGGER.info("非合作伙伴关系,则添加分享人为合作伙伴!");
                        partner = new Partner();
                        partner.setShareUser(shareUser);
                        partner.setUserId(currentUser.getId());
                        partner.setCreateDate(new Date());
                        partnerRepository.save(partner);
                    }else {
                        LOGGER.info("非会员分享优惠无效!");
                    }
                }else {//已经有合作伙伴不做处理
                    LOGGER.error("分享人：{} 与 被分享人：{} 已经是合作伙伴关系！",shareUser.getNickName(),currentUser.getNickName());
                }
            }
        }
        return null;
    }
}
