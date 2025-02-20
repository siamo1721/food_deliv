let cartId = localStorage.getItem('cartId');

function showNotification(message, isError = false) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification ${isError ? 'error' : ''}`;

    // Показываем уведомление
    setTimeout(() => {
        notification.classList.add('show');
    }, 100);

    // Скрываем через 3 секунды
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

async function loadDishes() {
    try {
        const response = await fetch('/api/dishes');
        const dishes = await response.json();
        const container = document.getElementById('dishesContainer');

        const categories = {
            'BREAKFAST': 'Завтраки',
            'DINNER': 'Ужины',
            'ADDITIONAL': 'Дополнительные блюда'
        };

        Object.entries(categories).forEach(([categoryKey, categoryName], index) => {
            const categoryDishes = dishes.filter(dish => dish.category === categoryKey);
            if (categoryDishes.length > 0) {
                const categoryContainer = document.createElement('div');
                categoryContainer.className = 'category-container';
                categoryContainer.style.animationDelay = `${index * 0.2}s`;

                categoryContainer.innerHTML = `
                        <h2>${categoryName}</h2>
                        <div class="dish-container" id="${categoryKey}Container"></div>
                    `;
                container.appendChild(categoryContainer);

                const dishContainer = document.getElementById(`${categoryKey}Container`);
                categoryDishes.forEach((dish, dishIndex) => {
                    const card = document.createElement('div');
                    card.className = 'dish-card';
                    card.style.animationDelay = `${dishIndex * 0.1}s`;

                    card.innerHTML = `
                            <img src="${dish.imageUrl || 'https://images.unsplash.com/photo-1546793665-c74683f339c1'}" alt="${dish.name}" class="dish-image">
                            <div class="dish-details">
                                <h3 class="dish-title">${dish.name}</h3>
                                <p class="dish-description">${dish.description}</p>
                                <p class="dish-price">${dish.price} ₽</p>
                                <button class="add-to-cart" onclick="showDishModal(${JSON.stringify(dish).replace(/"/g, '&quot;')})">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                        <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"></path>
                                        <line x1="3" y1="6" x2="21" y2="6"></line>
                                        <path d="M16 10a4 4 0 0 1-8 0"></path>
                                    </svg>
                                    Добавить в корзину
                                </button>
                            </div>
                        `;
                    dishContainer.appendChild(card);
                });
            }
        });
    } catch (error) {
        console.error('Ошибка при загрузке блюд:', error);
        showNotification('Ошибка при загрузке блюд', true);
    }
}

// Добавляем новые функции для работы с модальным окном
let currentDish = null;

function showDishModal(dish) {
    currentDish = dish;
    const modal = document.getElementById('dishModal');
    document.getElementById('modalDishName').textContent = dish.name;
    document.getElementById('modalDishDescription').textContent = dish.description;
    document.getElementById('modalDishPrice').textContent = `${dish.price} ₽`;
    document.getElementById('modalDishWeight').textContent = `${dish.weight} г`;
    document.getElementById('modalQuantity').value = 1;

    modal.style.display = 'block';

    // Обновляем обработчик события для кнопки "Добавить в корзину"
    document.getElementById('modalAddToCart').onclick = () => {
        const quantity = parseInt(document.getElementById('modalQuantity').value);
        addToCartFromModal(dish.id, quantity);
    };
}

function closeModal() {
    document.getElementById('dishModal').style.display = 'none';
    currentDish = null;
}

function incrementQuantity() {
    const input = document.getElementById('modalQuantity');
    if (input.value < 10) {
        input.value = parseInt(input.value) + 1;
    }
}

function decrementQuantity() {
    const input = document.getElementById('modalQuantity');
    if (input.value > 1) {
        input.value = parseInt(input.value) - 1;
    }
}

async function addToCartFromModal(dishId, quantity) {
    try {
        const currentCartId = await getOrCreateCart();
        const response = await fetch(`/api/cart/${currentCartId}/dishes/${dishId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ quantity: quantity })
        });

        if (!response.ok) {
            throw new Error('Ошибка при добавлении в корзину');
        }

        const updatedCart = await response.json();
        updateCartUI(updatedCart);
        showNotification('Товар успешно добавлен в корзину');
        closeModal();
    } catch (error) {
        console.error('Ошибка при добавлении в корзину:', error);
        showNotification(error.message, true);
    }
}

// Закрытие модального окна при клике вне его
window.onclick = function(event) {
    const modal = document.getElementById('dishModal');
    if (event.target === modal) {
        closeModal();
    }
}
async function addToCart(itemId, itemType) {
    try {
        const currentCartId = await getOrCreateCart();
        const response = await fetch(`/api/cart/${currentCartId}/${itemType}/${itemId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ quantity: 1 }) // Передаём quantity в теле запроса
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Ошибка при добавлении в корзину: ${errorText}`);
        }

        const updatedCart = await response.json();
        updateCartUI(updatedCart);
        showNotification('Товар успешно добавлен в корзину');
    } catch (error) {
        console.error('Ошибка при добавлении в корзину:', error);
        alert(error.message);
    }
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
async function createCart() {
    try {
        const response = await fetch('/api/cart', { method: 'POST' });
        const cart = await response.json();
        cartId = cart.id;
        localStorage.setItem('cartId', cartId);
    } catch (error) {
        console.error('Ошибка при создании корзины:', error);
    }
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
async function updateCartUI(cart) {
    const cartContainer = document.getElementById('cartContainer');
    cartContainer.innerHTML = ''; // Очищаем контейнер

    cart.items.forEach(item => {
        const itemElement = document.createElement('div');
        itemElement.className = 'cart-item';
        itemElement.innerHTML = `
            <p>${item.dish ? item.dish.name : item.complexLunch.name}</p>
            <p>Количество: ${item.quantity}</p>
            <p>Цена: ${item.price} ₽</p>
        `;
        cartContainer.appendChild(itemElement);
    });

    updateCartCount(cart.items.length);
}
async function updateQuantity(itemId, newQuantity) {
    try {
        const response = await fetch(`/api/cart/${cartId}/items/${itemId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ quantity: newQuantity })
        });

        if (response.ok) {
            loadCart(); // Обновляем корзину
        } else {
            throw new Error('Ошибка при обновлении количества');
        }
    } catch (error) {
        console.error('Ошибка при обновлении количества:', error);
    }
}
document.addEventListener('DOMContentLoaded', async () => {
    try {
        await getOrCreateCart();
        const response = await fetch(`/api/cart/${cartId}`);
        if (response.ok) {
            const cart = await response.json();
            updateCartCount(cart.items.length);
        }
    } catch (error) {
        console.error('Ошибка при инициализации корзины:', error);
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadDishes();
    updateCartCount();
});