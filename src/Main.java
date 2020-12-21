
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class Main {
    public static <var> void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://mtuci.ru/");                              //URL-адрес, с которого можно начать просмотр страницы.

        Crawler parser = new Crawler();
        parser.startParsing(url, 2, 0);                       //Положительное целое число, которое является максимальной глубиной поиска

        LinkedList<UrlDepthPair> result = parser.proccessed;

        result.forEach(urlDepthPair -> {                                           //выводит адрес и глубину
            System.out.println(
                    "URL is " + urlDepthPair.url +
                            " | depth: " + urlDepthPair.depth);
        });
    }
}