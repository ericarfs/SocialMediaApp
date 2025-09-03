package ericarfs.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ericarfs.socialmedia.entity.Post;
import ericarfs.socialmedia.entity.Share;
import ericarfs.socialmedia.entity.User;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Optional<Share> findByPostAndUser(Post post, User user);

    List<Share> findByPostId(Long postId);

    @Query("SELECT s.user FROM Share s WHERE s.post.id = :postId")
    List<User> findSharedUsersByPostId(Long postId);
}
