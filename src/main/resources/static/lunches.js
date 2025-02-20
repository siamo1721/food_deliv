let cartId = localStorage.getItem('cartId');

const menuData = {
    'Понедельник': {
        image: 'https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&q=80&w=800&h=600',
        items: [
            {
                name: 'Грибной (250 мл)',
                description: 'Грибы, картофель, морковь, лук, специи',
                calories: 128.5
            },
            {
                name: 'Рис с гуляшом из свинины (150/100 гр)',
                description: 'Свинина, рис, лук, морковь, специи',
                calories: 485.5
            },
            {
                name: 'Столичный (150 гр)',
                description: 'Курица, картофель, морковь, огурцы, яйца, горошек, майонез',
                calories: 344.3
            }
        ]
    },
    'Вторник': {
        image: 'https://images.unsplash.com/photo-1512058564366-18510be2db19?auto=format&fit=crop&q=80&w=800&h=600',
        items: [
            {
                name: 'Картофельный с фрикадельками (250 мл)',
                description: 'Картофель, фрикадельки, морковь, лук, специи',
                calories: 347.5
            },
            {
                name: 'Макароны с сосиской (150/70 гр)',
                description: 'Макароны, сосиски, масло',
                calories: 291.4
            },
            {
                name: 'Свекла с чесноком (150 гр)',
                description: 'Свекла, чеснок, масло растительное',
                calories: 176.8
            }
        ]
    },
    'Среда': {
        image: 'https://images.unsplash.com/photo-1574653853027-5382a3d23c10?auto=format&fit=crop&q=80&w=800&h=600',
        items: [
            {
                name: 'Рыбный (минтай) (250 гр)',
                description: 'Минтай, картофель, морковь, лук, специи',
                calories: 189.5
            },
            {
                name: 'Гречневая каша с мясом (150/70 гр)',
                description: 'Гречка, говядина, лук, морковь, специи',
                calories: 290.5
            },
            {
                name: 'Из свежей капусты (150 гр)',
                description: 'Капуста, морковь, масло растительное',
                calories: 222.3
            }
        ]
    },
    'Четверг': {
        image: 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&q=80&w=800&h=600',
        items: [
            {
                name: 'Щи из свежей капусты (250 гр)',
                description: 'Капуста, картофель, морковь, лук, специи',
                calories: 142.5
            },
            {
                name: 'Макароны с тефтелями с том/соус (150/70 гр)',
                description: 'Макароны, тефтели, томатный соус',
                calories: 250
            },
            {
                name: 'Из свеж. капусты с овощами (150 гр)',
                description: 'Капуста, морковь, огурцы, помидоры, масло растительное',
                calories: 238.3
            }
        ]
    },
    'Пятница': {
        image: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&q=80&w=800&h=600',
        items: [
            {
                name: 'Рассольник (250 гр)',
                description: 'Огурцы солёные, перловка, картофель, морковь, лук',
                calories: 244.5
            },
            {
                name: 'Картоф. пюре с котлетой (150/70 гр)',
                description: 'Картофель, молоко, масло, котлета',
                calories: 292.3
            },
            {
                name: 'Из свежих овощей (150 гр)',
                description: 'Помидоры, огурцы, перец, масло растительное',
                calories: 208.3
            }
        ]
    }
};

function createWeekGrid() {
    const grid = document.getElementById('weekGrid');
    Object.keys(menuData).forEach(day => {
        const card = document.createElement('div');
        card.className = 'day-card';
        card.innerHTML = `
                    <div class="day-header">${day}</div>
                    <div class="day-content" style="background-image: url('${menuData[day].image}')">
                        <p>Нажмите, чтобы посмотреть меню</p>
                    </div>
                `;
        card.onclick = () => showDayMenu(day);
        grid.appendChild(card);
    });
}

function showDayMenu(day) {
    const modal = document.getElementById('menuModal');
    const content = document.getElementById('modalContent');
    const dayMenu = menuData[day];

    let menuItemsHTML = '';
    dayMenu.items.forEach(item => {
        menuItemsHTML += `
                    <div class="menu-item">
                        <h3>${item.name}</h3>
                        <p>${item.description}</p>
                        <p>Калории: ${item.calories} ккал</p>
                    </div>
                `;
    });

    content.innerHTML = `
                <h2 style="color: #ff7b00; margin-bottom: 20px;">${day}</h2>
                ${menuItemsHTML}
                <div class="drink-options">
                    <h3>Выберите напиток:</h3>
                    <div class="radio-group">
                        <label class="radio-label">
                            <input type="radio" name="drink" value="none" checked>
                            Без напитка
                        </label>
                        <label class="radio-label">
                            <input type="radio" name="drink" value="morse">
                            Морс
                        </label>
                        <label class="radio-label">
                            <input type="radio" name="drink" value="compote">
                            Компот
                        </label>
                        <label class="radio-label">
                            <input type="radio" name="drink" value="tea">
                            Чай
                        </label>
                    </div>
                </div>

                <div class="bread-options">
                    <h3>Выберите хлеб:</h3>
                    <div class="radio-group">
                        <label class="radio-label">
                            <input type="radio" name="bread" value="none" checked>
                            Без хлеба
                        </label>
                        <label class="radio-label">
                            <input type="radio" name="bread" value="white">
                            Белый
                        </label>
                        <label class="radio-label">
                            <input type="radio" name="bread" value="black">
                            Черный
                        </label>
                    </div>
                </div>

                <div class="quantity-control">
                    <h3>Количество:</h3>
                    <button class="quantity-btn" onclick="decrementQuantity()">-</button>
                    <input type="number" id="quantity" class="quantity-input" value="1" min="1" max="10">
                    <button class="quantity-btn" onclick="incrementQuantity()">+</button>
                </div>

                <div id="totalPrice" style="margin-top: 15px; font-weight: bold;">Итого: 350 руб.</div>

                <button class="add-to-cart" onclick="addToCart()">
                    Добавить в корзину
                </button>
            `;

    modal.style.display = 'block';
    updatePrice();

    // Добавляем обработчики событий для обновления цены
    document.querySelectorAll('input[name="bread"], input[name="drink"]').forEach(radio => {
        radio.addEventListener('change', updatePrice);
    });

    document.getElementById('quantity').addEventListener('change', updatePrice);
}

function closeModal() {
    document.getElementById('menuModal').style.display = 'none';
}

function incrementQuantity() {
    const input = document.getElementById('quantity');
    if (input.value < 10) {
        input.value = parseInt(input.value) + 1;
        updatePrice();
    }
}

function decrementQuantity() {
    const input = document.getElementById('quantity');
    if (input.value > 1) {
        input.value = parseInt(input.value) - 1;
        updatePrice();
    }
}

function updatePrice() {
    const quantity = parseInt(document.getElementById('quantity').value);
    const basePrice = 350;
    const breadPrice = 15;
    const drinkPrice = 30;

    const selectedBread = document.querySelector('input[name="bread"]:checked').value;
    const selectedDrink = document.querySelector('input[name="drink"]:checked').value;

    let totalPrice = basePrice * quantity;

    if (selectedBread !== "none") {
        totalPrice += breadPrice * quantity;
    }

    if (selectedDrink !== "none") {
        totalPrice += drinkPrice * quantity;
    }

    document.getElementById('totalPrice').textContent = `Итого: ${totalPrice} руб.`;
}

function showNotification(message, isError = false) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification ${isError ? 'error' : ''}`;
    notification.classList.add('show');

    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

async function getOrCreateCart() {
    if (!cartId) {
        try {
            const response = await fetch('/api/cart', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Не удалось создать корзину');
            }

            const cart = await response.json();
            cartId = cart.id;
            localStorage.setItem('cartId', cartId);
        } catch (error) {
            console.error('Ошибка при создании корзины:', error);
            throw error;
        }
    }
    return cartId;
}

async function updateCartCount() {
    if (!cartId) return;

    try {
        const response = await fetch(`/api/cart/${cartId}`);
        if (response.ok) {
            const cart = await response.json();
            const itemCount = cart.items.reduce((sum, item) => sum + item.quantity, 0);
            document.getElementById('cartCount').textContent = itemCount.toString();
        }
    } catch (error) {
        console.error('Ошибка при получении данных корзины:', error);
    }
}

async function addToCart() {
    const quantity = parseInt(document.getElementById('quantity').value);
    const drink = document.querySelector('input[name="drink"]:checked').value;
    const bread = document.querySelector('input[name="bread"]:checked').value;

    try {
        const currentCartId = await getOrCreateCart();
        const response = await fetch(`/api/cart/${currentCartId}/complex-lunches/1`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                quantity,
                options: {
                    drink,
                    bread
                }
            })
        });

        if (!response.ok) {
            throw new Error('Ошибка при добавлении в корзину');
        }

        await updateCartCount();
        closeModal();
        showNotification('Обед успешно добавлен в корзину!');
    } catch (error) {
        console.error('Ошибка при добавлении в корзину:', error);
        showNotification(error.message, true);
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    createWeekGrid();
    await getOrCreateCart();
    await updateCartCount();
});

window.onclick = function(event) {
    const modal = document.getElementById('menuModal');
    if (event.target === modal) {
        closeModal();
    }
}