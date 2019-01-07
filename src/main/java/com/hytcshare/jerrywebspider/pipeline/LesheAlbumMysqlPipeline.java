package com.hytcshare.jerrywebspider.pipeline;

import com.hytcshare.jerrywebspider.entity.LesheAlbumImages;
import com.hytcshare.jerrywebspider.enums.DownloadedStatusEnum;
import com.hytcshare.jerrywebspider.service.LesheAlbumImagesService;
import org.springframework.util.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import java.util.List;

public class LesheAlbumMysqlPipeline implements Pipeline {

    private LesheAlbumImagesService lesheAlbumImagesService;

    public LesheAlbumMysqlPipeline(LesheAlbumImagesService lesheAlbumImagesService) {
        this.lesheAlbumImagesService = lesheAlbumImagesService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> imgList = resultItems.get("imgList");
        if (imgList == null || imgList.size() == 0) {
            return;
        }
        for (String imgUrl : imgList) {
            if (StringUtils.isEmpty(imgUrl)) {
                continue;
            }
            //解析文件名称
            LesheAlbumImages image = lesheAlbumImagesService.fingByUrl(imgUrl);
            if (image == null || StringUtils.isEmpty(image.getTitle())) {
                image = new LesheAlbumImages();
            }
            image.setTitle(resultItems.get("title"));
            image.setUrl(imgUrl);
            image.setDownloaded(DownloadedStatusEnum.NOT_DOWNLOADED.getCode());
            lesheAlbumImagesService.insertOrUpdate(image);
        }
    }

}
