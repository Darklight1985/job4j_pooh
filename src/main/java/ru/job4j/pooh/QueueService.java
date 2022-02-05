package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> queue
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl = null;
        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedDeque<>());
            queue.get(req.getSourceName()).addLast(req.getParam());
        } else {
                if (!"null".equals(queue.get(req.getSourceName()).getLast())) {
                    rsl = new Resp(queue.get(req.getSourceName()).pollLast(), "200");
                } else {
                    rsl = new Resp("no data", "204");
                }
            }
        return rsl;
    }
}
