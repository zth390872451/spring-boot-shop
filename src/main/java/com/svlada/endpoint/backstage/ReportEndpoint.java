package com.svlada.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.component.service.ReportService;
import com.svlada.endpoint.dto.ProductInfoDescDto;
import com.svlada.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.component.service.ReportService.CURRENT_DAY;
import static com.svlada.component.service.ReportService.CURRENT_MONTH;
import static com.svlada.component.service.ReportService.RECENT_MONTH;

@RestController
@RequestMapping("/api/back/report")
public class ReportEndpoint {

    @Autowired
    private ReportService reportService;

    @GetMapping("/user")
    public CustomResponse getUser(){
        Long current_day = reportService.getLoginUser(CURRENT_DAY);
        Long current_month = reportService.getLoginUser(CURRENT_MONTH);
        Long recent_month = reportService.getLoginUser(RECENT_MONTH);
        Map<String,Object> result = new HashMap<>();
        result.put("currentDay",current_day);
        result.put("currentMonth",current_month);
        result.put("recentMonth",recent_month);
        return success(result);
    }

    /**
     *
     * @param pageable 分页参数:
     * 1、商品销量 pageable=(sort=saleCount,DESC) 默认
     * 2、商品庫存數量 pageable=(sort=stock,DESC)
     * 3、商品收藏數量 pageable=(sort=favorite,DESC)
     * 4、商品评论数量 pageable=(sort=badComment,DESC || goodComment,DESC)
     * 5、商品浏览数量 pageable=(sort=hit,DESC)
     * @return
     */
    @GetMapping("/product")
    public CustomResponse getProduct(@PageableDefault(value = 10, sort = {"saleCount"}, direction = Sort.Direction.DESC)
                                                 Pageable pageable){
        Map<String,Object> result = new HashMap<>();
        PageRequest pageRequest = null;

        pageRequest = new PageRequest(0,10,Sort.Direction.DESC,"saleCount");
        List<ProductInfoDescDto> saleCountProduct = reportService.getProductInfoDescDto(pageRequest);

        pageRequest = new PageRequest(0,10,Sort.Direction.DESC,"stock");
        List<ProductInfoDescDto> stockProduct = reportService.getProductInfoDescDto(pageRequest);

        pageRequest = new PageRequest(0,10,Sort.Direction.DESC,"favorite");
        List<ProductInfoDescDto> favoriteProduct = reportService.getProductInfoDescDto(pageRequest);

        pageRequest = new PageRequest(0,10,Sort.Direction.DESC,"badComment");
        List<ProductInfoDescDto> badCommentProduct = reportService.getProductInfoDescDto(pageRequest);

        pageRequest = new PageRequest(0,10,Sort.Direction.DESC,"goodComment");
        List<ProductInfoDescDto> goodCommentProduct = reportService.getProductInfoDescDto(pageRequest);

        pageRequest = new PageRequest(0,10,Sort.Direction.DESC,"hit");
        List<ProductInfoDescDto> hitProduct = reportService.getProductInfoDescDto(pageRequest);

        result.put("saleCount",saleCountProduct);
        result.put("stock",stockProduct);
        result.put("favorite",favoriteProduct);
        result.put("badComment",badCommentProduct);
        result.put("goodComment",goodCommentProduct);
        result.put("hit",hitProduct);
        return success(result);
    }

}
