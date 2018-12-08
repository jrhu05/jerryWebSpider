package com.hytcshare.jerrywebspider.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hytcshare.jerrywebspider.entity.SpiderTask;
import com.hytcshare.jerrywebspider.entity.TuwanImages;
import com.hytcshare.jerrywebspider.entity.TuwanMp3;
import com.hytcshare.jerrywebspider.enums.ExceptionEnum;
import com.hytcshare.jerrywebspider.enums.SpiderTaskStatusEnum;
import com.hytcshare.jerrywebspider.exception.SpiderException;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.service.TuwanImagesService;
import com.hytcshare.jerrywebspider.service.TuwanMp3Service;
import com.hytcshare.jerrywebspider.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.*;

@Component
@Slf4j
public class TuwanSpiderTask implements Runnable {
    private String welfareUrl;
    private TuwanImagesService tuwanImagesService;
    private TuwanMp3Service tuwanMp3Service;
    private SpiderTaskService spiderTaskService;

    private int tuwanStart;
    private Integer endLine;

    private String tuwanSpiderTaskName;


    public void setSpiderTaskService(SpiderTaskService spiderTaskService) {
        this.spiderTaskService = spiderTaskService;
    }

    public void setTuwanSpiderTaskName(String tuwanSpiderTaskName) {
        this.tuwanSpiderTaskName = tuwanSpiderTaskName;
    }


    public void setTuwanStart(int tuwanStart) {
        this.tuwanStart = tuwanStart;
    }

    public void setTuwanImagesService(TuwanImagesService tuwanImagesService) {
        this.tuwanImagesService = tuwanImagesService;
    }

    public void setTuwanMp3Service(TuwanMp3Service tuwanMp3Service) {
        this.tuwanMp3Service = tuwanMp3Service;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }

    public void setWelfareUrl(String welfareUrl) {
        this.welfareUrl = welfareUrl;
    }

    @Override
    public void run() {
        log.info("tuwan spider start！");
        SpiderTask tuwanSpiderTask = spiderTaskService.getTaskByName(tuwanSpiderTaskName);
        if (tuwanSpiderTask == null || StringUtils.isEmpty(tuwanSpiderTask.getTaskName())) {
            //建立新任务
            tuwanSpiderTask = new SpiderTask();
            tuwanSpiderTask.setTaskName(tuwanSpiderTaskName);
            tuwanSpiderTask.setStatus(SpiderTaskStatusEnum.ONGOING.getCode());
            spiderTaskService.saveOrUpdate(tuwanSpiderTask);
        } else if (tuwanSpiderTask.getStatus() == SpiderTaskStatusEnum.ONGOING.getCode()) {
            //已经有在执行的任务
            return;
        }
        //置为正在执行
        tuwanSpiderTask.setStatus(SpiderTaskStatusEnum.ONGOING.getCode());
        spiderTaskService.saveOrUpdate(tuwanSpiderTask);
        //获取图片压缩包和MP3地址
        List<TuwanImages> tuwanImagesList = new ArrayList<>();
        List<TuwanMp3> tuwanMp3List = new ArrayList<>();
        if (endLine == null || endLine < tuwanStart + 1) {
            throw new SpiderException(ExceptionEnum.ENDLINE_ILLEGAL.getCode(), ExceptionEnum.ENDLINE_ILLEGAL.getDes());
        }
        log.info("endLine:" + endLine);
        for (int i = tuwanStart; i <= endLine; i++) {
            try {
                Thread.sleep((long) Math.random() * 3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getImagesAndMp3(tuwanImagesList, tuwanMp3List, i);
        }
        //更新任务状态为执行完毕
        tuwanSpiderTask.setStatus(SpiderTaskStatusEnum.SHUTDOWN.getCode());
        spiderTaskService.saveOrUpdate(tuwanSpiderTask);
        log.info("tuwan spider task finish!");
    }

    private void insertIntoDb(List<TuwanImages> tuwanImagesList, List<TuwanMp3> tuwanMp3List) {
        for (TuwanImages tuwanImages : tuwanImagesList) {
            tuwanImagesService.insertOrUpdate(tuwanImages);
        }
        for (TuwanMp3 tuwanMp3 : tuwanMp3List) {
            tuwanMp3Service.insertOrUpdate(tuwanMp3);
        }
    }

    private void getImagesAndMp3(List<TuwanImages> tuwanImagesList, List<TuwanMp3> tuwanMp3List, int index) {
        //Map<String, String> params = new HashMap<>();
        //sealTuwanRequestParams(params,index);
        //发起请求
        //String tuwanResult = HttpUtils.postForm(welfareUrl, params);
        String tuwanResult = HttpUtils.get(welfareUrl + "?id=" + index);
        tuwanResult = tuwanResult.substring(1, tuwanResult.length() - 1);
        //log.info(tuwanResult);
        if (tuwanResult.contains("error_msg")) {
            JSONObject jsonObject = JSONObject.parseObject(tuwanResult);
            Integer errorFlag = (Integer) jsonObject.get("error");
            if (errorFlag == 0) {
                //后续解析
                String title = (String) jsonObject.get("title");
                String url = (String) jsonObject.get("url");
                Integer total= (Integer) jsonObject.get("total");
                //log.info("total:"+total);
                JSONArray covers= (JSONArray)jsonObject.get("data");
                //log.info("covers:"+covers);
                String cover="";
                if (covers.size()>0){
                    JSONObject coverObj = (JSONObject) covers.get(0);
                    cover = (String) coverObj.get("pic");
                }
                boolean hasMusic = true;
                String bgm = "";
                Object hasMusicStr = jsonObject.get("bgm");
                try {
                    hasMusic = (boolean) hasMusicStr;
                } catch (Exception e) {
                    bgm = (String) hasMusicStr;
                }
                TuwanImages tuwanImages = new TuwanImages();
                tuwanImages.setTitle(title);
                tuwanImages.setUrl(url);
                tuwanImages.setId(index);
                tuwanImages.setTotal(total);
                tuwanImages.setCover(cover);
                tuwanImagesList.add(tuwanImages);
                //写入数据库
                tuwanImagesService.insertOrUpdate(tuwanImages);
                log.info(tuwanImages.toString());
                if (hasMusic) {
                    String bgmName = (String) jsonObject.get("bgm_name");
                    TuwanMp3 tuwanMp3 = new TuwanMp3();
                    tuwanMp3.setBgm(bgm);
                    tuwanMp3.setBgmName(bgmName);
                    tuwanMp3.setId(index);
                    tuwanMp3List.add(tuwanMp3);
                    log.info(tuwanMp3.toString());
                    //写入数据库
                    tuwanMp3Service.insertOrUpdate(tuwanMp3);
                } else {
                    log.info("no music!-->" + index);
                }
            }

        }else {
            log.info("wrongPage!-->" + index);
        }
    }


    /**
     * 组织请求参数
     *
     * @param params
     */
    private static void sealTuwanRequestParams(Map<String, String> params, int index) {
        params.put("type", "image");
        params.put("dpr", "3");
        params.put("id", index + "");
        //params.put("callback","jQuery1123013948419463427864_1543999122085");
        params.put("_", "1543999122086");
    }
}
