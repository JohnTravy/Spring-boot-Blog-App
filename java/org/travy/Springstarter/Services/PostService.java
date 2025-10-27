package org.travy.Springstarter.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.travy.Springstarter.models.Post;
import org.travy.Springstarter.respositories.PostRespository;


@Service
public class PostService {

    @Autowired
    private PostRespository postRespository;

    public Optional<Post> getByid(Long id){
     
        return postRespository.findById(id);

    }

    public List<Post> findAll(){
     
        return postRespository.findAll();

    }

    public Page<Post> findAll(int offset, int PageSize, String field){
     
        return postRespository.findAll(PageRequest.of(offset, PageSize).withSort(Direction.ASC, field));

    }



    public  void delete (Post post){
     
         postRespository.delete(post);

    }

    public Post save(Post post){
     
        if(post.getId() == null){

            post.setCreatedAT(LocalDateTime.now());

        }
        post.setUpdatedAT(LocalDateTime.now());

        return postRespository.save(post);

    }

    
}
