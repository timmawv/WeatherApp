function viewButton() {

    const buttonOne = document.querySelector('.btn.btn-outline-primary.one');
    const buttonTwo = document.querySelector('.btn.btn-outline-primary.two');

    const forecastBlocksFull = document.querySelectorAll(".forecast.full");
    const forecastBlocksShort = document.querySelectorAll(".forecast.short");

    buttonOne.addEventListener('click', function () {
        const isActive = buttonOne.classList.contains('active');
        if (!isActive) {
            buttonOne.classList.toggle('active');
            buttonTwo.classList.remove('active');
            forecastBlocksFull.forEach(function (block) {
                block.style.display = "inline-block";
            });
            forecastBlocksShort.forEach(function (block) {
                block.style.display = "none";
            });
        }
    });

    buttonTwo.addEventListener('click', function () {
        const isActive = buttonTwo.classList.contains('active');
        if (!isActive) {
            buttonOne.classList.remove('active');
            buttonTwo.classList.toggle('active');
            forecastBlocksFull.forEach(function (block) {
                block.style.display = "none";
            });
            forecastBlocksShort.forEach(function (block) {
                block.style.display = "inline-block";
            });
        }
    });
}