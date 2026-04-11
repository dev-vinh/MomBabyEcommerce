
function toggleMoreActionMenu(icon) {
    // Tìm đối tượng menu trong cùng thẻ cha
    const container = icon.parentElement;
    const menu = container.querySelector(".more_action_menu");
    if (menu) {
        menu.classList.toggle("hidden");
    }
}

// Popup Invite Member
document.addEventListener("DOMContentLoaded", function () {
    const inviteBtn = document.getElementById("invite");
    const inviteModal = document.getElementById("inviteModal");
    const closeBtn = document.querySelector(".close_btn");
    const cancelInvite = document.getElementById("cancelInvite");
    const inviteForm = document.getElementById("inviteForm");
    const gearIcons = document.querySelectorAll(".more_action i.fa-gear");
    const menus = document.querySelectorAll(".more_action_menu");


    menus.forEach(function (menu) {
        menu.classList.add("hidden");
    });

    gearIcons.forEach(function (icon) {
        icon.addEventListener("click", function (event) {
            event.stopPropagation();
            toggleMoreActionMenu(this);
        });
    });

    inviteBtn.addEventListener("click", function () {
        inviteModal.classList.remove("hidden");
    });

    closeBtn.addEventListener("click", function () {
        inviteModal.classList.add("hidden");
    });

    cancelInvite.addEventListener("click", function (e) {
        e.preventDefault();
        inviteModal.classList.add("hidden");
    });


    inviteForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const email = document.getElementById("inviteEmail").value.trim();
        const name = document.getElementById("inviteName").value.trim();
        const roleSelect = document.getElementById("inviteRole");
        const roleId = roleSelect.value;
        const roleName = roleSelect.options[roleSelect.selectedIndex].text;
        payload = {
            email: email, name: name, roleId: roleId, roleName: roleName,
        }

        console.log(payload)

        fetch("member/invite", {
            method: "POST", headers: {
                "Content-Type": "application/json",

            }, body: JSON.stringify(payload), redirect: "manual"
        })
            .then(res => {
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
                alert("Invitation sent!");
                inviteModal.classList.add("hidden");
            })


    });


    const roleSelects = document.querySelectorAll(".role_list");

    roleSelects.forEach(select => {
        let currentRole = select.getAttribute("data-role-current");
        const member = select.closest("tr");
        console.log(member);


        if (member) {
            const memberId = member.getAttribute("data-member-id");
            console.log(memberId);


            select.addEventListener("change", function (event) {
                const newRole = event.target.value;

                if (newRole !== currentRole) {
                    const confirmChange = confirm("Bạn có chắc chắn muốn đổi vai trò?");
                    if (!confirmChange) {
                        event.target.value = currentRole;
                    } else {
                        const roleId = event.target.selectedOptions[0].dataset.roleId;
                        const request = {
                            "memberId": memberId, "roleId": roleId
                        }

                        fetch("/admin/member/change-role", {
                            method: "POST", headers: {
                                "Content-Type": "application/json",
                            }, body: JSON.stringify(request),
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
                                if (data && data.success) {
                                    currentRole = newRole;
                                    select.setAttribute("data-role-current", newRole);
                                    alert("Success!");
                                }
                            })


                    }
                }
            });
        }
    });


});
