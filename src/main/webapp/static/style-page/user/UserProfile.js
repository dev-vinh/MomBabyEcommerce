document.addEventListener('DOMContentLoaded', () => {

    window.addEventListener('message', (event) => {
        const iframe = document.getElementById('main_content'); // Đảm bảo rằng bạn có ít nhất một iframe trong document

        console.log( " event.data : " ,event.data);

        if (event.data.type === 'openUserProfile') {
            console.log('Thông điệp đã được nhận từ iframe:', event.data.src);

            if (iframe) {
                iframe.src = event.data.src;
                console.log('Đã thay đổi src của iframe thành:', iframe.src);

            } else {
                console.error('Không tìm thấy iframe trong document.');
            }
        }

        else if (event.data.type === 'openOrderHistory') {
            console.log(event.data.src)

            iframe.src = event.data.src;
        }

        else{
            console.log("Error");

        }



    });
});





