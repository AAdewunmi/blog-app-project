package com.springapplication.blogappproject.repository;

import com.springapplication.blogappproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find posts by title:
    // List<Post> findByTitle(String title);
    // Or to find posts containing a specific keyword in the content:
    // List<Post> findByContentContaining(String keyword);

}
