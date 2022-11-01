import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BenefitCalculatorBot extends TelegramLongPollingBot {
    int answerCounter = 0;
    String firstProductPrice;
    String firstProductWeight;
    String secondProductPrice;
    String secondProductWeight;

    @Override
    public String getBotUsername() {
        return "BenefitCalculator_Bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().equals("/start")) {
                SendMessage msg = new SendMessage();
                msg.setChatId(update.getMessage().getChatId());
                String welcomeMessage = """                                              
                        Добро пожаловать в Калькулятор выгод!
                        Когда видишь в магазине два товара с похожими характеристиками, но имеющими разный вес и объем, всегда хочется купить тот, который выгоднее.
                        Этот бот поможет вам в этом.
                        Удачи!""";
                msg.setText(welcomeMessage);
                sendMessage(msg);
            }
            if (update.hasMessage() && update.getMessage().hasText() && answerCounter == 0) {
                SendMessage msg = new SendMessage();
                msg.setChatId(update.getMessage().getChatId());
                msg.setText("Введите цену первого товара.");
                sendMessage(msg);
                answerCounter++;
            } else if (update.hasMessage() && update.getMessage().hasText() && answerCounter == 1) {
                firstProductPrice = update.getMessage().getText();
                SendMessage msg = new SendMessage();
                msg.setChatId(update.getMessage().getChatId());
                msg.setText("Введите массу первого товара.");
                sendMessage(msg);
                answerCounter++;
            } else if (update.hasMessage() && update.getMessage().hasText() && answerCounter == 2) {
                firstProductWeight = update.getMessage().getText();
                SendMessage msg = new SendMessage();
                msg.setChatId(update.getMessage().getChatId());
                msg.setText("Введите цену второго товара.");
                sendMessage(msg);
                answerCounter++;
            } else if (update.hasMessage() && update.getMessage().hasText() && answerCounter == 3) {
                secondProductPrice = update.getMessage().getText();
                SendMessage msg = new SendMessage();
                msg.setChatId(update.getMessage().getChatId());
                msg.setText("Введите массу второго товара.");
                sendMessage(msg);
                answerCounter++;
            } else if (update.hasMessage() && update.getMessage().hasText() && answerCounter == 4) {
                secondProductWeight = update.getMessage().getText();
                String resultMessage;
                {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(update.getMessage().getChatId());

                    if (isNumeric(firstProductPrice) && isNumeric(firstProductWeight) && isNumeric(secondProductPrice) && isNumeric(secondProductWeight) &&
                            isAvailableValue(firstProductPrice) && isAvailableValue(firstProductWeight) && isAvailableValue(secondProductPrice) && isAvailableValue(secondProductWeight)) {
                        resultMessage = whichOneCheaper(firstProductPrice, firstProductWeight, secondProductPrice, secondProductWeight);
                    } else {
                        resultMessage = """                                            
                                Упс... Что-то пошло не так.
                                Цена товара должна быть указана без символов валют.
                                Масса товара должна быть указана без единиц измерения.
                                Бот понимает только положительные числовые значения.
                                Попробуйте ещё раз.
                                """;
                    }
                    msg.setText(resultMessage);
                    sendMessage(msg);
                    answerCounter = 0;
                }
                {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(update.getMessage().getChatId());
                    msg.setText("Введите цену первого товара.");
                    sendMessage(msg);
                    answerCounter++;
                }
            }
        }
    }

    private void sendMessage(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    private static boolean isAvailableValue(String str) {
        return Double.parseDouble(str) > 0 && Double.parseDouble(str) < Double.MAX_VALUE;
    }

    public static String whichOneCheaper(String firstPrice, String firstWeight, String secondPrice, String secondWeight) {
        String message;
        double firstProductPerItemPrice = Double.parseDouble(firstPrice) / Double.parseDouble(firstWeight);
        double secondProductPerItemPrice = Double.parseDouble(secondPrice) / Double.parseDouble(secondWeight);

        if (firstProductPerItemPrice < secondProductPerItemPrice) {
            message = "Первый товар выгоднее.\n";
        } else if (firstProductPerItemPrice > secondProductPerItemPrice) {
            message = "Второй товар выгоднее.\n";
        } else {
            message = "Цена за единицу товара одинаковая.\n";
        }
        return message;
    }
}