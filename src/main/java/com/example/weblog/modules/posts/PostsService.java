package com.example.weblog.modules.posts;


import com.example.weblog.MyBeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PostsService {

    private PostsRepo postsRepository;

    @Autowired
    public PostsService(PostsRepo postsRepository) {
        this.postsRepository = postsRepository;
    }

    @Transactional
    public Posts registerPost(Posts posts) throws IOException, InvocationTargetException, IllegalAccessException {

        if (!posts.getFile().isEmpty()) {
            String path = ResourceUtils.getFile("classpath:static/img").getAbsolutePath();   //path
            byte[] bytes = posts.getFile().getBytes();   //convert file to byte array
            String name = UUID.randomUUID() + "." + Objects.requireNonNull(posts.getFile().getContentType()).split("/")[1]; //image/jpg[1]
            Files.write(Paths.get(path + File.separator + name), bytes); //save byte array in path
            posts.setCover(name);
        }
        if (posts.getId() != null) {
            Posts exist = postsRepository.getOne(posts.getId());
            MyBeanCopy myBeanCopy = new MyBeanCopy();
            myBeanCopy.copyProperties(exist, posts);
            return postsRepository.save(exist);
        }
        return this.postsRepository.save(posts);
    }

    public List<Posts> findAllPosts() {
        return this.postsRepository.findAll();
    }

    public Page<Posts> findAllPosts(Pageable pageable) {
        return this.postsRepository.findAll(pageable);
    }


    public Posts findById(Long id) {
        return postsRepository.getOne(id);
    }

    @Transactional
    public void deleteById(Long id) {
        postsRepository.deleteById(id);
    }

    public Page<Posts> findBySearch(Posts posts, Pageable pageable) {
        return postsRepository.findBySearch(posts, (posts.getCategories() != null ? (long) posts.getCategories().size() : 0), pageable);
    }

}
