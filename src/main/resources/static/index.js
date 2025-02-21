document.addEventListener('DOMContentLoaded', () => {
    // Добавляем класс для анимации появления
    document.querySelectorAll('.content-wrapper').forEach((wrapper, index) => {
        setTimeout(() => {
            wrapper.style.opacity = '1';
            wrapper.style.transform = 'translateY(0)';
        }, index * 200);
    });

    // Плавное изменение прозрачности при наведении
    document.querySelectorAll('.hero-section').forEach(section => {
        section.addEventListener('mouseenter', () => {
            section.style.flex = '1.2';
            section.querySelector('.content-wrapper').style.opacity = '1';
        });

        section.addEventListener('mouseleave', () => {
            section.style.flex = '1';
            section.querySelector('.content-wrapper').style.opacity = '0.9';
        });
    });
});
// Функция для обновления счетчика корзины
async function updateCartCount() {
    const cartId = localStorage.getItem('cartId');
    if (!cartId) {
        document.getElementById('cartCount').textContent = '0';
        return;
    }

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
document.addEventListener('DOMContentLoaded', function () {
    const aboutButton = document.getElementById('aboutButton');
    const aboutModal = document.getElementById('aboutModal');
    const closeModal = document.querySelector('.close');

    // Открываем модальное окно при нажатии на кнопку
    aboutButton.addEventListener('click', function () {
        aboutModal.style.display = 'flex';
    });

    // Закрываем модальное окно при нажатии на крестик
    closeModal.addEventListener('click', function () {
        aboutModal.style.display = 'none';
    });

    // Закрываем модальное окно при клике вне его
    window.addEventListener('click', function (event) {
        if (event.target === aboutModal) {
            aboutModal.style.display = 'none';
        }
    });
});
// Обновляем счетчик при загрузке страницы
document.addEventListener('DOMContentLoaded', updateCartCount);