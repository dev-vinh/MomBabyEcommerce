document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('currentDate').textContent = new Date().toLocaleDateString('vi-VN', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });

    const ctx = document.getElementById('revenueChart');
    if (ctx) {
        const revenueChart = new Chart(ctx.getContext('2d'), {
            type: 'bar',
            data: {
                labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
                datasets: [{
                    label: 'Doanh Thu (VND)',
                    data: [65000000, 78000000, 85000000, 72000000, 95000000, 110000000, 98000000, 125000000, 140000000, 135000000, 150000000, 168000000],
                    backgroundColor: 'rgba(14, 165, 233, 0.8)',
                    borderColor: '#0ea5e9',
                    borderWidth: 1,
                    borderRadius: 6,
                    hoverBackgroundColor: '#0284c7'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        backgroundColor: '#1e293b',
                        titleColor: '#fff',
                        bodyColor: '#fff',
                        padding: 12,
                        displayColors: false,
                        callbacks: {
                            label: function(context) {
                                return new Intl.NumberFormat('vi-VN').format(context.raw) + ' VND';
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        },
                        ticks: {
                            color: '#64748b',
                            font: {
                                size: 11
                            }
                        }
                    },
                    y: {
                        grid: {
                            color: '#e2e8f0'
                        },
                        ticks: {
                            color: '#64748b',
                            font: {
                                size: 11
                            },
                            callback: function(value) {
                                return (value / 1000000) + 'M';
                            }
                        }
                    }
                }
            }
        });
    }

    document.querySelectorAll('.chart-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            document.querySelectorAll('.chart-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
        });
    });
});
