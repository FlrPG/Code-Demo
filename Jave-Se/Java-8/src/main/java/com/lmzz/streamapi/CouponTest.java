package com.lmzz.streamapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CouponTest {

    public static void main(String[] args) throws JsonProcessingException {
        List<CouponResponse> coupons = getCoupons();
        //对优惠券统计数量
        Map<Long, CouponInfo> collect = coupons.stream().collect(Collectors.toMap(
                CouponResponse::getId,
                resp -> {
                    CouponInfo info = new CouponInfo();
                    info.setId(resp.getId());
                    info.setNum(1);
                    info.setName(resp.getName());
                    info.setCondition(resp.getCondition());
                    info.setDenominations(resp.getDenominations());
                    return info;
                },
                (pre, next) -> {
                    pre.setNum(pre.getNum() + 1);
                    return pre;
                }));

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(collect.values()));
    }

    private static List<CouponResponse> getCoupons() {
        return Arrays.asList(
                new CouponResponse(1L, "满5减4", 500L, 400L),
                new CouponResponse(1L, "满5减4", 500L, 400L),
                new CouponResponse(2L, "满10减9", 1000L, 900L),
                new CouponResponse(3L, "满60减50", 6000L, 5000L)
        );
    }

    @Data
    @AllArgsConstructor
    static class CouponResponse {
        private Long id;
        private String name;
        private Long condition;
        private Long denominations;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CouponResponse that = (CouponResponse) o;

            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CouponInfo {
        private Long id;
        private String name;
        private Integer num;
        private Long condition;
        private Long denominations;
    }
}