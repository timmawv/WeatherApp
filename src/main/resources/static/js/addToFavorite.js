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
                            return response.json().then(errorData => {
                                throw new Error("Error: " + errorData);
                            });
                        }
                    })
                    .then(data => {
                        console.log('Successfully response:', data);
                        button.classList.toggle('active');
                        setTimeout(function () {
                            alert("Your location was successfully removed!");
                        }, 1000);
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                        alert(error.message);
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
                            return response.json().then(errorData => {
                                console.log(errorData);
                                throw new Error("Error: " + errorData);
                            });
                        }
                    })
                    .then(data => {
                        console.log('Successfully response:', data);
                        button.classList.toggle('active');
                        setTimeout(function () {
                            alert("Your location was successfully saved!");
                        }, 1000);
                    })
                    .catch(error => {
                        console.error('Ошибка:', error);
                        alert(error.message);
                    });
            }
        });
    });
}