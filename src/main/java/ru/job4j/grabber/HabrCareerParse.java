package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    public static void main(String[] args) throws IOException {
        HabrCareerDateTimeParser dataParser = new HabrCareerDateTimeParser();
        HabrCareerParse vacancyParser = new HabrCareerParse();

        for (int numberOfPage = 1; numberOfPage < 6; numberOfPage++) {
            String pageLink = String.format("%s/vacancies/java_developer?page=%d", SOURCE_LINK, numberOfPage);
            Connection connection = Jsoup.connect(pageLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            for (Element row : rows) {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                Element dataElement = row.select(".vacancy-card__date").first();
                Element vacancyDate = dataElement.child(0);
                String data = vacancyDate.attr("datetime");
                String parseData = dataParser.parse(data).toString();
                String description = vacancyParser.retrieveDescription(link);
                System.out.printf("%s %s %s%n %s%n", vacancyName, parseData, link, description);
            }
        }
    }

    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Elements descriptionElement = document.select(".style-ugc");
        return descriptionElement.text();
    }
}
