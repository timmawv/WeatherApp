function addToFavorite() {
    var buttons = document.querySelectorAll('.button');

    buttons.forEach(function (button) {
        button.addEventListener('click', function () {

            var form = button.closest('form');
            var latitude = form.querySelector('#latitudeId').value;
            var longitude = form.querySelector('#longitudeId').value;
            var cityName = form.querySelector('#cityNameId').value;
            var userId = form.querySelector('#userId').value;

            fetch('/WeatherApp-1.0/weather/search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({latitude, longitude, cityName, userId})
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
                        alert(error);
                    } else {
                        alert(error);
                    }
                });
        });
    });
}