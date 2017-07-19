package com.sftc.web.service.impl;

import com.sftc.web.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by huxingyue on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring**.xml"})
public class MessageServiceImplTest {
    @Resource
    private MessageService messageService;

    @Test
    public void getMessage() throws Exception {
    }

    @Test
    public void register() throws Exception {
    }

    @Test
    public void getToken() throws Exception {
    }

    @Test
    public void sfLogin() throws Exception {
    }

    @Test
    public void sendWXTemplateMessage() throws Exception {
        System.out.println("===========================1");
//        List<String> tags = new ArrayList<String>();
//        tags.add("1");
//        tags.add("2");
//        Article article = new Article("java", "作者", tags);
//        List<Article> articles = new ArrayList<Article>();
//        for (int i = 0; i < 5; i++) {
//            articles.add(article);
//        }
//        articles.stream().filter(article2 -> article2.getTags().contains("java")).findFirst();
        final int parm = 10;
        Arrays.asList("a","b").forEach(e ->
        {
            System.out.println(e+parm);

        });
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName( "JavaScript" );


        System.out.println( engine.getClass().getName() );
        System.out.println( "Result:" + engine.eval( "function f() { return 1; }; f() + 1;" ) );
    }

}

class Article {

    private String title;
    private String author;
    private List<String> tags;

    public Article(String title, String author, List<String> tags) {
        this.title = title;
        this.author = author;
        this.tags = tags;
    }

    public Article() {
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getTags() {
        return tags;
    }
}