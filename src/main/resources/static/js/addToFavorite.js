function addToFavorite() {
    var buttons = document.querySelectorAll('.button');

    buttons.forEach(function(button) {
        button.addEventListener('click', function() {
            // Получаем данные из формы
            var latitude = document.getElementById('latitudeId').value;
            var longitude = document.getElementById('longitudeId').value;
            var cityName = document.getElementById('cityNameId').value;

            // Отправляем POST-запрос с использованием Fetch API
            fetch('/WeatherApp-1.0/weather/search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ latitude, longitude, cityName })
            })
                .then(response => {
                    console.log(response);
                    if (!response.ok) {
                        throw new Error('error HTTP: ' + response.status);
                    }
                })
                .then(data => {
                    console.log('Successfully response:', data);
                    var isActive = button.classList.contains('active');
                    // Если класс active уже есть, удаляем его
                    if (isActive) {
                        button.classList.remove('active');
                        alert("Your location was successfully removed!");
                    }
                    // Иначе добавляем класс active
                    else {
                        button.classList.add('active');
                        alert("Your location was successfully saved!");
                    }
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    var isActive = button.classList.contains('active');
                    if (isActive) {
                        alert("Your location wasn't  removed!");
                    }
                    else {
                        alert("Your location wasn't saved!");
                    }
                });
        });
    });
}