package com.ec.gateway.filter.auth;


import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ec.commons.constant.RedisKeyConstant;
import com.ec.commons.entities.bo.account.AccountBO;
import com.ec.commons.filter.log.LogInfo;
import com.ec.commons.filter.log.LogInfoType;
import com.ec.commons.util.IpUtil;
import com.ec.commons.util.StringUtilExtend;
import com.ec.commons.util.ret.R;
import com.ec.gateway.schedules.LogPushSchedule;
import com.edward.redis.template.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 网关统一的token验证
 *
 * @author edward
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;
    private RedisUtil redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        log(exchange.getRequest(), "gain", LogInfoType.GATEWAY_INFO);
        // 0. 如果未启用网关验证，则跳过
        if (!authProperties.getEnable()) {
            return chain.filter(exchange);
        }

        // 1. 获取请求
        ServerHttpRequest request = exchange.getRequest();

        //　3. 如果在忽略的url里，则跳过
        String path = request.getURI().getPath();
        String requestUrl = request.getURI().getRawPath();
        if (ignore(path) || ignore(requestUrl)) {
            return chain.filter(exchange);
        }

        // 4. 获取请求头
        HttpHeaders headers = request.getHeaders();
        // 5. 请求头中获取令牌
        String token = headers.getFirst("token");

        // 6. 判断请求头中是否有令牌
        if (StrUtil.isBlank(token)) {
            // 授权失败
            return unauthorized(exchange, "UNAUTHORIZED");
        }

        // ======= 验证token是否有效 =======
        //9. 如果请求头中有令牌则解析令牌
        try {

            // redis 校验token有效性
            String at = StringUtilExtend.Trim(
                    redisTemplate.get(RedisKeyConstant.AUTH_TOKEN + token));
            if (!"".equals(at)) {
                AccountBO account = JSONUtil.toBean(at, AccountBO.class);
                //可以不用再验证token有效性。由redis控制即可
                //JwtUtil.verify(at,account.getName(),account.getPwd());

                // 构建请求头传递给后续微服务
                ServerHttpRequest.Builder requestBuilder = request.mutate();
                requestBuilder.headers(k -> k.set("nativeId", account.getNativeId().toString()));

                ServerHttpRequest newRequest = requestBuilder.build();
                exchange.mutate().request(newRequest).build();

                //12. 鉴权通过。放行。
                return chain.filter(exchange);
            } else {
                return unauthorized(exchange, "UNAUTHORIZED。" + "token过期或不存在！");
            }

        } catch (Exception e) {
            // 授权失败
            return unauthorized(exchange, "UNAUTHORIZED。" + e.getMessage());
        }

    }

    /**
     * 检查是否忽略url
     *
     * @param path 路径
     * @return boolean
     */
    private boolean ignore(String path) {
        return authProperties.getIgnoreUrl().stream()
                .map(url -> url.replace("/**", ""))
                .anyMatch(path::startsWith);
    }


    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange, String msg) {
        R data = R.fail(401, msg);
        DataBuffer buffer =
                serverWebExchange.getResponse().bufferFactory().wrap(
                        JSONUtil.toJsonStr(data).getBytes(StandardCharsets.UTF_8));
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private void log(ServerHttpRequest request,
                     String msg,
                     String type) {
        LogInfo info = new LogInfo();
        info.setMethod(request.getMethodValue());
        info.setIp(IpUtil.getRealIpAddress(request));
        info.setMsg(msg);
        info.setUrl(request.getURI().getPath());
        info.setType(type);
        info.setQueryParams(request.getQueryParams().toString());
        info.setHeaders(request.getHeaders().toString());
        LogPushSchedule.logQueue.add(JSONUtil.parse(info));
    }

}
