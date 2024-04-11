function viewButton() {

    var buttonOne = document.querySelector('.btn.btn-light.one');
    var buttonTwo = document.querySelector('.btn.btn-light.two');

    buttonOne.addEventListener('click', function () {
        var isActive = buttonOne.classList.contains('active');
        if (!isActive) {
            buttonOne.classList.toggle('active');
            buttonTwo.classList.remove('active');
        }
    });

    buttonTwo.addEventListener('click', function () {
        var isActive = buttonTwo.classList.contains('active');
        if (!isActive) {
            buttonOne.classList.remove('active');
            buttonTwo.classList.toggle('active');
        }
    });
}