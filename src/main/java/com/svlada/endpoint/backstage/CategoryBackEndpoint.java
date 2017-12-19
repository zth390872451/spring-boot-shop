package com.svlada.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.entity.product.Category;
import com.svlada.endpoint.dto.CategoryDto;
import com.svlada.component.repository.CategoryRepository;
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

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.common.request.CustomResponseStatus.*;

@RestController
@RequestMapping("/api/back/category")
public class CategoryBackEndpoint {

    @Autowired
    private CategoryRepository categoryRepository;

    @ApiOperation(value="添加商品的类别条目", notes="新建商品类别")
    @PostMapping(value = "/add")
    public CustomResponse add(@Valid @RequestBody CategoryDto dto) {
        Category category = categoryRepository.findOneByName(dto.getName());
        if (category!=null){
            return fail(_20001,"商品的类别条目名称已存在!");//已存在
        }
        category = new Category();
        category.setDescription(dto.getDescription());
        category.setName(dto.getName());
        if (dto.getParentId()!=null){
            Category parentCategory = categoryRepository.findOne(dto.getParentId());
            if (parentCategory==null){
                return fail(_40000,"parentId对应的父类别不存在!");
            }
            category.setParent(parentCategory);
        }
        categoryRepository.save(category);
        return success(category);
    }

    @ApiOperation(value="查询类别条目", notes="查询商品类别")
    @ApiImplicitParam(name = "id", value = "类别ID",paramType = "path",required = true, dataType = "Long")
    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id){
        Category category = categoryRepository.findOne(id);
        return success(category);
    }

    @ApiOperation(value="修改类别", notes="修改商品类别")
    @PutMapping(value = "/update")
    public CustomResponse update(@RequestBody @Valid CategoryDto dto) {
        Category category = categoryRepository.findOne(dto.getId());
        if (category==null){
            return fail(_40000,"ID对应的类别不存在");
        }
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        categoryRepository.save(category);
        return success();
    }

    @ApiOperation(value="删除类别", notes="只允许删除无子类别且该类别下无商品")
    @ApiImplicitParam(name = "id", value = "类别ID",paramType = "path",required = true, dataType = "Long")
    @DeleteMapping(value = "/delete/{id}")
    public CustomResponse delete(@PathVariable Long id) {
        try {
            Category category= categoryRepository.findOne(id);
            categoryRepository.delete(category);
        }catch (Exception e){
            return fail(_0,"删除失败，该类下有商品或者有子类别");
        }
        return success();
    }

    /**
     * 分页
     * 查询商品类别
     * @return
     */
    @ApiOperation(value="分页查询所有的类别条目", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页参数,默认为0", paramType = "query", dataType = "Int", required = false),
            @ApiImplicitParam(name = "size", value = "每页显示数量,默认为10", paramType = "query", dataType = "Int", required = false),
            @ApiImplicitParam(name = "sort", value = "排序，默认按照id，升序;sort=id,ASC", paramType = "query", dataType = "String", required = false)
    })
    @GetMapping(value = "/list")
    public CustomResponse list(@PageableDefault(value = 10, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return success(categoryPage.getContent());
    }


 /*   @GetMapping(value = "/list/{categoryId}")
    public CustomResponse listCategoryProduct(Pageable pageable) {
        Category category = new Category();

        Page<Category> categoryPage = categoryRepository.findAll(category,pageable);
        return success(categoryPage);
    }
*/

    /**
     * 分页 根据类别查询商品(默认按照销量排序)
     * @return
     */
    /*@GetMapping(value = "/list/{categoryId}")
    public CustomResponse listCategoryProduct(Pageable pageable) {
        Category category = new Category();

        Page<Category> categoryPage = categoryRepository.findAll(category,pageable);
        return success(categoryPage);
    }*/


}
