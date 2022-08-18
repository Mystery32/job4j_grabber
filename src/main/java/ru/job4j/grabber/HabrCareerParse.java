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
        for (int numberOfPage = 1; numberOfPage < 6; numberOfPage++) {
            String pageLink = String.format("%s/vacancies/java_developer?page=%d", SOURCE_LINK, numberOfPage);
            Connection connection = Jsoup.connect(pageLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                Element dataElement = row.select(".vacancy-card__date").first();
                Element vacancyDate = dataElement.child(0);
                String data = vacancyDate.attr("datetime");
                HabrCareerDateTimeParser hcdtp = new HabrCareerDateTimeParser();
                String parseData = hcdtp.parse(data).toString();
                System.out.printf("%s %s %s %n", vacancyName, parseData, link);
            });
        }
    }
}
