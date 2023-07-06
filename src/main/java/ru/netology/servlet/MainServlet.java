package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainServlet extends HttpServlet {
  private PostController controller;
  protected List<String> possiblePaths = List.of("/api/posts");

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals("GET") && possiblePaths.contains(path)) {
        controller.all(resp);
        return;
      }
      if (method.equals("GET") && matchFind(path)) {
        // easy way
        String[] parts = path.split("/");
        final var id = Long.parseLong(parts[parts.length-1]);
        controller.getById(id, resp);
        return;
      }
      if (method.equals("POST") && possiblePaths.contains(path)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals("DELETE") && matchFind(path)) {
        // easy way
        String[] parts = path.split("/");
        final var id = Long.parseLong(parts[parts.length-1]);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
  public boolean matchFind(String path){
    for (String s : possiblePaths) {
      if (path.matches(s+"/\\d+")) {
        return true;
      }
    }
    return false;
  }
}

