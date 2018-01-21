package com.motikan2010.littleproxysample.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReqResData {

    private String date;

    private String method;

    private String uri;
}
