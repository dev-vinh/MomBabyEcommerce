window.showToast = function (message, type = 'error', duration = 2000) {
    let toast = $('#global-toast');

    if (toast.length === 0) {
        $('body').append(`
            <div id="global-toast" class="global-toast hidden">
                <span id="global-toast-message"></span>
            </div>
        `);

        toast = $('#global-toast');
    }

    const toastMessage = $('#global-toast-message');

    toast.removeClass('hidden success error warning info show');
    toast.addClass(type || 'error');
    toastMessage.text(message);

    setTimeout(function () {
        toast.addClass('show');
    }, 10);

    clearTimeout(window.globalToastTimeout);

    window.globalToastTimeout = setTimeout(function () {
        toast.removeClass('show');

        setTimeout(function () {
            toast.addClass('hidden');
        }, 300);
    }, duration);
};