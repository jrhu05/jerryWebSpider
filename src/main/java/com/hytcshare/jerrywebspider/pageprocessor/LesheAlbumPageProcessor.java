package com.hytcshare.jerrywebspider.pageprocessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import java.util.List;

public class LesheAlbumPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(300)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
    ;

    public static final String URL_LIST = "https://www\\.leshe\\.us/page/.+";

    public static final String URL_POST = "https://www.leshe.us/xz/.+/.+";

    @Override
    public void process(Page page) {
        //列表页
        if (page.getUrl().regex(URL_LIST).match()) {
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
        } else {
            String title = page.getHtml().xpath("//*[@class=\"focusbox-title\"]/text()").toString();
            List<String> zipList = page.getHtml().links().regex("https://www.leshe.us/wp-content/uploads/.+jpg").all();
            page.putField("imgList", zipList);
            page.putField("title", title);
        }
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        OOSpider.create(new LesheAlbumPageProcessor())
                .addUrl("https://www.leshe.us/page/1")
                .addPipeline(new ConsolePipeline())
                .thread(5).run();
    }
}
