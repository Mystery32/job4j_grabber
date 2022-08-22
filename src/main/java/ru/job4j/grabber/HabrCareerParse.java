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
    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final int TOTAL_PAGE = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) {
        HabrCareerDateTimeParser dataParser = new HabrCareerDateTimeParser();
        HabrCareerParse vacancyParser = new HabrCareerParse(dataParser);
        System.out.println(vacancyParser.list(SOURCE_LINK));
    }

    private String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        Elements descriptionElement = document.select(".style-ugc");
        return descriptionElement.text();
    }

    private Post getPost(Element element) {
        Element titleElement = element.select(".vacancy-card__title").first();
        Element linkElement = titleElement.child(0);
        String vacancyName = titleElement.text();
        String linkToVacancy = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
        Element dataElement = element.select(".vacancy-card__date").first();
        Element vacancyDate = dataElement.child(0);
        String data = vacancyDate.attr("datetime");
        LocalDateTime parseData = dateTimeParser.parse(data);
        String description = retrieveDescription(linkToVacancy);
        return new Post(vacancyName, linkToVacancy, description, parseData);
    }

    @Override
    public List<Post> list(String link) {
        List<Post> post = new ArrayList<>();
        for (int numberOfPage = 1; numberOfPage <= TOTAL_PAGE; numberOfPage++) {
            String pageLink = String.format("%s/vacancies/java_developer?page=%d", link, numberOfPage);
            Connection connection = Jsoup.connect(pageLink);
            Document document;
            try {
                document = connection.get();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
            Elements rows = document.select(".vacancy-card__inner");
            for (Element row : rows) {
                post.add(getPost(row));
            }
        }
        return post;
    }
}
