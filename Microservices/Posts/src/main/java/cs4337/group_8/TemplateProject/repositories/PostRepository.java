package cs4337.group_8.TemplateProject.repositories;

import cs4337.group_8.TemplateProject.entities.Post;
import cs4337.group_8.TemplateProject.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

}