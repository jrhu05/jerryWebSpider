package com.hytcshare.jerrywebspider.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hytcshare.jerrywebspider.entity.TuwanAlbumImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import com.hytcshare.jerrywebspider.enums.ExceptionEnum;
import com.hytcshare.jerrywebspider.exception.SpiderException;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.service.TuwanAlbumImagesService;
import com.hytcshare.jerrywebspider.utils.HttpUtils;
import com.hytcshare.jerrywebspider.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TuwanAlbumSpiderTask implements Runnable {
    private String welfareUrl;
    private int tuwanStart;
    private Integer endLine;

    private SpiderTaskService spiderTaskService;
    private TuwanAlbumImagesService tuwanAlbumImagesService;

    private String tuwanAlbumSpiderTaskName;

    public void setWelfareUrl(String welfareUrl) {
        this.welfareUrl = welfareUrl;
    }

    public void setTuwanStart(int tuwanStart) {
        this.tuwanStart = tuwanStart;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }

    public void setSpiderTaskService(SpiderTaskService spiderTaskService) {
        this.spiderTaskService = spiderTaskService;
    }

    public void setTuwanAlbumImagesService(TuwanAlbumImagesService tuwanAlbumImagesService) {
        this.tuwanAlbumImagesService = tuwanAlbumImagesService;
    }

    public void setTuwanAlbumSpiderTaskName(String tuwanAlbumSpiderTaskName) {
        this.tuwanAlbumSpiderTaskName = tuwanAlbumSpiderTaskName;
    }

    @Override
    public void run() {
        log.info("tuwan album spider start！");
        TaskUtils.startTask(spiderTaskService, tuwanAlbumSpiderTaskName);
        //获取图片地址
        if (endLine == null || endLine < tuwanStart + 1) {
            throw new SpiderException(ExceptionEnum.ENDLINE_ILLEGAL.getCode(), ExceptionEnum.ENDLINE_ILLEGAL.getDes());
        }
        for (int i = tuwanStart; i <= endLine; i++) {
            try {
                Thread.sleep((long) Math.random() * 3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getAlbumImages(i);
        }
        //更新任务状态为执行完毕
        TaskUtils.shutdownTask(spiderTaskService, tuwanAlbumSpiderTaskName);
        log.info("tuwan album spider task finish!");
    }

    private void getAlbumImages(int index) {
        String tuwanResult = HttpUtils.get(welfareUrl + "?id=" + index);
        tuwanResult = tuwanResult.substring(1, tuwanResult.length() - 1);
        if (tuwanResult.contains("error_msg")) {
            JSONObject jsonObject = JSONObject.parseObject(tuwanResult);
            Integer errorFlag = (Integer) jsonObject.get("error");
            if (errorFlag == 0) {
                //后续解析
                String title = (String) jsonObject.get("title");
                JSONArray albums = (JSONArray) jsonObject.get("data");
                String cover = "";
                if (albums.size() > 0) {
                    for (Object album : albums) {
                        JSONObject albumObj = (JSONObject) album;
                        String url = (String) albumObj.get("pic");
                        //写数据库
                        TuwanAlbumImages tuwanAlbumImages = tuwanAlbumImagesService.fingByUrl(url);
                        if (tuwanAlbumImages == null) {
                            tuwanAlbumImages = new TuwanAlbumImages();
                        }
                        tuwanAlbumImages.setTitle(index + title);
                        tuwanAlbumImages.setUrl(url);
                        tuwanAlbumImages.setDownloaded(DownloadedStatusEnum.NOT_DOWNLOADED.getCode());
                        tuwanAlbumImagesService.insertOrUpdate(tuwanAlbumImages);
                    }
                }

            }

        } else {
            log.info("wrongPage!-->" + index);
        }
    }

//    public static void main(String[] args) {
//        getAlbumImages("https://api.tuwan.com/apps/Welfare/detail", 1024);
//    }
}
