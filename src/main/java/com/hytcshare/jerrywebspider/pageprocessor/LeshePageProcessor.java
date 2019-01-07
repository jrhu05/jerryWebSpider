package com.hytcshare.jerrywebspider.pageprocessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class LeshePageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex(".+page=.?").all());
        List<String> zipList = page.getHtml().links().regex(".+zip").all();
        page.putField("zipList", zipList);
    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {
        String test = "风之领域";
        String encode = "";
        try {
            encode = URLEncoder.encode(test, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //%E9%A3%8E%E4%B9%8B%E9%A2%86%E5%9F%9F
        if (!encode.equalsIgnoreCase("")) {
            OOSpider.create(new LeshePageProcessor())
                    .addUrl("http://111.231.221.217:34567/A:/" + encode + "?sortby=lastModtime&order=desc&page=1")
                    .addPipeline(new ConsolePipeline())
                    .thread(5).run();
        }
    }
}
