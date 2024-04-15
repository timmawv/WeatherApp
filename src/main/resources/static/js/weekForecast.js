const tempElement = document.querySelector('.temp_weekly').value;
let tempArray;
tempArray = JSON.parse(tempElement);
const maxValue = Math.max(...tempArray) + 10;
const minValue = Math.min(...tempArray) - 10;

const timeElement = document.querySelector('.time_weekly');
let timeValue = timeElement.value;
timeValue = timeValue.slice(1, -1);
const timeArray = timeValue.split(',');

let mainWeather = document.querySelector('.mainWeatherWeekly').value;
mainWeather = mainWeather.slice(1, -1);
const mainWeatherArray = mainWeather.split(',');

let descriptions = document.querySelector('.descriptions_weekly').value;
descriptions = descriptions.slice(1, -1);
const descriptionsArray = descriptions.split(', ');

new Chart("weeklyChart", {
    type: "line",
    data: {
        labels: timeArray,
        datasets: [{
            data: tempArray,
            borderColor: "red",
            fill: 'start',
            backgroundColor: 'rgba(255, 99, 132, 0.2)'
        }]
    },
    options: {
        scales: {
            y: {
                min: minValue,
                max: maxValue,
                beginAtZero: false
            },
        },
        plugins: {
            title: {
                display: true,
                text: 'Weekly forecast'
            },
            legend: {
                display: false
            },
            tooltip: {
                callbacks: {
                    title: function (tooltipItems) {
                        const tooltipItem = tooltipItems[0];
                        const index = tooltipItem.dataIndex * 2;
                        return `Weather: ${mainWeatherArray[index]}, ${mainWeatherArray[index + 1]}`;
                    },
                    label: function (context) {
                        let label = context.dataset.data[context.dataIndex];
                        return `Date: ${context.label}, Temperature: ${label}${celsiusSing}`;
                    },
                    footer: function (tooltipItems) {
                        const index = tooltipItems[0].dataIndex;
                        return `Detailed: ${descriptionsArray[index]}`;
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