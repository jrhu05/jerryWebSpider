package com.hytcshare.jerrywebspider.controller;

import com.hytcshare.jerrywebspider.enums.ServerStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

/**
 * 基础controller
 *
 * @author jerryfu
 */
@Controller
@Slf4j
class BaseController {

    void sealSuccess(DeferredResult deferredResult, Object data) {
        //Map<String, Object> resultMap = ImmutableMap.of("code", ServerStatusEnum.OK.getCode(), "msg", "", "data", data);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", ServerStatusEnum.OK.getCode());
        resultMap.put("msg", "");
        resultMap.put("data", data);
        deferredResult.setResult(resultMap);
    }

    void sealFail(DeferredResult deferredResult, String msg) {
//        Map<String, Object> resultMap = ImmutableMap.of("code", ServerStatusEnum.ERROR.getCode(), "msg", msg, "data",
//                null);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", ServerStatusEnum.ERROR.getCode());
        resultMap.put("msg", msg);
        resultMap.put("data", null);
        deferredResult.setResult(resultMap);
    }


}
