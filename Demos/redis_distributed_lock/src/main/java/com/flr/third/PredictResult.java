package com.flr.third;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方返回值
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictResult {
    /**
     * 解析结果
     */
    private String resultJson;
    /**
     * 解析状态
     * 0 初始化
     * 1 处理中
     * 2 调用成功
     * 3 调用失败
     */
    private Integer status;
}