package ericarfs.socialmedia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ericarfs.socialmedia.entity.Post;
import ericarfs.socialmedia.entity.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
        Optional<Post> findByIdAndAuthor(Long id, User author);

        List<Post> findByAuthor(User author);

        @Query(value = "\n" + //
                        "SELECT \n" + //
                        "  p.id, \n" + //
                        "  p.answer_id,\n" + //
                        "  p.author_id, \n" + //
                        "  p.updated_at, \n" + //
                        "  CASE WHEN p.author_id=:userId THEN p.created_at \n" + //
                        "  ELSE s.share_date END AS created_at \n" + //
                        "FROM posts AS p \n" + //
                        "LEFT JOIN posts_shares AS s ON p.id= s.post_id AND s.user_id=:userId \n" + //
                        "WHERE p.author_id=:userId OR s.user_id=:userId \n" + //
                        "ORDER BY created_at DESC", nativeQuery = true)
        Page<Post> findAuthoredAndSharedPosts(Long userId, Pageable pageable);

        @Query(value = "\n" + //
                        "WITH following AS (\n" + //
                        "  SELECT following_id\n" + //
                        "  FROM users_following\n" + //
                        "  WHERE followers_id = :userId\n" + //
                        ")\n" + //
                        "SELECT\n" + //
                        "  p.id,\n" + //
                        "  p.answer_id,\n" + //
                        "  p.author_id,\n" + //
                        "  p.updated_at,\n" + //
                        "  CASE\n" + //
                        "    WHEN p.author_id IN (SELECT following_id FROM Following) THEN p.created_at\n" + //
                        "    ELSE s.share_date\n" + //
                        "  END AS created_at\n" + //
                        "FROM\n" + //
                        "  posts AS p\n" + //
                        "  LEFT JOIN posts_shares AS s\n" + //
                        "    ON p.id = s.post_id AND s.user_id IN (SELECT following_id FROM following)\n" + //
                        "WHERE\n" + //
                        "  p.author_id IN (SELECT following_id FROM following)\n" + //
                        "  OR s.user_id IN (SELECT following_id FROM following)\n" + //
                        "ORDER BY created_at DESC;", nativeQuery = true)
        Page<Post> findFollowersActivities(Long userId, Pageable pageable);

        boolean existsByIdAndAuthor(Long id, User author);
}
