function addToFavorite() {
    const buttons = document.querySelectorAll('.button');

    buttons.forEach(function (button) {
        button.addEventListener('click', function () {

            event.preventDefault(); // Предотвращаем отправку формы по умолчанию

            const form = button.closest('form');
            const latitude = form.querySelector('.latitudeId').value;
            const longitude = form.querySelector('.longitudeId').value;
            const cityName = form.querySelector('.cityNameId').value;

            const isActive = button.classList.contains('active')

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
                    body: JSON.stringify({latitude, longitude, cityName})
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