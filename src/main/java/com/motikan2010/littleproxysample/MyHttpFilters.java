package com.motikan2010.littleproxysample;

import com.motikan2010.littleproxysample.data.ReqResData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.littleshoot.proxy.HttpFilters;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyHttpFilters implements HttpFilters {

    private final boolean CLIENT_TO_PROXY_REQUEST = false;
    private final boolean PROXY_TO_SERVER_REQUEST_ENABLE = true;
    private final boolean SERVER_TO_PROXY_RESPONSE_ENABLE = true;
    private final boolean PROXY_TO_CLIENT_RESPONSE_ENABLE = false;

    // クライアント -> プロキシ
    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        if(CLIENT_TO_PROXY_REQUEST) {
            showRequest(httpObject);
        }

        return null;
    }

    // プロキシ -> サーバ
    @Override
    public HttpResponse proxyToServerRequest(HttpObject httpObject) {
        if (httpObject instanceof HttpRequest) {
            ((HttpRequest) httpObject).headers().remove("Via");
        }
        if(PROXY_TO_SERVER_REQUEST_ENABLE) {
            showRequest(httpObject);
        }
        return null;
    }

    // サーバ -> プロキシ
    @Override
    public HttpObject serverToProxyResponse(HttpObject httpObject) {
        if(SERVER_TO_PROXY_RESPONSE_ENABLE) {
            showResponse(httpObject);
        }
        return httpObject;
    }

    // プロキシ -> クライアント
    @Override
    public HttpObject proxyToClientResponse(HttpObject httpObject) {
        if(httpObject instanceof HttpResponse) {
            ((HttpResponse) httpObject).headers().remove("Via");
        }
        if(PROXY_TO_CLIENT_RESPONSE_ENABLE) {
            showResponse(httpObject);
        }
        return httpObject;
    }

    private void showRequest(HttpObject httpObject) {
        if (httpObject instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) httpObject;

            MainApplication.addReqResRow(Calendar.getInstance(), httpRequest.getMethod().name(), httpRequest.getUri());

            /*
            // メソッド
            System.out.print(httpRequest.getMethod() + " ");

            // URI
            System.out.print(httpRequest.getUri() + " ");

            // HTTPバージョン
            System.out.println(httpRequest.getProtocolVersion());

            // ヘッダー
            HttpHeaders httpHeaders = httpRequest.headers();
            List<Map.Entry<String,String>> headerList = httpHeaders.entries();
            for (Map.Entry<String, String> header : headerList) {
                System.out.println(header.getKey() + ": " + header.getValue());
            }
            */
        }

        if (httpObject instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) httpObject;
            showContent(httpContent);
        }
        System.out.println();
    }

    private void showResponse(HttpObject httpObject){
        // レスポンスヘッダ
        if (httpObject instanceof HttpResponse) {
            System.out.print(((HttpResponse) httpObject).getProtocolVersion() + " ");

            System.out.println(((HttpResponse) httpObject).getStatus());

            // ヘッダー
            HttpHeaders httpHeaders = ((HttpResponse) httpObject).headers();
            List<Map.Entry<String,String>> headerList = httpHeaders.entries();
            for (Map.Entry<String, String> header : headerList) {
                System.out.println(header.getKey() + ": " + header.getValue());
            }
        }

        // レスポンスボディ
        if (httpObject instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) httpObject;
            showContent(httpContent);
        }
        System.out.println();
    }

    private void showContent(HttpContent httpContent){
        // String responseBody = httpContent.content().toString(Charset.defaultCharset());
        // System.out.println();
        // System.out.println(responseBody);
    }

    @Override
    public void serverToProxyResponseTimedOut() {}

    @Override
    public void serverToProxyResponseReceiving() {}

    @Override
    public void serverToProxyResponseReceived() {}

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
    public void proxyToServerConnectionSucceeded(ChannelHandlerContext serverCtx) {}

    @Override
    public void proxyToServerConnectionStarted() {}

    @Override
    public void proxyToServerConnectionSSLHandshakeStarted() {}

    @Override
    public void proxyToServerConnectionQueued() {}

    @Override
    public void proxyToServerConnectionFailed() {}

}
