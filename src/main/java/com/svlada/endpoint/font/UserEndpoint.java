package com.svlada.endpoint.font;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.common.utils.DateUtils;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.PartnerRepository;
import com.svlada.component.repository.UserRepository;
import com.svlada.endpoint.dto.OrderInfoDto;
import com.svlada.endpoint.dto.UserDto;
import com.svlada.entity.Order;
import com.svlada.entity.Partner;
import com.svlada.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.entity.Order.pay_status_success;

@RestController
@RequestMapping("/api/font/user")
public class UserEndpoint {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartnerRepository partnerRepository;

    @PostMapping(value = "/register")
    public CustomResponse register(@RequestBody UserDto dto) {
        User user = userRepository.findOne(dto.getId());
        userRepository.save(user);
        return success();
    }


    @PutMapping(value = "/forgot")
    public CustomResponse forgot(@RequestBody UserDto dto) {
        User user = userRepository.findOne(dto.getId());
        userRepository.save(user);
        return success();
    }

    @GetMapping("/get")
    public CustomResponse get() {
        User user = WebUtil.getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("nickName", user.getNickName());
        result.put("balance", user.getBalance());
        result.put("member", user.getMember() == null ? false : true);
        result.put("mobile", user.getMobile());
        result.put("sex", user.getSex());
        result.put("headImageUrl", user.getHeadImgUrl());
        result.put("province", user.getProvince());
        result.put("city", user.getCity());
        result.put("address", user.getAddress());
        return success(result);
    }


    @PostMapping(value = "/update")
    public CustomResponse update(@RequestBody UserDto dto) {
        User user = WebUtil.getCurrentUser();
        if (user == null) {
            return fail(CustomResponseStatus._40000, "用户ID对应的记录不存在!");
        }
        if (!StringUtils.isEmpty(dto.getMobile())) {
            user.setMobile(dto.getMobile());
        }
        if (!StringUtils.isEmpty(dto.getNickName())) {
            user.setNickName(dto.getNickName());
        }
        if (!StringUtils.isEmpty(dto.getProvince())) {
            user.setProvince(dto.getProvince());
        }
        if (!StringUtils.isEmpty(dto.getCity())) {
            user.setCity(dto.getCity());
        }
        if (!StringUtils.isEmpty(dto.getAddress())) {
            user.setAddress(dto.getAddress());
        }
        userRepository.save(user);
        return success();
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(UserEndpoint.class);

    /**
     * 查看用户合伙人
     *
     * @return
     */
    @GetMapping("/list/partner")
    public CustomResponse partner() {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> partner = null;
        User user = WebUtil.getCurrentUser();
        Partner shareToMe = partnerRepository.findFirstByUserId(user.getId());//分享给我的人
        List<Partner> partners = partnerRepository.findAllByShareUserId(user.getId());//我分享出去的人
        List<Map<String, String>> meShareTo = new ArrayList<>();
        for (Partner each : partners) {
            User part = userRepository.findOne(each.getUserId());
            if (part != null) {
                partner = new HashMap<>();
                partner.put("nickName", part.getNickName());
                meShareTo.add(partner);
            } else {
                LOGGER.info("数据异常！{}", each.getUserId());
            }

        }
        result.put("meShareTo", meShareTo);
        if (shareToMe != null) {
            if (shareToMe.getShareUser() != null) {
                result.put("shareToMe", shareToMe.getShareUser().getNickName());
            } else {
                LOGGER.error("数据异常！分享给我的人用户不存在!Partner记录ID:{}", shareToMe.getId());
            }
        } else {
            result.put("shareToMe", null);
            LOGGER.info("该用户非会员分享登陆！");
        }
        LOGGER.info("该用户的合作伙伴和分享人：有{}", result);
        return success(result);
    }

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 查看订单列表
     *
     * @return
     */
    @GetMapping("/list/order")
    public CustomResponse order() {
        User user = WebUtil.getCurrentUser();
        List<Order> orders = orderRepository.findOneByUserIdAndPayStatus(user.getId(), pay_status_success);
        List<OrderInfoDto> orderInfoDtos = new ArrayList<>();
        for (Order entity : orders) {
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            orderInfoDto.setBody(entity.getBody());
            orderInfoDto.setDetails(entity.getDetails());
            orderInfoDto.setId(entity.getId());
            orderInfoDto.setOutTradeNo(entity.getOutTradeNo());
            orderInfoDto.setPaymentDate(DateUtils.getFormatDate(entity.getPaymentDate(), DateUtils.FULL_DATE_FORMAT));
            orderInfoDto.setPayStatus(entity.getPayStatus());
            orderInfoDto.setTotalMoney(entity.getTotalMoney());
            if (entity.getWxpayNotify() != null) {
                orderInfoDto.setTransactionId(entity.getWxpayNotify().getTransactionId());
            }
            orderInfoDtos.add(orderInfoDto);
        }
        return success(orderInfoDtos);
    }

    /**
     * 查看合作伙伴订单
     *
     * @return
     */
    @GetMapping("/list/partnerOrder")
    public CustomResponse partnerOrder() {
        User currentUser = WebUtil.getCurrentUser();
        Map<String, Object> result = new HashMap<>();
        result.put("isMember", currentUser.getMember());
        if (currentUser != null && currentUser.getMember() != null && currentUser.getMember() == true) {
            List<Order> orders = orderRepository.findByShareIdAndPayStatus(currentUser.getOpenId(), pay_status_success);
            List<OrderInfoDto> orderInfoDtos =orders.stream().map(entity -> {
                OrderInfoDto orderInfoDto = new OrderInfoDto();
                orderInfoDto.setBody(entity.getBody());
                orderInfoDto.setDetails(entity.getDetails());
                orderInfoDto.setId(entity.getId());
                orderInfoDto.setOutTradeNo(entity.getOutTradeNo());
                orderInfoDto.setPaymentDate(DateUtils.getFormatDate(entity.getPaymentDate(), DateUtils.FULL_DATE_FORMAT));
                orderInfoDto.setPayStatus(entity.getPayStatus());
                orderInfoDto.setTotalMoney(entity.getTotalMoney());
                orderInfoDto.setShareFlag(entity.getShareFlag());//是否已结算
                if (entity.getWxpayNotify() != null) {
                    orderInfoDto.setTransactionId(entity.getWxpayNotify().getTransactionId());
                }
                User user = entity.getUser();
                if (user != null) {
                    orderInfoDto.setNickName(user.getNickName());
                    orderInfoDto.setOpenId(user.getOpenId());
                }
                if (!StringUtils.isEmpty(entity.getShareId())) {//有分享人则有佣金显示
                    User shareUser = userRepository.findOneByOpenId(entity.getShareId());
                    if (shareUser != null) {
                        orderInfoDto.setShareNickName(shareUser.getNickName());
                    }
                    orderInfoDto.setShareFlag(entity.getShareFlag());
                }
                return orderInfoDto;
            }).collect(Collectors.toList());
            Long totalMoney = 0L;
            for (Order order : orders) {
                if (!order.getShareFlag()) {//未结算过佣金的订单
                    totalMoney += order.getTotalMoney();
                }
            }
            Integer number = orders.size();
            result.put("number", number);
            result.put("totalMoney", totalMoney);
            Double chargeDouble = totalMoney * 0.09;
            Integer charge = chargeDouble.intValue();
            result.put("charge", charge);
            result.put("orderInfoDtos",orderInfoDtos);
        } else {
            result.put("number", 0);
            result.put("totalMoney", 0);
            result.put("charge", 0);
        }
        return success(result);
    }


}
