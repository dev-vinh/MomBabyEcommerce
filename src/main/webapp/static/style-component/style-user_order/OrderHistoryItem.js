$(document).ready(function () {
    const viewDetail = $('.btn_detail')

    viewDetail.on('click', function () {
        const data = $(this).data('src')
        const message = {
            type: 'openOrderHistory',
            src: data
        };

        // console.log(message)
        // console.log(data)
        window.parent.postMessage(message , '*');
        console.log("Da goi data")



    })
})