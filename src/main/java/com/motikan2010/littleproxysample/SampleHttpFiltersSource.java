package com.motikan2010.littleproxysample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSource;

public class SampleHttpFiltersSource implements HttpFiltersSource {

    @Override
    public int getMaximumResponseBufferSizeInBytes() {
        return 10240 * 10240;
    }

    @Override
    public int getMaximumRequestBufferSizeInBytes() {
        return 10240 * 10240;
    }

    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        if (originalRequest.getMethod() == HttpMethod.CONNECT) {
            return new HttpFiltersAdapter(originalRequest, ctx);
        }
        return new MyHttpFilters();
    }


}
