package com.sharescrow.sample.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharescrow.sample.model.Product;
import com.sharescrow.sample.model.response.BaseResponse;
import com.sharescrow.sample.service.ProductService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController extends BaseRestController{
  @Autowired
  private ProductService productService;

  @GetMapping("")
  public BaseResponse<Product> getProducts(
    @RequestParam(value = "offset", defaultValue = "0") int offset
    , @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
  ) {
    return new BaseResponse<>(productService.getProducts(offset, pageSize));
  }

  @GetMapping("/{productNo}")
  public BaseResponse<Product> getProductByProductNo(
    @PathVariable("productNo") long productNo
  ) {
    return new BaseResponse<>(productService.getProductByProductNo(productNo));
  }

  @PostMapping("/create")
  public BaseResponse<Product> createProduct(
    @RequestBody Product product
  ) {
    // note: 이 코드엔 문제가 있습니다. 생각 해보시면 좋을것  같아요!
    return new BaseResponse<>(productService.createProduct(product));
  }

  @PostMapping("/{productNo}/edit")
  public BaseResponse<Product> editProduct(
    @PathVariable long productNo
    , @RequestBody Product product
  ) {
    return new BaseResponse<>(productService.editProduct(productNo, product));
  }
}
