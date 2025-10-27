package org.travy.Springstarter.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.travy.Springstarter.models.Post;

public interface PostRespository extends JpaRepository<Post, Long> {
    
}
