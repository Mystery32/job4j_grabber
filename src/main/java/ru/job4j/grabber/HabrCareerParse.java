package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private static final String SOURCE_LINK = "https://career.habr.com";

    public static void main(String[] args) throws IOException {
        HabrCareerDateTimeParser dataParser = new HabrCareerDateTimeParser();
        HabrCareerParse vacancyParser = new HabrCareerParse(dataParser);

        for (int numberOfPage = 1; numberOfPage < 6; numberOfPage++) {
            String pageLink = String.format("%s/vacancies/java_developer?page=%d", SOURCE_LINK, numberOfPage);
            System.out.println(vacancyParser.list(pageLink));
        }
    }

    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements descriptionElement = document.select(".style-ugc");
        return descriptionElement.text();
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> post = new ArrayList<>();
        HabrCareerDateTimeParser dataParser = new HabrCareerDateTimeParser();
        HabrCareerParse vacancyParser = new HabrCareerParse(dataParser);
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-card__inner");
        for (Element row : rows) {
            Element titleElement = row.select(".vacancy-card__title").first();
            String vacancyName = titleElement.text();
            Element dataElement = row.select(".vacancy-card__date").first();
            Element vacancyDate = dataElement.child(0);
            String data = vacancyDate.attr("datetime");
            LocalDateTime parseData = dataParser.parse(data);
            String description = vacancyParser.retrieveDescription(link);
            post.add(new Post(vacancyName, link, description, parseData));
        }
        return post;
    }
}
