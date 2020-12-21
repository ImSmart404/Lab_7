import java.net.MalformedURLException;
import java.net.URL;

public class UrlDepthPair {                                                 //Программа должна хранить URL-адрес в виде строки вместе с его глубиной (которая для начала будет равна 0). Вам будет необходимо создать класс для представления пар [URL, depth].

    public String url;
    public int depth;
    public URL realUrl;

    public UrlDepthPair (String url, int depth) throws MalformedURLException {
        realUrl = new URL(url);
        this.url = url;
        this.depth = depth;
    }
}