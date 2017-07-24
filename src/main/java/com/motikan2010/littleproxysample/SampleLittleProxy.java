package com.motikan2010.littleproxysample;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import io.netty.handler.codec.http.*;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSource;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;


public class SampleLittleProxy {

    private static final int PORT = 8080;

    public static void main(String[] args) {

        SampleHttpFiltersSource sampleHttpFiltersSource = new SampleHttpFiltersSource();

        DefaultHttpProxyServer.bootstrap()
                .withPort(PORT)
                .withFiltersSource(sampleHttpFiltersSource)
                .withAllowLocalOnly(false)
                .withName("FilteringProxy")
                .start();
    }


    static class SampleHttpFiltersSource implements HttpFiltersSource{

        @Override
        public int getMaximumResponseBufferSizeInBytes() {
            return 10240 * 10240;
        }

        @Override
        public int getMaximumRequestBufferSizeInBytes() {
            return 10240 * 10240;
        }

        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {

            return new HttpFilters() {

                @Override
                public void serverToProxyResponseTimedOut() {}

                @Override
                public void serverToProxyResponseReceiving() {}

                @Override
                public void serverToProxyResponseReceived() {}

                @Override
                public HttpObject serverToProxyResponse(HttpObject httpObject) {
                    System.out.println("=== serverToProxyResponse ===");

                    // レスポンスヘッダ
                    if (httpObject instanceof HttpResponse) {
                        HttpResponse httpResponse = (HttpResponse) httpObject;
                        System.out.println(httpResponse.toString());
                        System.out.println();
                    }

                    // レスポンスボディ
                    if (httpObject instanceof HttpContent) {
                        HttpContent httpContent = (HttpContent) httpObject;
                        String resposeBody = httpContent.content().toString(Charset.defaultCharset());
                        System.out.println(resposeBody);
                    }

                    System.out.println("");
                    return httpObject;
                }

                @Override
                public void proxyToServerResolutionSucceeded(String serverHostAndPort, InetSocketAddress resolvedRemoteAddress) {}

                @Override
                public InetSocketAddress proxyToServerResolutionStarted(String resolvingServerHostAndPort) {
                    return null;
                }

                @Override
                public void proxyToServerResolutionFailed(String hostAndPort) {}

                @Override
                public void proxyToServerRequestSent() {}

                @Override
                public void proxyToServerRequestSending() {}

                @Override
                public HttpResponse proxyToServerRequest(HttpObject httpObject) {
                    System.out.println("=== proxyToServerRequest ===");
                    if (httpObject instanceof HttpRequest) {
                        System.out.println(httpObject.toString());
                    }
                    System.out.println("");
                    return null;
                }

                @Override
                public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {}

                @Override
                public void proxyToServerConnectionStarted() {}

                @Override
                public void proxyToServerConnectionSSLHandshakeStarted() {}

                @Override
                public void proxyToServerConnectionQueued() {}

                @Override
                public void proxyToServerConnectionFailed() {}

                @Override
                public HttpObject proxyToClientResponse(HttpObject httpObject) {
                    System.out.println("=== proxyToClientResponse ===");

                    // レスポンスヘッダ
                    if (httpObject instanceof HttpResponse) {
                        HttpResponse httpResponse = (HttpResponse) httpObject;
                        System.out.println(httpResponse.toString());
                        System.out.println();
                    }

                    // レスポンスボディ
                    if (httpObject instanceof HttpContent) {
                        HttpContent httpContent = (HttpContent) httpObject;
                        String resposeBody = httpContent.content().toString(Charset.defaultCharset());
                        System.out.println(resposeBody);
                    }

                    System.out.println("");
                    return httpObject;
                }

                @Override
                public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                    System.out.println("=== clientToProxyRequest ===");
                    if (httpObject instanceof HttpRequest) {
                        System.out.println(httpObject.toString());
                    }
                    System.out.println("");
                    return null;
                }

            };
        }
    }

}

