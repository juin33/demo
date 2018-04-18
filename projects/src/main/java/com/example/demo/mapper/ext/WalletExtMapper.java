package com.example.demo.mapper.ext;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WalletExtMapper {
    Integer isWalletExistOrNot(Integer userId);
}