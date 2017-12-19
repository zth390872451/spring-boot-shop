package com.svlada.component.service;

import com.svlada.common.utils.DateUtils;
import com.svlada.component.repository.OrderItemRepository;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.ProductRepository;
import com.svlada.component.repository.UserRepository;
import com.svlada.endpoint.dto.ProductInfoDescDto;
import com.svlada.endpoint.dto.builder.ProductInfoBuilder;
import com.svlada.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReportService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;

    public static final int CURRENT_DAY = 0;//0:今日登录用户统计
    public static final int CURRENT_MONTH = 1;//1:最近一个月登录用户统计
    public static final int RECENT_MONTH = 2;//2:本月登录用户统计
    /**
     *
     * @param type
     * @return
     */
    public Long getLoginUser(int type){
        Date startDate = new Date();
        Date endDate = new Date();
        Long result = 0L;
        switch (type){
            case 0:
                startDate = DateUtils.getStartTimeOfToday();
                endDate = DateUtils.getEndTimeOfToday();
                result = userRepository.countByLastLoginDateDateBetween(startDate, endDate);
                break;
            case 1:
                startDate = DateUtils.arithmetic(new Date(), Calendar.MONTH,-1);
                endDate = DateUtils.getEndTimeOfToday();
                result = userRepository.countByLastLoginDateDateBetween(startDate, endDate);
                break;
            case 2:
                startDate = DateUtils.getStartTimeOfMonth();
                endDate = DateUtils.getEndTimeOfMonth();
                result = userRepository.countByLastLoginDateDateBetween(startDate, endDate);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     *
     * @param pageable 分页参数:
     * 1、商品销量 pageable=(sort=saleCount,DESC)
     * 2、商品庫存數量 pageable=(sort=stock,DESC)
     * 3、商品收藏數量 pageable=(sort=favorite,DESC)
     * 4、商品评论数量 pageable=(sort=badComment,DESC || goodComment,DESC)
     * 5、商品浏览数量 pageable=(sort=hit,DESC)
     * @return
     */
    public List<ProductInfoDescDto> getProductInfoDescDto(Pageable pageable){
        Page<Product> productPage = productRepository.findAll(pageable);
        Page<ProductInfoDescDto> dtoPage = productPage.map(new Converter<Product, ProductInfoDescDto>() {
            @Override
            public ProductInfoDescDto convert(Product entity) {
                ProductInfoDescDto dto = ProductInfoBuilder.builderProductInfoDescDto(entity);
                return dto;
            }
        });
        List<ProductInfoDescDto> content = dtoPage.getContent();
        return content;
    }

}
