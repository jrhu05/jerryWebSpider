package com.hytcshare.jerrywebspider.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hytcshare.jerrywebspider.constant.TuwanConstant;
import com.hytcshare.jerrywebspider.entity.TuwanAlbumImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import com.hytcshare.jerrywebspider.enums.ExceptionEnum;
import com.hytcshare.jerrywebspider.exception.SpiderException;
import com.hytcshare.jerrywebspider.service.SpiderTaskService;
import com.hytcshare.jerrywebspider.service.TuwanAlbumImagesService;
import com.hytcshare.jerrywebspider.utils.ExceptionUtils;
import com.hytcshare.jerrywebspider.utils.HttpUtils;
import com.hytcshare.jerrywebspider.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

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
        try {
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
        }catch (Exception e){
            log.info(ExceptionUtils.getStackTrace(e));
        }finally {
            //更新任务状态为执行完毕
            TaskUtils.shutdownTask(spiderTaskService, tuwanAlbumSpiderTaskName);
            log.info("tuwan album spider task finish!");
        }
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
                //防重判断
                if (tuwanAlbumImagesService.countByTitle(title)>0){
                    return;
                }
                JSONArray thumbs = (JSONArray) jsonObject.get("thumb");
                String cover = "";
                if (thumbs == null || thumbs.size() <= 0){
                    return;
                }
                for (Object thumb : thumbs) {
                    String thumbUrl = (String) thumb;
                    String url = translateUrl(thumbUrl);
                    //写数据库
                    TuwanAlbumImages tuwanAlbumImages = tuwanAlbumImagesService.fingByUrl(url);
                    if (tuwanAlbumImages == null) {
                        tuwanAlbumImages = new TuwanAlbumImages();
                    }
                    tuwanAlbumImages.setAlbumIndex(index);
                    tuwanAlbumImages.setTitle(title);
                    tuwanAlbumImages.setUrl(url);
                    tuwanAlbumImages.setDownloaded(DownloadedStatusEnum.NOT_DOWNLOADED.getCode());
                    tuwanAlbumImagesService.insertOrUpdate(tuwanAlbumImages);
                }

            }

        } else {
            log.info("wrongPage!-->" + index);
        }
    }

    private String translateUrl(String thumbUrl) {
        if (!thumbUrl.contains(TuwanConstant.THUMB_PREFIX)){
            return "";
        }
        String[] urlPartition = thumbUrl.split("/");
        if (urlPartition.length < TuwanConstant.THUMB_PARTITION_MIN_LENGTH){
            return "";
        }
        //取得code
        String thumbCode = urlPartition[TuwanConstant.THUMB_CODE_POSITION];
        byte[] decodeByte = Base64.getDecoder().decode(thumbCode);
        String decodeStr = new String(decodeByte);
        if (!decodeStr.contains(TuwanConstant.THUMB_CODE_SEPARATOR)){
            return "";
        }
        String[] decodePartition = decodeStr.split(TuwanConstant.THUMB_CODE_SEPARATOR);
        if (decodePartition.length < TuwanConstant.THUMB_CODE_PARTITION_MIN_LENGTH){
            return "";
        }
        //转换
        String realTumbId = decodePartition[0];
        String needEncode = realTumbId + TuwanConstant.MAHOU_CODE;
        byte[] encode = Base64.getEncoder().encode(needEncode.getBytes());
        urlPartition[TuwanConstant.THUMB_CODE_POSITION] = new String(encode);
        StringBuilder sb = new StringBuilder();
        for (String temp : urlPartition){
            sb.append(temp).append("/");
        }
        String realUrl = sb.substring(0,sb.lastIndexOf("/"));
        return realUrl;
    }

    public static void main(String[] args) {
        TuwanAlbumSpiderTask tuwanAlbumSpiderTask =new TuwanAlbumSpiderTask();
        String realUrl = tuwanAlbumSpiderTask.translateUrl("http://img4.tuwandata.com/v3/thumb/jpg/NzliYSwxNTgsMTU4LDksMywxLC0xLE5PTkUsLCw5MA==/u/GLDM9lMIBglnFv7YKftLBGMvdHLuuaCeChlsq0Xe5FYcdsnwHww1mBTnGlUSoeUOoTb9mvfPektEbYcun5ZMfGMUcpOxjDMb5Z2SxwM0JaGI.jpg");
        System.out.println("realUrl==>"+realUrl);
        tuwanAlbumSpiderTask.setWelfareUrl("https://api.tuwan.com/apps/Welfare/detail");
        tuwanAlbumSpiderTask.getAlbumImages(62);
    }
}
