<h2>Проект "Агрегатор вакансий"</h2>

Система запускается по расписанию. Период запуска указывается в настройках - app.properties. Программа должна считывать все вакансии относящиеся к Java и записывать их в базу. Информация может парситься с различных сайтов (в данном случае информация берётся с сайта career.habr.com, а именно с раздела https://career.habr.com/vacancies/java_developer).

Доступ к вакансиям через localhost:9000

Параметры запуска прописываются в файле настроек app.properties:  
port - HTTP порт для просмотра вакансий  
time - интервал парсинга

#### Стек технологий: 
Java 14, PostgreSQL, JDBC, Quartz-Scheduler, Jsoup, log4j+slf4j, Maven, Git
