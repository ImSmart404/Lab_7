import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {                                                              //ищет ссылки (сканер)
    public LinkedList<UrlDepthPair> proccessed;                                     //List по ссылкам (хранит ссылки на предыдущий и следующий элемент)

    public Crawler() {
        proccessed = new LinkedList<>();
    }

    public void startParsing(URL baseUrl, int maxDepth, int currentDepth) {
        if (currentDepth > maxDepth) return;

        LinkedList<UrlDepthPair> links = getAllLinks(baseUrl, currentDepth);       //записывает все эллементы (ссылки в новый лист)

        for (UrlDepthPair link: links) {
            startParsing(link.realUrl, maxDepth, currentDepth + 1);     //для нахождение "подссылок"
        }

        proccessed.addAll(links);                                                   //записывает все ссылки в лист proccessed из links
    }

    private static LinkedList<UrlDepthPair> getAllLinks(URL url, int depth) {
        try {
            LinkedList<UrlDepthPair> links = new LinkedList<>();

            int port = 80;
            String hostname = url.getHost();                                        //имя хоста

            Socket socket = new Socket(hostname, port);
            socket.setSoTimeout(3000);                                              //вызывается исключение после того как проходит 3000 мс

            OutputStream outStream = socket.getOutputStream();                      //ссылка на поток в который мы будем записывать данные

            PrintWriter writer = new PrintWriter(outStream, true);          //Класс PrintStream - это именно тот класс, который используется для вывода на консоль. Когда мы выводим на консоль некоторую информацию с помощью вызова System.out.println(), то тем самым мы задействует PrintStream, так как переменная out в классе System как раз и представляет объект класса PrintStream, а метод println() - это метод класса PrintStream.

            if (url.getPath().length() == 0) {                                      //вывод в консоль
                writer.println("GET / HTTP/1.1");                               //если нет пары
                writer.println("Host: " + hostname);
                writer.println("Accept: text/html");
                writer.println("Accept-Language: en,en-US;q=0.9,ru;q=0.8");
                writer.println("Connection: close");
                writer.println();
            }
            else {
                writer.println("GET " + url.getPath() + " HTTP/1.1");           //есть пара
                writer.println("Host: " + hostname);
                writer.println("Accept: text/html");
                writer.println("Accept-Language: en,en-US;q=0.9,ru;q=0.8");
                writer.println("Connection: close");
                writer.println();
            }

            InputStream input = socket.getInputStream();                                //открывает входной поток данных
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));   //считывает текст из потока ввода данных

            String htmlLine;

            Pattern patternURL = Pattern.compile(                                                                               //compline создаем неизменяемый обьект потом мы его используем
                    "(href=\"http|href=\"https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?"   //шаблон - по которому мы ориентируемся
            );

            while ((htmlLine = reader.readLine()) != null) {                                        //считываем текст из потока
                Matcher matcherURL = patternURL.matcher(htmlLine);                                   //определяет совпадение со строкой
                while (matcherURL.find()) {                                                           //ищет совпадения
                    String link = htmlLine.substring(matcherURL.start() + 6,                          //считываем посимвольно
                            matcherURL.end());

                    links.add(new UrlDepthPair(link, depth));
                }
            }
            socket.close();

            return links;
        } catch (Exception e) {                                         //если сработало исключение
            System.out.print(e.getMessage());
            System.out.print(Arrays.toString(e.getStackTrace()));

            return new LinkedList<>();
        }
    }
}