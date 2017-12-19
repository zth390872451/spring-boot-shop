package com.svlada.endpoint.backstage;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.component.repository.CategoryRepository;
import com.svlada.component.repository.DetailsImageRepository;
import com.svlada.component.repository.MajorImageRepository;
import com.svlada.component.repository.ProductRepository;
import com.svlada.component.service.ProductService;
import com.svlada.config.ConstantConfig;
import com.svlada.endpoint.dto.MarkDto;
import com.svlada.endpoint.dto.ProductInfoDescDto;
import com.svlada.endpoint.dto.builder.ProductInfoBuilder;
import com.svlada.entity.User;
import com.svlada.entity.product.DetailsImage;
import com.svlada.entity.product.MajorImage;
import com.svlada.entity.product.Product;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.common.request.CustomResponseStatus.*;

@RestController
@RequestMapping("/api/back/product")
public class ProductBackEndpoint {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MajorImageRepository majorImageRepository;
    @Autowired
    private DetailsImageRepository detailsImageRepository;
    @Autowired
    private ProductService productService;

    @ApiOperation(value="商品编号录入", notes="商品录入：唯一编号录入")
    @ApiImplicitParams({
    })
    @PostMapping(value = "/add/code")
    public CustomResponse addCode(@RequestBody String code) {
        Product product = productRepository.findOneByCode(code);
        if (product!=null){
            return fail(_40000,"商品编号不能重复!");
        }
        product = new Product();
        product.setCode(code);
        User currentUser = WebUtil.getCurrentUser();
        product.setCreateID(currentUser.getId());
        product.setCreateTime(new Date());
        productRepository.save(product);
        return success();
    }

    /*public CustomResponse addCode(@RequestBody @Valid BasicProductInfoDto dto) {
        Product product = productRepository.findOneByCode(dto.getCode());
        if (product!=null){
            return fail(_40000,"商品编号不能重复!");
        }
        product = new Product();
        product.setCode(dto.getCode());

        if(dto.getCategoryId()!=null && dto.getCategoryId()>0){
            Category category = categoryRepository.findOne(dto.getCategoryId());
            if (category==null){
                return fail(_40000,"categoryId对应类别记录不存在!");
            }
            product.setCategory(category);
        }
        User currentUser = WebUtil.getCurrentUser();
        product.setCreateID(currentUser.getId());
        product.setCreateTime(new Date());
        productRepository.save(product);
        return success();
    }*/


    @ApiOperation(value="商品信息列表", notes="商品信息列表分页")
    @ApiImplicitParams({
    })
    @GetMapping(value = "/list")
    public CustomResponse list(@RequestParam(name = "key",required = false) String key,
                               @PageableDefault(value = 20, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(this.getSpecification(key), pageable);
        Page<ProductInfoDescDto> dtoPage = productPage.map(entity -> {
            ProductInfoDescDto dto = ProductInfoBuilder.builderProductInfoDescDto(entity);
            return dto;
        });
        return success(dtoPage);
    }

    private Specification<Product> getSpecification(String key){
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();
            if(!StringUtils.isEmpty(key)){
                Predicate searchKey = cb.like(root.get("searchKey").as(String.class), "%" + key + "%");
                Predicate name = cb.like(root.get("name").as(String.class), "%" + key + "%");
                predicate.add(cb.or(searchKey,name));
            }
            Predicate[] pre = new Predicate[predicate.size()];
            return query.where(predicate.toArray(pre)).getRestriction();
        };
    }


    @ApiOperation(value="商品基本信息设置[修改]", notes="商品参数设置")
    @ApiImplicitParams({
    })
    @PostMapping(value = "/set/basic")
    public CustomResponse setBasic(@RequestBody @Valid ProductInfoDescDto dto) {
        Product product = null;
        if (StringUtils.isEmpty(dto.getCode())){//新建商品记录
            product = new Product();
            //生成商品编号
            if (!StringUtils.isEmpty(dto.getName())){
                long timeMillis = System.currentTimeMillis();
                product.setCode("code"+timeMillis);
            }else {
                return fail(_40000,"商品名称必须填写!");
            }
        }else {
            product = productRepository.findOneByCode(dto.getCode());
            if (product==null){
                return fail(_40401,"商品不存在!");
            }
        }
        if (!StringUtils.isEmpty(dto.getName())){
            product.setName(dto.getName());
        }
        if (!StringUtils.isEmpty(dto.getTitle())){
            product.setTitle(dto.getTitle());
        }
        if (!StringUtils.isEmpty(dto.getIntroduce())){
            product.setIntroduce(dto.getIntroduce());
        }
        if (!StringUtils.isEmpty(dto.getPrice())){
            product.setPrice(dto.getPrice());
        }
        if (!StringUtils.isEmpty(dto.getNowPrice())){
            product.setNowPrice(dto.getNowPrice());
        }
        if (!StringUtils.isEmpty(dto.getName())){
            product.setSearchKey(dto.getSearchKey());
        }
        if (!StringUtils.isEmpty(dto.getStock())){
            product.setStock(dto.getStock());
        }
        if (!StringUtils.isEmpty(dto.getDescription())){
            product.setDescription(dto.getDescription());
        }
        if (!StringUtils.isEmpty(dto.getStatus())){
            product.setStatus(dto.getStatus());
        }
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        User currentUser = WebUtil.getCurrentUser();
        product.setCreateID(currentUser.getId());
        productRepository.save(product);
        return success();
    }

    @ApiOperation(value="商品基本信息查询", notes="查询商品基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "商品唯一编号", paramType = "path", dataType = "String", required = true),
    })
    @GetMapping("/get/basic/{code}")
    public CustomResponse getBasic(@PathVariable("code") String code) {
        Product product = productRepository.findOneByCode(code);
        if (product==null){
            return fail(_40401,"商品Code对应的商品不存在!");
        }
        ProductInfoDescDto productInfoDescDto = ProductInfoBuilder.builderProductInfoDescDto(product);
        return success(productInfoDescDto);
    }


    @ApiOperation(value="商品图片信息获取", notes="图片信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "商品ID", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "majorImageFiles", value = "商品页面轮播图", paramType = "body", dataType = "file", required = false),
            @ApiImplicitParam(name = "detailImageFiles", value = "商品详情页面图", paramType = "body", dataType = "file", required = false)
    })
    @GetMapping(value = "/get/pic/{code}")
    public CustomResponse getPic(@PathVariable("code") String code) {
        User user = WebUtil.getCurrentUser();
        Product product = productRepository.findOneByCode(code);
        if(product==null){
            return fail(_40401);
        }
        List<MajorImage> majorImages = majorImageRepository.findAllByProductIdOrderById(product.getId());
        List<DetailsImage> detailImages = detailsImageRepository.findAllByProductIdOrderById(product.getId());
        Map<String,Object> result = new HashMap<>();
        result.put("majorImages",majorImages);
        if (detailImages.size()==0){
            DetailsImage detailsImage = new DetailsImage();
            detailsImage.setImageUrl(ConstantConfig.DEFAULT_PIC);
            detailImages.add(detailsImage);
        }
        result.put("detailImages",detailImages);
        return success(result);
    }


   /* @ApiOperation(value="商品图片设置", notes="商品录入：图片信息录入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "商品ID", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "majorImageFiles", value = "商品页面轮播图", paramType = "body", dataType = "file", required = false),
            @ApiImplicitParam(name = "detailImageFiles", value = "商品详情页面图", paramType = "body", dataType = "file", required = false)
    })
    @PostMapping(value = "/set/pic/{code}")
    public CustomResponse setPic(@PathVariable("code") String code,
                                 @RequestParam(value = "majorImageFiles",required = false) MultipartFile[] majorImageFiles,
                                 @RequestParam(value = "detailImageFiles",required = false) MultipartFile[] detailImageFiles) {
        User user = WebUtil.getCurrentUser();
        Product product = productRepository.findOneByCode(code);
        if(product==null){
            return fail(_40401);
        }
        productService.setPicService(majorImageFiles, detailImageFiles, user, product);
        return success();
    }*/

    @ApiOperation(value="商品图片设置", notes="商品录入：图片信息录入")
    @ApiImplicitParams({
//            @ApiImplicitParam(name = "code", value = "商品ID", paramType = "path", dataType = "String", required = true),
            @ApiImplicitParam(name = "majorImageFiles", value = "商品页面轮播图", paramType = "body", dataType = "file", required = false),
            @ApiImplicitParam(name = "detailImageFiles", value = "商品详情页面图", paramType = "body", dataType = "file", required = false)
    })
    @PostMapping(value = "/set/pic/{code}")
    public CustomResponse setPic(@PathVariable("code") String code,
//                                 @RequestParam(value = "majorImageFiles",required = false) MultipartFile[] majorImageFiles,
                                 @RequestBody(required = false) String detailImageFiles,
                                 HttpServletRequest request) {
        User user = WebUtil.getCurrentUser();
        Product product = productRepository.findOneByCode(code);
        if(product==null){
            return fail(_40401);
        }
        productService.setPicService(detailImageFiles, user, product);
        return success();
    }



    @ApiOperation(value="商品营销策略设置", notes="营销策略：是否包邮、卖家强推、新品上市、特价优惠、首页轮询产品")
    /*@ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "商品ID", paramType = "path", dataType = "String", required = true)
    })*/
    @PostMapping(value = "/set/mark")
    public CustomResponse setMark(@RequestBody @Valid MarkDto dto) {
        Product product = productRepository.findOneByCode(dto.getCode());
        if (product==null){
            return fail(_40401,"商品Code对应的商品不存在!");
        }
        if (product!=null){
            if (dto.getMailFree()!=null){
                product.setMailFree(dto.getMailFree());
            }
            if (dto.getNew()!=null){
                product.setNew(dto.getNew());
            }
            if (dto.getRecommend()!=null){
                product.setRecommend(dto.getRecommend());
            }
            if (dto.getSpecialPrice()!=null){
                if (dto.getSpecialPrice() && dto.getNowPrice()!=null){
                    product.setNowPrice(dto.getNowPrice());
                }
                if (dto.getSpecialPrice() && dto.getPrice()!=null){
                    product.setPrice(dto.getPrice());
                }
                product.setSpecialPrice(dto.getSpecialPrice());
            }
            if (dto.getCyclic()!=null){
                if (dto.getCyclic()){
                    //循环滚动最多设置x个产品
                    Long cyclicIsTrue = productRepository.countProductByCyclicIsTrue();
                    if (cyclicIsTrue>=Product.CYCLIC_MAX){//超过了设置的最大值5个
                        return fail(_20002,"循环滚动最多设置5个产品");//循环滚动最多设置5个产品
                    }
                    //查询是否有详情图了(轮询必须有)
                    List<DetailsImage> detailsImages = product.getDetailsImages();
                    if (detailsImages==null || detailsImages.size()==0){
                        return fail(_20003, "循环滚动的商品必须有详情图");
                    }
                }
                product.setCyclic(dto.getCyclic());
            }
            productRepository.save(product);
        }
        return success();
    }

    @ApiOperation(value="商品营销策略获取", notes="营销策略：是否包邮、卖家强推、新品上市、特价优惠")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "商品Code", paramType = "path", dataType = "String", required = true)
    })
    @GetMapping(value = "/get/mark/{code}")
    public CustomResponse getMark(@PathVariable("code") String code) {
        Product product = productRepository.findOneByCode(code);
        if (product==null){
            return fail(_40401,"商品Code对应的商品不存在!");
        }
        MarkDto markDto = ProductInfoBuilder.builderMarkDto(product);
        return success(markDto);
    }

}
