package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if ("POST".equals(req.httpRequestType())) {
            for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry
                    : topics.get(req.getSourceName()).entrySet()) {
                entry.getValue().add(req.getParam());
            }
        } else {
           topics.putIfAbsent(req.getSourceName(),
                   new ConcurrentHashMap<>());
                if (topics.get(req.getSourceName())
                        .putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>()) != null) {
                   rsl = new Resp(topics.get(req.getSourceName())
                            .get(req.getParam()).poll(), "200");
                } else {
                   rsl = new Resp("", "204");
                }
            }
        return rsl;
    }
}
