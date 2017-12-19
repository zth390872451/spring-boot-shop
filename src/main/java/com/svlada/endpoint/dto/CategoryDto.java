package com.svlada.endpoint.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@ApiModel(description = "类别信息DTO")
public class CategoryDto implements Serializable{

    @ApiModelProperty(notes="类别id，查询不为null")
    private Long id;

    @ApiModelProperty(notes="类别名字,唯一活动名称",required = true)
    @NotBlank(message = "不能为空或者空串")
    private String name;

    @ApiModelProperty(notes="类别描述，不为null")
    @NotBlank(message = "不能为空或者空串")
    private String description;

    @ApiModelProperty(notes="父类别id")
    private Long parentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
