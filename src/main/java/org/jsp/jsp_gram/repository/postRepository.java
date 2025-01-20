package org.jsp.jsp_gram.repository;

import java.util.List;

import org.jsp.jsp_gram.dto.Posts;
import org.jsp.jsp_gram.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface postRepository extends JpaRepository<Posts,Integer> {

    List<Posts> findByUser(User user);

    List<Posts> findByUserIn(List<User> users);

}
