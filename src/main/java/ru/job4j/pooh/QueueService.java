package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> queue
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp rsl;
        switch (req.httpRequestType()) {
            case "POST":
                queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedDeque<>());
                queue.get(req.getSourceName()).addLast(req.getParam());
                rsl = new Resp(req.getParam(), "200");
                break;
            case "GET":
                if (!queue.get(req.getSourceName()).isEmpty()) {
                    rsl = new Resp(queue.get(req.getSourceName()).pollLast(), "200");
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
