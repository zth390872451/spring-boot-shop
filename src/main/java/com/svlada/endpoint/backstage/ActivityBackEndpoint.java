package com.svlada.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.entity.Activity;
import com.svlada.entity.product.Product;
import com.svlada.endpoint.dto.ActivityDto;
import com.svlada.component.repository.ActivityRepository;
import com.svlada.component.repository.CategoryRepository;
import com.svlada.component.repository.ProductRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("api/back/activity")
public class ActivityBackEndpoint {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ActivityRepository activityRepository;

    /**
     * 添加活动:并添加一系列对应的产品
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "添加活动", notes = "")
    @ApiImplicitParam(name = "dto", value = "活动信息", paramType = "body", required = true, dataType = "ActivityDto")
    @PostMapping(value = "/add")
    public CustomResponse add(@RequestBody @Valid ActivityDto dto) {
        Activity activity = new Activity();
        activity.setName(dto.getName());
        activity.setContent(dto.getContent());
        activity.setMaxSaleCount(dto.getMaxSaleCount());
        activity.setStartDate(dto.getStartDate());
        activity.setEndDate(dto.getEndDate());
        activity.setStatus(Activity.STATUS_NORMAL);
        activityRepository.save(activity);
        return success();
    }


    @ApiOperation(value = "查询编号对应的商品是否存在", notes = "存在返回true")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "商品编号", paramType = "path", required = true, dataType = "String"),
    })
    @GetMapping(value = "/{code}")
    public CustomResponse code(@PathVariable("code") String code
    ) {
        Product product = productRepository.findOneByCode(code);
        return success(product != null ? true : false);
    }


    @ApiOperation(value = "添加享受活动的产品", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动ID", paramType = "path", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "codes", value = "商品编号列表", paramType = "body", required = true, dataType = "array")
    })
    @PostMapping(value = "/{id}/addProducts")
    public CustomResponse addProducts(@PathVariable("id") Long id,
                                      @RequestBody @Valid String[] codes) {
        Activity activity = activityRepository.findOne(id);
        if (activity == null) {
            return fail(CustomResponseStatus._40000, "活动ID对应的记录不存在!");
        }
        List<Product> productList = new ArrayList<>();
        for (String code : codes) {
            Product product = productRepository.findOneByCode(code);
            if (product != null) {
                product.setActivity(activity);
                productList.add(product);
            }
        }
        productRepository.save(productList);
        return success();
    }

    /**
     * 活动下线，则对应的所有的产品都下线
     * 修改活动信息:状态 内容 名字 日期
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "更新活动信息", notes = "状态[上下线]、内容、名字、日期[起止时间]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动ID", paramType = "query", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "productIds", value = "商品ID列表", paramType = "body", required = true, dataType = "array")
    })
    @PostMapping(value = "/update")
    public CustomResponse update(@RequestBody @Valid ActivityDto dto) {
        Activity activity = activityRepository.findOne(dto.getId());
        if (dto.getStatus() != null) {
            activity.setStatus(dto.getStatus());
        }
        if (dto.getContent() != null) {
            activity.setContent(dto.getContent());
        }
        if (dto.getName() != null) {
            activity.setName(dto.getName());
        }
        if (activity.getMaxSaleCount() != null && activity.getMaxSaleCount() > 0) {
            activity.setMaxSaleCount(dto.getMaxSaleCount());
        }
        if (dto.getStartDate() != null) {
            activity.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            activity.setEndDate(dto.getEndDate());
        }
        activityRepository.save(activity);
        return success();
    }



    @ApiOperation(value = "分页查询活动信息", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页参数,默认为0", paramType = "query", dataType = "Int", required = false),
            @ApiImplicitParam(name = "size", value = "每页显示数量,默认为10", paramType = "query", dataType = "Int", required = false),
            @ApiImplicitParam(name = "sort", value = "排序，默认按照id，升序;sort=id,ASC", paramType = "query", dataType = "String", required = false)
    })
    @GetMapping(value = "/list")
    public CustomResponse list(@PageableDefault(value = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Activity> page = activityRepository.findAll(pageable);
        return success(page.getContent());
    }

    @GetMapping(value = "/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id) {
        Activity activity= activityRepository.findOne(id);
        if (activity==null){
            return fail(CustomResponseStatus._40401,"活动ID对应的记录不存在!");
        }
        return success();
    }




}
