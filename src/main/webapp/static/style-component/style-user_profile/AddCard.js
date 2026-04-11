function openAddCardOverlay() {
    document.getElementById("add-card-overlay").style.display = "block";
}


function closeAddCardOverlay() {
    document.getElementById("add-card-overlay").style.display = "none";
}


window.onclick = function(event) {
    const overlay = document.getElementById("add-card-overlay");
    const container = document.querySelector(".add-card-container");
    if (event.target === overlay) {
        closeAddCardOverlay();
    }

}


$(document).ready(function() {
    const card_form = $("#card_form");
    card_form.submit(function(event) {
        event.preventDefault();
        const formData = new FormData(this);


        const payload={
            name: $("#name").val(),
            cardNumber: $("#card-number").val(),
            expiry: $("#expiry").val(),
            cvv: $("#cvv").val(),
            primaryCard: $("#primary-card").is(":checked")
        }

        fetch("add-card", {
            method: "POST",
            body: JSON.stringify(payload),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(function(response) {
            if (response.ok) {
                window.location.reload();
                return response.json();
            }
        })

    })
})