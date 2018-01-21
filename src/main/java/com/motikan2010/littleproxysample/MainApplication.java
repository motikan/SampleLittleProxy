package com.motikan2010.littleproxysample;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.motikan2010.littleproxysample.data.ReqResData;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.lightbody.bmp.mitm.KeyStoreFileCertificateSource;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

public class MainApplication extends Application {

    private static final int PORT = 8080;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:dd";

    private static HttpProxyServer httpProxyServer = null;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final ObservableList<ReqResData> reqResDataObservableList = FXCollections.observableArrayList();

    public static void main(String[] args) {
        // load the root certificate and private key from an existing KeyStore
        KeyStoreFileCertificateSource fileCertificateSource = new KeyStoreFileCertificateSource(
                "PKCS12",       // KeyStore type. for .jks files (Java KeyStore), use "JKS"
                new File("sslkey/keystore.p12"),
                "mykey",    // alias of the private key in the KeyStore; if you did not specify an alias when creating it, use "1"
                "keypass");

        ImpersonatingMitmManager mitmManager = ImpersonatingMitmManager.builder()
                .rootCertificateSource(fileCertificateSource)
                .build();

        httpProxyServer = DefaultHttpProxyServer.bootstrap()
                .withPort(PORT)
                .withFiltersSource(new SampleHttpFiltersSource())
                .withManInTheMiddle(mitmManager)
                .withAllowLocalOnly(false)
                .withName("FilteringProxy")
                .start();


        // GUI起動
        launch(args);
    }

    /**
     * GUI起動
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        HBox hBox = new HBox();

        // 列
        TableView<ReqResData> reqResDataTableView = new TableView<>();
        TableColumn<ReqResData, String> dateCol = new TableColumn<>("時間");
        TableColumn<ReqResData, String> methodCol = new TableColumn<>("メソッド");
        TableColumn<ReqResData, String> uriCol = new TableColumn<>("URI");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        methodCol.setCellValueFactory(new PropertyValueFactory<>("method"));
        uriCol.setCellValueFactory(new PropertyValueFactory<>("uri"));
        reqResDataTableView.getColumns().addAll(dateCol, methodCol, uriCol);
        reqResDataTableView.itemsProperty().setValue(reqResDataObservableList);

        hBox.getChildren().addAll(reqResDataTableView);
        stage.setScene(new Scene(hBox));
        stage.setOnCloseRequest(event -> closeWindow());
        stage.show();
    }

    /**
     * ウィンドウの終了
     */
    private static void closeWindow() {
        httpProxyServer.abort();
    }

    /**
     * リクエスト列の追加
     *
     * @param calendar
     * @param method
     * @param uri
     */
    static void addReqResRow(Calendar calendar, String method, String uri) {
        reqResDataObservableList.add(new ReqResData(simpleDateFormat.format(calendar.getTime()), method, uri));
    }
}