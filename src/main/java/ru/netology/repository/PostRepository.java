package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exeption.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    protected Map<Long, Post> postCollection = Collections.synchronizedMap(new HashMap<>());
    protected AtomicLong idCount = new AtomicLong(0);

    public Map<Long, Post> all() {
        return postCollection;
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(postCollection.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(idCount.getAndIncrement());
            postCollection.put(post.getId(), post);
        } else if (getById(post.getId()).isPresent()) {
            removeById(post.getId());
            postCollection.put(post.getId(), post);
        } else {
            throw new NotFoundException("Выбран несуществующий пост");
        }
        return post;
    }

    public void removeById(long id) {
        postCollection.remove(id);
    }
}
