$(document).ready(function () {
    window.addEventListener('message', function (event) {
        if (event.data.type === 'openOrderHistory') {
            window.parent.postMessage(event.data, '*')
        }
    })
})