package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        if ((!content.contains("POST") && !content.contains("GET"))
                && !content.contains("Host")
                && !content.contains("HTTP/1.1")
                && !content.contains("User-Agent")
                && !content.contains("Accept")) {
            throw new IllegalArgumentException("Request not valid");
        }

        String[] strings = content.split(System.lineSeparator());
        String[] stroka = strings[0].split("/");
        String param;
        String httpRequestType = content.startsWith("POST") ? "POST" : "GET";
        String poohMode = stroka[1];
        String sourcName = stroka[2].split(" ")[0];
        if (httpRequestType.equals("GET")) {
            if (stroka.length > 4) {
                param = stroka[3].split(" ")[0];
            } else {
                param = "";
            }
        } else {
            param = strings[7];
        }

        return new Req(httpRequestType, poohMode, sourcName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }

}
