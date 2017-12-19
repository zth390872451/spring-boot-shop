package com.svlada.endpoint.font;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.component.repository.AddressRepository;
import com.svlada.component.service.AddressService;
import com.svlada.endpoint.dto.AddressDto;
import com.svlada.entity.Address;
import com.svlada.entity.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/address")
public class AddressEndpoint {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "添加收货地址", notes = "")
    @ApiImplicitParam(name = "dto", value = "地址信息", paramType = "body", required = true, dataType = "AddressDto")
    @PostMapping("/add")
    public CustomResponse add(@RequestBody AddressDto dto) {
        User user = WebUtil.getCurrentUser();
        Address address = new Address();
        if (!StringUtils.isEmpty(dto.getName())) {
            address.setName(dto.getName());
        }
        if (!StringUtils.isEmpty(dto.getAddress())) {
            address.setAddress(dto.getAddress());
        }
        if (!StringUtils.isEmpty(dto.getArea())) {
            address.setArea(dto.getArea());
        }
        if (!StringUtils.isEmpty(dto.getCity())) {
            address.setCity(dto.getCity());
        }
        if (!StringUtils.isEmpty(dto.getMobile())) {
            address.setMobile(dto.getMobile());
        }
        if (!StringUtils.isEmpty(dto.getPhone())) {
            address.setPhone(dto.getPhone());
        }
        if (!StringUtils.isEmpty(dto.getPcaDetail())) {
            address.setPcaDetail(dto.getPcaDetail());
        }
        if (!StringUtils.isEmpty(dto.getProvince())) {
            address.setProvince(dto.getProvince());
        }
        if (!StringUtils.isEmpty(dto.getDefault())) {
            if (dto.getDefault()) {//将新添加的地址设置为默认地址,则现将原有的默认地址修改为非默认地址
                Address defaultAddress = addressRepository.findOneByUserIdAndIsDefault(user.getId(),true);
                if (defaultAddress != null) {//已经有默认地址
                    defaultAddress.setDefault(false);//
                    addressRepository.save(defaultAddress);
                }
            }
            address.setDefault(dto.getDefault());
        }
        if (!StringUtils.isEmpty(dto.getZip())) {
            address.setZip(dto.getZip());
        }
        address.setUser(user);
        addressRepository.save(address);
        return success();
    }

    @ApiOperation(value = "编辑收货地址", notes = "")
    @ApiImplicitParam(name = "dto", value = "收货地址", paramType = "body", required = true, dataType = "AddressDto")
    @PutMapping("/update")
    public CustomResponse update(@RequestBody AddressDto dto) {
        User user = WebUtil.getCurrentUser();
        Address address = addressRepository.findOne(dto.getId());
        if (address == null) {
            return fail(CustomResponseStatus._40400, "地址ID对应的记录不存在!");
        }
        if (!StringUtils.isEmpty(dto.getAddress())) {
            address.setAddress(dto.getAddress());
        }
        if (!StringUtils.isEmpty(dto.getArea())) {
            address.setArea(dto.getArea());
        }
        if (!StringUtils.isEmpty(dto.getCity())) {
            address.setCity(dto.getCity());
        }
        if (!StringUtils.isEmpty(dto.getDefault())) {
            if (dto.getDefault()) {//将新添加的地址设置为默认地址,则现将原有的默认地址修改为非默认地址
                Address defaultAddress = addressRepository.findOneByUserIdAndIsDefault(user.getId(),true);
                if (defaultAddress != null) {//已经有默认地址
                    defaultAddress.setDefault(false);
                    addressRepository.save(defaultAddress);
                }
            }
            address.setDefault(dto.getDefault());
        }
        if (!StringUtils.isEmpty(dto.getMobile())) {
            address.setMobile(dto.getMobile());
        }
        if (!StringUtils.isEmpty(dto.getName())) {
            address.setName(dto.getName());
        }
        if (!StringUtils.isEmpty(dto.getPhone())) {
            address.setPhone(dto.getPhone());
        }
        if (!StringUtils.isEmpty(dto.getPcaDetail())) {
            address.setPcaDetail(dto.getPcaDetail());
        }
        if (!StringUtils.isEmpty(dto.getProvince())) {
            address.setProvince(dto.getProvince());
        }
        if (!StringUtils.isEmpty(dto.getZip())) {
            address.setZip(dto.getZip());
        }
        addressRepository.save(address);
        return success();
    }



    @ApiOperation(value = "删除收货地址", notes = "")
    @ApiImplicitParam(name = "id", value = "地址记录ID", paramType = "path", required = true, dataType = "Long")
    @DeleteMapping("/delete/{id}")
    public CustomResponse delete(@PathVariable("id") Long id) {
        User user = WebUtil.getCurrentUser();
        addressService.deleteAddress(id, user.getId());
        return success();
    }


    @ApiOperation(value = "获取收货地址详情", notes = "")
    @ApiImplicitParam(name = "id", value = "地址记录ID", paramType = "path", required = true, dataType = "Long")
    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id) {
        User user = WebUtil.getCurrentUser();
        Address address = addressRepository.findOneByIdAndUserId(id, user.getId());
        return success(address);
    }

    @ApiOperation(value = "获取用户所有的收货地址", notes = "")
    @GetMapping("/list")
    public CustomResponse list() {
        User user = WebUtil.getCurrentUser();
        List<Address> addresses= addressRepository.findAllByUserId(user.getId());
        return success(addresses);
    }

}
