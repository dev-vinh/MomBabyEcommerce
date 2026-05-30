    let revenueChart = null;

function initDashboardChart(labels, data) {
    const ctx = document.getElementById('revenueChart');
    if (!ctx) return;

    if (revenueChart) revenueChart.destroy();
    const maxVal = Math.max(...data, 1);

    revenueChart = new Chart(ctx.getContext('2d'), {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh Thu',
                data: data,
                backgroundColor: function(context) {
                    const chart = context.chart;
                    const { ctx: c, chartArea } = chart;
                    if (!chartArea) return 'rgba(99,102,241,0.8)';
                    const gradient = c.createLinearGradient(0, chartArea.bottom, 0, chartArea.top);
                    gradient.addColorStop(0, 'rgba(99,102,241,0.4)');
                    gradient.addColorStop(1, 'rgba(99,102,241,0.9)');
                    return gradient;
                },
                borderColor: '#6366F1',
                borderWidth: 0,
                borderRadius: 8,
                borderSkipped: false,
                hoverBackgroundColor: '#4F46E5'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: '#1E293B',
                    titleColor: '#94A3B8',
                    bodyColor: '#F8FAFC',
                    padding: 14,
                    displayColors: false,
                    cornerRadius: 10,
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
                    grid: { display: false },
                    border: { display: false },
                    ticks: {
                        color: '#94A3B8',
                        font: { size: 11, weight: '500' }
                    }
                },
                y: {
                    grid: { color: '#F1F5F9', drawBorder: false },
                    border: { display: false, dash: [4, 4] },
                    ticks: {
                        color: '#94A3B8',
                        font: { size: 11 },
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