function viewButton() {

    const buttonOne = document.querySelector('.btn.btn-outline-primary.one');
    const buttonTwo = document.querySelector('.btn.btn-outline-primary.two');

    const hourlyChart = document.querySelector("#hourlyChart");
    const hourlyImages = document.querySelector(".hourly.images");
    const weeklyChart = document.querySelector("#weeklyChart");
    const weeklyImages = document.querySelector(".weekly.images");

    buttonOne.addEventListener('click', function () {
        const isActive = buttonOne.classList.contains('active');
        if (!isActive) {
            buttonOne.classList.toggle('active');
            buttonTwo.classList.remove('active');
            hourlyChart.style.display = "block";
            hourlyImages.style.display = "flex";
            weeklyChart.style.display = "none";
            weeklyImages.style.display = "none";
        }
    });

    buttonTwo.addEventListener('click', function () {
        const isActive = buttonTwo.classList.contains('active');
        if (!isActive) {
            buttonOne.classList.remove('active');
            buttonTwo.classList.toggle('active');
            hourlyChart.style.display = "none";
            hourlyImages.style.display = "none";
            weeklyChart.style.display = "block";
            weeklyImages.style.display = "flex";
        }
    });
}