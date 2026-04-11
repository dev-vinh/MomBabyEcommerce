// đóng mở menu
document.querySelectorAll('.menu_item').forEach(item => {
    item.addEventListener('click', (event) => {
        event.stopPropagation();
        document.querySelectorAll('.menu_item').forEach(otherItem => {
            if (otherItem !== item) {
                otherItem.classList.remove('active');
                const otherSubmenu = otherItem.querySelector('.submenu');
                if (otherSubmenu) {
                    otherSubmenu.style.display = 'none';
                }
            }
        });



        // submenu
        const submenu = item.querySelector('.submenu');
        if (submenu) {
            const submenuItems = submenu.querySelectorAll('.submenu_item');
            submenuItems.forEach((submenuItem) => {
                console.log(submenuItem);
                if (submenuItem.classList.contains('active')) {
                    submenuItem.classList.remove('active');
                }
            });

            const isOpen = submenu.style.display === 'flex';
            submenu.style.display = isOpen ? 'none' : 'flex';
            item.classList.toggle('active', !isOpen);
        } else {

            document.querySelectorAll('.menu_item').forEach(otherItem => {
                otherItem.classList.remove('active');
            });
            item.classList.add('active');
        }
    });
});


//sự kiện submenu
document.querySelectorAll('.submenu li').forEach(submenuItem => {
    submenuItem.addEventListener('click', (event) => {
        event.stopPropagation();

        document.querySelectorAll('.submenu li').forEach(link => {
            link.classList.remove('active');
        });
        submenuItem.classList.add('active');
    });
});