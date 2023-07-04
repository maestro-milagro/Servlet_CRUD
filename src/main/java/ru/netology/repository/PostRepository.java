package ru.netology.repository;

import ru.netology.exeption.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
  protected Set<Post> postCollection = Collections.synchronizedSet(new HashSet<>());
  protected AtomicLong idCount = new AtomicLong(0);
  public Set<Post> all() {
      return postCollection;
  }

  public Optional<Post> getById(long id) {
    return postCollection.stream()
            .filter(s->s.getId()==id)
            .findAny();
  }

  public Post save(Post post) {
      if (post.getId()==0) {
          post.setId(idCount.getAndIncrement());
          postCollection.add(post);
      }else if (getById(post.getId()).isPresent()) {
          removeById(post.getId());
          postCollection.add(post);
      } else {
          throw new NotFoundException("Выбран несуществующий пост");
      }
      return post;
  }

  public void removeById(long id) {
      Optional<Post> rem = postCollection.stream()
            .filter(s->s.getId()==id)
            .findAny();
      rem.ifPresent(post -> postCollection.remove(post));

  }
}
