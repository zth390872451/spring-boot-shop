package com.svlada.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "活动信息Dto")
public class ActivityDto implements Serializable{

    @ApiModelProperty(notes="活动ID",required = false)
    private Long id;

    @ApiModelProperty(notes="活动名称")
    @NotNull(message = "不能为空")
    private String name;

    @ApiModelProperty(notes="活动内容描述")
    @NotNull(message = "不能为空")
    private String content;

    @ApiModelProperty(notes="活动对应的商品")
    private Long[] productIds;

    @ApiModelProperty(notes="个人最多购买数量")
    @NotNull(message = "不能为空")
    private Integer maxSaleCount;

    @ApiModelProperty(notes="活动开始时间")
    @NotNull(message = "不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @ApiModelProperty(notes="活动结束时间")
    @NotNull(message = "不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @ApiModelProperty(notes="活动状态,true:正常上线;false:下线过期")
    private Boolean status = true;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long[] getProductIds() {
        return productIds;
    }

    public void setProductIds(Long[] productIds) {
        this.productIds = productIds;
    }

    public Integer getMaxSaleCount() {
        return maxSaleCount;
    }

    public void setMaxSaleCount(Integer maxSaleCount) {
        this.maxSaleCount = maxSaleCount;
    }
}
