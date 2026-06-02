    let revenueChart = null;

function initDashboardChart(labels, data) {
    const ctx = document.getElementById('revenueChart');
    if (!ctx) return;

    if (revenueChart) revenueChart.destroy();
    revenueChart = new Chart(ctx.getContext('2d'), {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh Thu',
                data: data,
                backgroundColor: '#2563EB',
                borderColor: '#1D4ED8',
                borderWidth: 0,
                borderRadius: 6,
                borderSkipped: false,
                hoverBackgroundColor: '#1D4ED8',
                barPercentage: 0.72,
                categoryPercentage: 0.7,
                maxBarThickness: 48
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            layout: {
                padding: {
                    top: 8,
                    right: 8,
                    bottom: 4,
                    left: 4
                }
            },
            plugins: {
                legend: { display: false },
                title: {
                    display: true,
                    text: 'Biểu đồ doanh thu',
                    color: '#111827',
                    align: 'start',
                    padding: {
                        bottom: 6
                    },
                    font: {
                        size: 16,
                        weight: '700'
                    }
                },
                subtitle: {
                    display: true,
                    text: 'Theo khoảng thời gian đã chọn',
                    color: '#6B7280',
                    align: 'start',
                    padding: {
                        bottom: 18
                    },
                    font: {
                        size: 12,
                        weight: '500'
                    }
                },
                tooltip: {
                    backgroundColor: '#111827',
                    titleColor: '#D1D5DB',
                    bodyColor: '#FFFFFF',
                    padding: 14,
                    displayColors: false,
                    cornerRadius: 12,
                    titleFont: { size: 11 },
                    bodyFont: { size: 14, weight: '700' },
                    callbacks: {
                        label: function(context) {
                            return new Intl.NumberFormat('vi-VN').format(context.raw) + ' đ';
                        }
                    }
                }
            },
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'Thời gian',
                        color: '#374151',
                        font: { size: 12, weight: '600' },
                        padding: { top: 10 }
                    },
                    grid: { display: false },
                    border: { display: false },
                    ticks: {
                        color: '#475569',
                        font: { size: 11, weight: '500' }
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Doanh thu (VNĐ)',
                        color: '#374151',
                        font: { size: 12, weight: '600' },
                        padding: { bottom: 10 }
                    },
                    grid: { color: '#E2E8F0', drawBorder: false },
                    border: { display: false, dash: [4, 4] },
                    ticks: {
                        color: '#475569',
                        font: { size: 11, weight: '500' },
                        padding: 8,
                        callback: function(value) {
                            if (value >= 1000000000) return (value / 1000000000).toFixed(1) + 'B';
                            if (value >= 1000000)    return (value / 1000000).toFixed(1) + 'M';
                            if (value >= 1000)        return (value / 1000).toFixed(0) + 'K';
                            return value;
                        }
                    }
                }
            },
            animation: {
                duration: 600,
                easing: 'easeOutQuart'
            }
        }
    });
}

function renderGrowthBadge(elementId, growth) {
    const el = document.getElementById(elementId);
    if (!el) return;

    const val = parseFloat(growth);
    const isUp = val > 0;
    const isZero = val === 0;

    if (isZero) {
        el.className = 'growth_badge neutral';
        el.innerHTML = `<i class="fa-solid fa-minus"></i> 0%`;
    } else if (isUp) {
        el.className = 'growth_badge up';
        el.innerHTML = `<i class="fa-solid fa-arrow-up"></i> +${val}%`;
    } else {
        el.className = 'growth_badge down';
        el.innerHTML = `<i class="fa-solid fa-arrow-down"></i> ${val}%`;
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const dateEl = document.getElementById('currentDate');
    if (dateEl) {
        dateEl.textContent = new Date().toLocaleDateString('vi-VN', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    const labels = window.chartLabels || [];
    const data   = window.chartData   || [];

    if (labels.length > 0) {
        initDashboardChart(labels, data);
    } else {
        const container = document.querySelector('.chart_container');
        if (container) {
            container.innerHTML = `
                <div style="display:flex;flex-direction:column;align-items:center;
                            justify-content:center;height:100%;color:#94A3B8;gap:10px;">
                    <i class="fa-regular fa-chart-bar" style="font-size:36px;"></i>
                    <p style="font-size:13px;">Chưa có dữ liệu doanh thu</p>
                </div>`;
        }
    }

    if (typeof window.revenueGrowth !== 'undefined') {
        renderGrowthBadge('revenueGrowthBadge', window.revenueGrowth);
    }
    if (typeof window.ordersGrowth !== 'undefined') {
        renderGrowthBadge('ordersGrowthBadge', window.ordersGrowth);
    }
    if (typeof window.cancelledGrowth !== 'undefined') {
        renderGrowthBadge('cancelledGrowthBadge', window.cancelledGrowth);
    }
});
