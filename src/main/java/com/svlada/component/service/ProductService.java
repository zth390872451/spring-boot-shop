package com.svlada.component.service;

import com.svlada.common.utils.FileUploadUtils;
import com.svlada.component.repository.DetailsImageRepository;
import com.svlada.component.repository.MajorImageRepository;
import com.svlada.component.repository.ProductRepository;
import com.svlada.entity.User;
import com.svlada.entity.product.DetailsImage;
import com.svlada.entity.product.MajorImage;
import com.svlada.entity.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MajorImageRepository majorImageRepository;
    @Autowired
    private DetailsImageRepository detailsImageRepository;

    public void setPicService(@RequestParam(value = "majorImageFiles", required = false) MultipartFile[] majorImageFiles, @RequestParam(value = "detailImageFiles", required = false) MultipartFile[] detailImageFiles, User user, Product product) {
        String uuid = UUID.randomUUID().toString();
        String path = /*product.getName() + File.separator+*/ uuid + File.separator;
        if (!StringUtils.isEmpty(majorImageFiles)){
            majorImageRepository.deleteAllByProductId(product.getId());
            List<String> filePaths = FileUploadUtils.saveCommonFile(majorImageFiles, path);
            List<MajorImage> majorImages = filePaths.stream().map(imageUrl -> new MajorImage(product, imageUrl)).collect(Collectors.toList());
            List<MajorImage> images = product.getMajorImages();
            images.addAll(majorImages);
            majorImageRepository.save(majorImages);
            product.setMajorImages(images);
        }
        if (!StringUtils.isEmpty(detailImageFiles)){
            detailsImageRepository.deleteAllByProductId(product.getId());
            List<String> detailImagesPaths = FileUploadUtils.saveCommonFile(detailImageFiles, path);
            List<DetailsImage> detailImages = detailImagesPaths.stream().map(imageUrl -> new DetailsImage(product, imageUrl)).collect(Collectors.toList());
            List<DetailsImage> images = product.getDetailsImages();
            images.addAll(detailImages);
            detailsImageRepository.save(detailImages);
            product.setDetailsImages(images);
        }
        product.setCreateID(user.getId());
        product.setCreateTime(new Date());
        productRepository.save(product);
    }
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public void setPicService(String detailImageFiles, User user, Product product) {
        String uuid = UUID.randomUUID().toString();
        String path = uuid;
        if (!StringUtils.isEmpty(detailImageFiles)){
            detailsImageRepository.deleteAllByProductId(product.getId());
            String detailImagesPath = FileUploadUtils.upload(detailImageFiles, path);
            if (detailImagesPath!=null){
                List<String> detailImagesPaths = new ArrayList<String>();
                detailImagesPaths.add(detailImagesPath);
                List<DetailsImage> detailImages = detailImagesPaths.stream().map(imageUrl -> new DetailsImage(product, imageUrl)).collect(Collectors.toList());
                List<DetailsImage> images = product.getDetailsImages();
                images.addAll(detailImages);
                detailsImageRepository.save(detailImages);
                product.setDetailsImages(images);
            }else {
             log.error("上传图片失败！");
            }
        }
        product.setCreateID(user.getId());
        product.setCreateTime(new Date());
        productRepository.save(product);
    }
}
