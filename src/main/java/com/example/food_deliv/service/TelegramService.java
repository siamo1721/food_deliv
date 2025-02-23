package com.example.food_deliv.service;

import com.example.food_deliv.config.TelegramConfig;
import com.example.food_deliv.model.Order;
import com.example.food_deliv.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DecimalFormat;

@Service
public class TelegramService extends DefaultAbsSender {
    private final TelegramConfig telegramConfig;

    protected TelegramService(TelegramConfig telegramConfig) {
        super(new DefaultBotOptions());
        this.telegramConfig = telegramConfig;
    }

    @Override
    public String getBotToken() {
        return telegramConfig.getBotToken();
    }

    public void sendOrderNotification(Order order) {
        try {
            String message = formatOrderMessage(order);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(telegramConfig.getChatId());
            sendMessage.setText(message);
            sendMessage.enableHtml(true);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка отправки уведомления в Telegram", e);
        }
    }

    private String formatOrderMessage(Order order) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        StringBuilder message = new StringBuilder();

        message.append("<b>🛍 Новый заказ #").append(order.getId()).append("</b>\n\n");
        message.append("👤 <b>Информация о клиенте:</b>\n");
        message.append("Имя: ").append(order.getFullName()).append("\n");
        message.append("Телефон: ").append(order.getPhone()).append("\n");
        message.append("Адрес: ").append(order.getAddress()).append("\n");

        if (order.getComment() != null && !order.getComment().isEmpty()) {
            message.append("Комментарий: ").append(order.getComment()).append("\n");
        }

        message.append("\n🛒 <b>Состав заказа:</b>\n");

        // Используем OrderItem вместо CartItem
        for (OrderItem item : order.getItems()) {
            message.append("\n• ");
            if (item.getDish() != null) {
                message.append(item.getDish().getName());
            } else if (item.getComplexLunch() != null) {
                message.append(item.getComplexLunch().getName());
                if (item.getBread() != null) {
                    message.append("\n  Хлеб: ").append(item.getBread());
                }
//                if (item.getDrink() != null) {
//                    message.append("\n  Напиток: ").append(item.getDrink());
//                }
            }
            message.append("\n  Количество - ").append(item.getQuantity());
            message.append(" - ").append(df.format(item.getPrice())).append(" ₽");
        }

        message.append("\n\n💰 <b>Итого: ").append(df.format(order.getTotalAmount())).append(" ₽</b>");
        message.append("\n📅 Время заказа: ").append(order.getCreatedAt().format(
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        message.append("\n💳 <b>Проверьте оплату от ").append(order.getFullName()).append(" в приложении Сбербанка</b>\n\n");
        return message.toString();
    }
}