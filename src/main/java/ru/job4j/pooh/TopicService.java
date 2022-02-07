package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.util.Objects.nonNull;

public class TopicService implements Service {
    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        switch (req.httpRequestType()) {
            case "POST":
                for (Map.Entry<String, ConcurrentLinkedQueue<String>> entry
                        : topics.get(req.getSourceName()).entrySet()) {
                    entry.getValue().add(req.getParam());
                    rsl = new Resp("", "200");
                }
                break;
            case "GET":
                topics.putIfAbsent(req.getSourceName(),
                        new ConcurrentHashMap<>());
                topics.get(req.getSourceName())
                        .putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
                String answer = topics.get(req.getSourceName())
                        .get(req.getParam()).poll();
                if (nonNull(answer)) {
                    rsl = new Resp(answer, "200");
                } else {
                    rsl = new Resp("", "204");
                }
                break;
            default:
                rsl = new Resp("", "501");
        }
        return rsl;
    }
}
