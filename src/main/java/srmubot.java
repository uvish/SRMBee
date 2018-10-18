import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;

public class srmubot extends TelegramLongPollingBot {

    String rollno="201610101110005";
    String password="123";
    SendMessage message;
    final String USER_AGENT = "\"Mozilla/5.0 (Windows NT\" +\n" +
            "          \" 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\"";
    String loginFormUrl = "http://115.112.99.254:37520/psp/ps/EMPLOYEE/SA/c/MANAGE_ACADEMIC_RECORDS.STDNT_ATTEND_TERM.GBL";
    String loginActionUrl = "http://115.112.99.254:37520/psp/ps/EMPLOYEE/SA/c/MANAGE_ACADEMIC_RECORDS.STDNT_ATTEND_TERM.GBL?&cmd=login&languageCd=ENG";
    String userId = "201610101110005";

    HashMap<String, String> formData = new HashMap<>();

    public void onUpdateReceived(Update update) {
    System.out.println(update.getMessage().getText());

    System.out.println(update.getMessage().getFrom().getFirstName()+" "+update.getMessage().getFrom().getLastName()+" "+update.getMessage().getFrom().getUserName());

        SendMessage message=new SendMessage();
        String query=update.getMessage().getText();
        if(query.equals("/attendance"))
        {
            try {
                message.setText(get_attendance(update));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       message.setChatId(update.getMessage().getChatId());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "srmubot";
    }

    public String getBotToken() {
        return "695759010:AAGaLRd4Ti5j2r6zrR0ktY9O3ywKLNIA3JI";
    }


    public String get_attendance(Update update) throws IOException {
           ScriptResult result = null;HtmlPage page=null;
          Connection.Response homePage=null;
         String out="";
      //  WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
        WebClient client=new WebClient(BrowserVersion.CHROME);
       // client.getOptions().setCssEnabled(false);
     //   client.getOptions().setJavaScriptEnabled(true);
      //  client.waitForBackgroundJavaScript(5000);

        try {


            Connection.Response loginForm = Jsoup.connect(loginFormUrl).method(Connection.Method.GET).userAgent(USER_AGENT).execute();
            Document loginDoc = loginForm.parse();
            // page=client.getPage("https://www.github.com");
            formData.put("commit", "Sign in");
            formData.put("utf8", "e2 9c 93");
            formData.put("userid", userId);
            formData.put("pwd", password);
            formData.put("ptlangsel","ENG");

            homePage = Jsoup.connect(loginActionUrl)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .execute();

              out=loginDoc.title();//homePage.parse().title();
            System.out.println(out);




        } catch (IOException e) {
            e.printStackTrace();
        }


        return (out);
    }
}
