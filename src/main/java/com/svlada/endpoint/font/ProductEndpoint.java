package com.svlada.endpoint.font;

import com.svlada.common.request.CustomResponse;
import com.svlada.component.repository.ProductRepository;
import com.svlada.endpoint.dto.ProductDetailsDto;
import com.svlada.endpoint.dto.builder.ProductInfoBuilder;
import com.svlada.entity.product.Product;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/product")
public class ProductEndpoint {

    @Autowired
    private ProductRepository productRepository;

    @ApiOperation(value="商品信息列表", notes="商品信息列表分页")
    @ApiImplicitParams({
    })
    @GetMapping(value = "/list")
    public CustomResponse list(@RequestParam(name = "key",required = false) String key,
                               @RequestParam(name = "recommend",required = false) Boolean recommend,
                               @RequestParam(name = "isNew",required = false) Boolean isNew,
                               @PageableDefault(value = 20, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(this.getSpecification(key,recommend,isNew), pageable);
        Page<ProductDetailsDto> dtoPage = productPage.map(entity -> {
            ProductDetailsDto dto = ProductInfoBuilder.builderProductDetailsDto(entity);
            return dto;
        });
        return success(dtoPage);
    }

    private Specification<Product> getSpecification(String key,Boolean recommend,Boolean isNew){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(key)){
                Predicate searchKeyPredicate = cb.like(root.get("searchKey").as(String.class), "%" + key + "%");
                Predicate namePredicate = cb.like(root.get("name").as(String.class), "%" + key + "%");
                predicates.add(cb.or(searchKeyPredicate,namePredicate));
            }
            if(!StringUtils.isEmpty(recommend)){
                Predicate predicate = cb.equal(root.get("recommend").as(Boolean.class), recommend);
                predicates.add(predicate);
            }
            if(!StringUtils.isEmpty(isNew)){
                Predicate predicate = cb.equal(root.get("isNew").as(Boolean.class), isNew);
                predicates.add(predicate);
            }
            Predicate[] pre = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pre)).getRestriction();
        };
    }

    /**
     *  查询首页轮询的商品，如果详情图的商品不足3个，则将展示 按照id降序的有详情图的商品
     */
    @GetMapping(value = "/cyclic")
    public CustomResponse cyclic() {
        List<Product> productList = productRepository.findProductByCyclicIsTrue();
        List<ProductDetailsDto> detailsDtos = productList.stream().filter(product -> product.getDetailsImages()!=null && product.getDetailsImages().size()>0).map(entity -> {
            ProductDetailsDto dto = ProductInfoBuilder.builderProductDetailsDto(entity);
            return dto;
        }).collect(Collectors.toList());
        if (detailsDtos!=null && detailsDtos.size()<Product.CYCLIC_MAX){
            List<Long> productIds = new ArrayList<>();
            for (ProductDetailsDto dt:detailsDtos) {
                productIds.add(dt.getId());
            }
//            detailsDtos.stream().map(dt->productIds.add(dt.getId()));
            List<Product> products = productRepository.findAllByIdNotInOrderByIdDesc(productIds);
            for (Product product:products) {
                if (detailsDtos.size()<Product.CYCLIC_MAX){
                    if (product.getDetailsImages()!=null && product.getDetailsImages().size()>0){
                        detailsDtos.add(ProductInfoBuilder.builderProductDetailsDto(product));
                    }
                }
            }
        }
        return success(detailsDtos);
    }


}
