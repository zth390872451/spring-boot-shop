package com.svlada.endpoint.dto.builder;

import com.svlada.common.utils.DateUtils;
import com.svlada.endpoint.dto.OrderInfoDto;
import com.svlada.endpoint.dto.ProductInfoDescDto;
import com.svlada.entity.Order;
import com.svlada.entity.product.Product;

public class OrderInfoBuilder {

    /**
     * @param source
     * @return
     */
    public static OrderInfoDto builderOrderInfoDto(Order source) {
        OrderInfoDto dto = new OrderInfoDto();
        dto.setId(source.getId());
        dto.setBody(source.getBody());
        dto.setDetails(source.getDetails());
        dto.setOutTradeNo(source.getOutTradeNo());
        if (source!=null){
            dto.setPaymentDate(DateUtils.getFormatDate(source.getPaymentDate(),DateUtils.FULL_DATE_FORMAT));
        }
        dto.setPayStatus(source.getPayStatus());
        dto.setTotalMoney(source.getTotalMoney());
        if (source.getWxpayNotify()!=null){
            dto.setTransactionId(source.getWxpayNotify().getTransactionId());
        }
        return dto;
    }


}
