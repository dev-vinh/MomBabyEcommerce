// To do
const registerButton = document.querySelector(".register-button");
const loginButton = document.querySelector(".login-button");
const container = document.querySelector(".container");
const togglePasswords = document.querySelectorAll(".toggle-password");

registerButton.addEventListener("click", () => {
    container.classList.add("right-panel-active");
});
loginButton.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
});
togglePasswords.forEach((togglePassword) => {
    togglePassword.addEventListener("click", function () {
        const passwordInput = document.querySelector(
            this.getAttribute("data-toggle")
        );

        const type =
            passwordInput.getAttribute("type") === "password" ? "text" : "password";
        passwordInput.setAttribute("type", type);

        this.classList.toggle("fa-eye-slash");
    });
});