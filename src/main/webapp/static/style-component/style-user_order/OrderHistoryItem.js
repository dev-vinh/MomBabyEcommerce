function cancelOrder(orderId) {
    if (!confirm("Bạn có chắc muốn hủy đơn hàng này không?")) {
        return;
    }
    fetch('cancel-order?orderId=' + orderId, {
        method: 'POST'
    })
        .then(r => r.json())
        .then(data => {

            console.log(data);

            if (data.success) {
                alert(data.message);
                window.location.reload();
            } else {
                alert(data.message);
            }
        })
        .catch(err => {
            console.log(err);
            alert("Có lỗi xảy ra!");
        });
}