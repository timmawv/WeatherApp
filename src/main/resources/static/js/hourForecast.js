const tempHourElement = document.querySelector('.temp').value;
let tempHourArray;
tempHourArray = JSON.parse(tempHourElement);
const maxHourTempValue = Math.max(...tempHourArray) + 10;
const minHourTempValue = Math.min(...tempHourArray) - 10;

let celsiusSing = "\u00B0C";

let timeHourElement = document.querySelector('.time').value;
timeHourElement = timeHourElement.slice(1, -1);
const timeHourArray = timeHourElement.split(',');

let mainHourWeather = document.querySelector('.mainWeather').value;
mainHourWeather = mainHourWeather.slice(1, -1);
const mainHourWeatherArray = mainHourWeather.split(',');

let descriptionsHour = document.querySelector('.descriptions').value;
descriptionsHour = descriptionsHour.slice(1, -1);
const descriptionsHourArray = descriptionsHour.split(',');

new Chart("hourlyChart", {
    type: "line",
    data: {
        labels: timeHourArray,
        datasets: [{
            data: tempHourArray,
            borderColor: "red",
            fill: 'start',
            backgroundColor: 'rgba(255, 99, 132, 0.2)'
        }]
    },
    options: {
        scales: {
            y: {
                min: minHourTempValue,
                max: maxHourTempValue,
                beginAtZero: false
            },
        },
        plugins: {
            title: {
                display: true,
                text: 'Hourly forecast'
            },
            legend: {
                display: false
            },
            tooltip: {
                callbacks: {
                    title: function (tooltipItems) {
                        const tooltipItem = tooltipItems[0];
                        const index = tooltipItem.dataIndex * 2;
                        return `Weather: ${mainHourWeatherArray[index]}, ${mainHourWeatherArray[index + 1]}`;
                    },
                    label: function (context) {
                        let label = context.dataset.data[context.dataIndex];
                        return `Time: ${context.label}, Temperature: ${label}${celsiusSing}`;
                    },
                    footer: function (tooltipItems) {
                        const index = tooltipItems[0].dataIndex;
                        return `Detailed: ${descriptionsHourArray[index]}`;
                    }
                },
                titleFont: {
                    size: 14,
                    family: 'Arial'
                },
                footerFont: {
                    size: 14,
                    family: 'Arial'
                },
            }
        }
    }
});