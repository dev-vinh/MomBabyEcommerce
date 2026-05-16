function cancelOrder(orderId) {
    fetch('cancel-order?orderId=' + orderId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(r => r.json())

        .then(data => {
            console.log(data);
            alert("Đơn hàng đã được hủy thành công!")
            window.location.reload();
        })
        .catch(err => console.log(err));

}