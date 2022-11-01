import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {

        TelegramBotsApi telegramBotsApi;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        BenefitCalculatorBot benefitCalculatorBot = new BenefitCalculatorBot();
        try {
            telegramBotsApi.registerBot(benefitCalculatorBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}