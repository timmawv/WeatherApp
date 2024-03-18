function addToFavorite() {
    var buttons = document.querySelectorAll('.button');

    buttons.forEach(function (button) {
        button.addEventListener('click', function () {

            event.preventDefault(); // Предотвращаем отправку формы по умолчанию

            var form = button.closest('form');
            var latitude = form.querySelector('#latitudeId').value;
            var longitude = form.querySelector('#longitudeId').value;
            var cityName = form.querySelector('#cityNameId').value;
            var userId = form.querySelector('#userId').value;

            var isActive = button.classList.contains('active')

            if (isActive) {
                fetch('/WeatherApp-1.0/weather/search', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({latitude, longitude})
                })
                    .then(response => {
                        console.log(response);
                        if (!response.ok) {
                            throw new Error('error HTTP: ' + response.status);
                        }
                    })
                    .then(data => {
                        console.log('Successfully response:', data);
                        button.classList.remove('active');
                        alert("Your location was successfully removed!");
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                        alert(error.message || 'Something went wrong');
                    });
            } else {

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
                        button.classList.add('active');
                        alert("Your location was successfully saved!");
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                        alert(error.message || 'This location was already saved');
                    });
            }
        });
    });
}