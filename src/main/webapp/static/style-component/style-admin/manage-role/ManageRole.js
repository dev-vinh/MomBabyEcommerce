
document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("roleModal");
    const addBtn = document.getElementById("addRoleBtn");
    const closeBtn = document.getElementById("closeRoleModal");
    const cancelBtn = document.getElementById("cancelAddRole");
    const form = document.getElementById("roleForm");

    addBtn.addEventListener("click", () => {
        modal.classList.remove("hidden");
    });

    closeBtn.addEventListener("click", () => {
        modal.classList.add("hidden");
    });

    cancelBtn.addEventListener("click", () => {
        modal.classList.add("hidden");
    });

    form.addEventListener("submit", (e) => {
        e.preventDefault();

        const roleName = form.elements["name"].value;
        const description =form.elements["description"].value;
        const permissionIds = Array.from(document.querySelectorAll(".permission:checked"))
            .map(p  => p.getAttribute("data-id"));

        const payload = {
            roleName: roleName,
            description: description,
            permissionIds: permissionIds
        }

        fetch(`role`, {
            method: "POST",
            body: JSON.stringify(payload),
            redirect: "manual"
        }).then(res => {
            console.log("Status:", res.status);
            if (res.status === 0) {
                alert("Your session may have expired. Redirecting...");
                window.location.href = "login"
                return Promise.reject("Session expired");
            }

            return res.json();
        })
            .then(data => {
                console.log(data);
                if (data.success) {
                    alert("Role added!");
                    location.reload();
                }
                else{
                    alert("Failed to add role!");
                }
            })



        modal.classList.add("hidden");
    });
});
