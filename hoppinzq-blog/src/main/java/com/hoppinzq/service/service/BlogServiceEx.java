package com.hoppinzq.service.service;

import com.hoppinzq.service.bean.Blog;
import com.hoppinzq.service.dao.BlogDao;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 额外
 */
@Service
public class BlogServiceEx {
    @Autowired
    private BlogDao blogDao;

    @Async
    public void insertDraftBlog(Blog blog){
        try{
            blogDao.insertBlog(blog);
        }catch (Exception ex){

        }
    }
}
