let cartId = localStorage.getItem('cartId');
let orderData = null; // Сохраняем данные заказа здесь

function showNotification(message, isError = false) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification ${isError ? 'error' : ''}`;

    setTimeout(() => {
        notification.classList.add('show');
    }, 0);

    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

async function loadCart() {
    if (!cartId) {
        renderEmptyCart();
        return;
    }

    try {
        const response = await fetch(`/api/cart/${cartId}`);
        if (!response.ok) {
            throw new Error('Ошибка при загрузке корзины');
        }
        const cart = await response.json();
        renderCart(cart);
    } catch (error) {
        console.error('Ошибка при загрузке корзины:', error);
        showNotification('Не удалось загрузить корзину', true);
    }
}

function renderCart(cart) {
    const container = document.getElementById('cartContainer');
    container.innerHTML = '';

    if (!cart.items || cart.items.length === 0) {
        renderEmptyCart();
        return;
    }

    cart.items.sort((a, b) => a.orderIndex - b.orderIndex);

    cart.items.forEach(item => {
        const itemElement = document.createElement('div');
        itemElement.className = 'cart-item';
        itemElement.innerHTML = `
                    <div class="item-details">
                        <span class="item-name">${item.dish ? item.dish.name : item.complexLunch.name}</span>
                        <span class="item-price">${item.price} ₽</span>
                    </div>
                    <div class="item-quantity">
                        <button class="quantity-btn" onclick="updateQuantity(${item.id}, ${Math.max(1, item.quantity - 1)})">-</button>
                        <input type="number" class="quantity-input" value="${item.quantity}" min="1" onchange="updateQuantity(${item.id}, this.value)">
                        <button class="quantity-btn" onclick="updateQuantity(${item.id}, ${item.quantity + 1})">+</button>
                    </div>
                    <button class="remove-item" onclick="removeItem(${item.id})">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <path d="M3 6h18"></path>
                            <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path>
                            <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path>
                        </svg>
                    </button>
                `;
        container.appendChild(itemElement);
    });

    const totalElement = document.createElement('div');
    totalElement.className = 'cart-total';
    totalElement.textContent = `Итого: ${cart.totalPrice} ₽`;
    container.appendChild(totalElement);

    const checkoutButton = document.createElement('button');
    checkoutButton.className = 'checkout-btn';
    checkoutButton.innerHTML = `
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-right: 8px;">
                    <circle cx="9" cy="21" r="1"></circle>
                    <circle cx="20" cy="21" r="1"></circle>
                    <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
                </svg>
                Оформить заказ
            `;
    checkoutButton.onclick = showOrderModal;
    container.appendChild(checkoutButton);
}

function renderEmptyCart() {
    const container = document.getElementById('cartContainer');
    container.innerHTML = `
                <div class="empty-cart">
                    <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <circle cx="9" cy="21" r="1"></circle>
                        <circle cx="20" cy="21" r="1"></circle>
                        <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
                    </svg>
                    <p>Ваша корзина пуста</p>
                    <a href="/" class="return-to-shop">Вернуться к покупкам</a>
                </div>
            `;
}

async function updateQuantity(itemId, newQuantity) {
    newQuantity = Math.max(1, parseInt(newQuantity, 10));
    try {
        const response = await fetch(`/api/cart/${cartId}/items/${itemId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ quantity: newQuantity })
        });

        if (!response.ok) {
            throw new Error('Ошибка при обновлении количества');
        }

        const updatedCart = await response.json();
        renderCart(updatedCart);
        showNotification('Количество обновлено');
    } catch (error) {
        console.error('Ошибка при обновлении количества:', error);
        showNotification('Не удалось обновить количество', true);
    }
}

async function removeItem(itemId) {
    try {
        const response = await fetch(`/api/cart/${cartId}/items/${itemId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка при удалении товара');
        }

        const updatedCart = await response.json();
        renderCart(updatedCart);
        showNotification('Товар удален из корзины');
    } catch (error) {
        console.error('Ошибка при удалении товара:', error);
        showNotification('Не удалось удалить товар', true);
    }
}

function showOrderModal() {
    document.getElementById('orderModal').style.display = 'block';
}

document.querySelector('.close').onclick = function() {
    document.getElementById('orderModal').style.display = 'none';
}

window.onclick = function(event) {
    if (event.target == document.getElementById('orderModal')) {
        document.getElementById('orderModal').style.display = 'none';
    }
}

// Обработка формы заказа
document.getElementById('orderForm').onsubmit = async function (e) {
    e.preventDefault();

    const form = e.target;
    const submitButton = form.querySelector('button[type="submit"]');
    submitButton.disabled = true;
    submitButton.textContent = "Оформление...";

    const formData = new FormData(form);
    orderData = Object.fromEntries(formData.entries()); // Сохраняем данные заказа

    // Закрываем модальное окно и показываем окно оплаты
    document.getElementById('orderModal').style.display = 'none';
    showPaymentModal();

    submitButton.disabled = false;
    submitButton.textContent = "Оформить заказ";
};

function showPaymentModal() {
    const modalHtml = `
        <div id="paymentModal" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Оплата заказа</h2>
                <div class="payment-details">
                    <p>Для оплаты заказа переведите сумму на карту Сбербанка:</p>
                    <div class="card-info">
                        <p class="card-number">5555 5555 5555 5555</p>
                        <p class="phone-number">Телефон: +7 (999) 999-99-99</p>
                        <p class="phone-number">Смирнов А А</p>
                    </div>
                    <button id="confirmPayment" class="confirm-payment-btn" onclick="confirmPayment()">
                        Подтвердить оплату
                    </button>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalHtml);

    const modal = document.getElementById('paymentModal');
    modal.style.display = 'block';

    const closeBtn = modal.querySelector('.close');
    closeBtn.onclick = () => modal.style.display = 'none';

    window.onclick = (event) => {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    };
}

async function confirmPayment() {
    const confirmButton = document.getElementById('confirmPayment');
    confirmButton.disabled = true;
    confirmButton.textContent = 'Подтверждение...';

    try {
        // Проверяем наличие cartId и orderData
        if (!cartId || !orderData) {
            throw new Error('Отсутствуют данные для оформления заказа');
        }

        // Создаем заказ на сервере
        const orderResponse = await fetch('/api/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                cartId: cartId,
                ...orderData
            })
        });

        if (!orderResponse.ok) {
            const errorResponse = await orderResponse.json();
            throw new Error(errorResponse.message || 'Ошибка при оформлении заказа');
        }

        const order = await orderResponse.json();

        // Подтверждаем оплату
        const paymentResponse = await fetch(`/api/payments/confirm/${order.id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!paymentResponse.ok) {
            const errorResponse = await paymentResponse.json();
            throw new Error(errorResponse.message || 'Ошибка при подтверждении оплаты');
        }

        showNotification('Оплата подтверждена! Спасибо за заказ.');
        localStorage.removeItem('cartId');
        cartId = null;

        // Закрываем модальное окно
        const modal = document.getElementById('paymentModal');
        if (modal) {
            modal.style.display = 'none';
        }

        // Перенаправляем на главную страницу
        setTimeout(() => {
            window.location.href = '/';
        }, 1000);
    } catch (error) {
        console.error('Ошибка при подтверждении оплаты:', error);
        showNotification('Не удалось подтвердить оплату: ' + error.message, true);
    } finally {
        confirmButton.disabled = false;
        confirmButton.textContent = 'Подтвердить оплату';
    }
}
document.addEventListener('DOMContentLoaded', loadCart);