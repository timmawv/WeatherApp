function removeFavorite() {
    const buttons = document.querySelectorAll('.button');
    buttons.forEach(function (button) {

        const form = button.closest('form');
        button.addEventListener("click", function () {
            form.submit();
        });
    });
}