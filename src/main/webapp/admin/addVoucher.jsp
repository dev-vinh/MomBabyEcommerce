<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Voucher Mới - VoucherAdmin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/style-admin/vouchers/addVoucher.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Outlined" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style-component/global-typography.css">

</head>
<body>
<div class="wrap_header">
    <jsp:include page="header.jsp"/>
</div>
<div class="app-shell"> <!-- Sidebar -->
    <div class="side_bar">
        <jsp:include page="/admin/sidebar.jsp"/>
    </div>
     <!-- Main Content -->
    <main class="main-content">
        <div class="content-container">
            <div class="form-container">
                <div class="form-card">
                    <div class="form-header"><h2>${voucher == null ? 'Thêm Voucher' : 'Chỉnh sửa Voucher'}</h2>
                        <p>Vui lòng nhập đầy đủ các thông tin bắt buộc để phát hành mã giảm giá.</p></div>
                    <form id="voucherForm" class="voucher-form" method="post" action="${pageContext.request.contextPath}${voucher == null ?
                                                                                        '/admin/voucher-add' : '/admin/vouchers-edit'}">

                        <div class="form-main-grid"> <!-- Cột Trái -->
                            <div class="form-column">
                                <input type="hidden" name="id" value="${voucher.id}">
                                <div class="form-group"><label>Mã Voucher <span class="required">*</span></label>

                                    <input type="text" id="voucherCode" name="code" placeholder="VD: SUMMER2026" value="${voucher.code}" required>

                                    <small class="helper-text">Mã voucher nên ngắn gọn và dễ nhớ.</small></div>
                                <div class="form-group"><label>Tên chiến dịch (Tùy chọn)</label> <input type="text"
                                                                                                        id="campaignName"
                                                                                                        name="description"
                                                                                                        placeholder="Khuyến mãi hè 2026"
                                                                                                        value="${voucher.description}">
                                </div>
                                <div class="status-box">
                                    <div class="status-info"><strong>Trạng thái hoạt động</strong>
                                        <p>Cho phép sử dụng ngay sau khi lưu</p></div>
                                    <label class="switch"> <input type="checkbox" id="isActive" name="active"
                                                                <c:if test="${voucher == null || voucher.active}">
                                                                  checked</c:if>>
                                        <span class="slider round"></span> </label></div>
                            </div> <!-- Cột Phải -->
                            <div class="form-column">
                                <div class="input-grid">
                                    <div class="form-group"><label>Phần trăm giảm (%) <span
                                            class="required">*</span></label>
                                        <div class="input-with-suffix">

                                            <input type="number" id="discountPercent" name="discountPercent" min="1" max="100"
                                                                              value="${voucher.discountPercent}">

                                            <span class="suffix">%</span></div>
                                    </div>
                                    <div class="form-group"><label>Số lượng phát hành <span
                                            class="required">*</span></label>
                                        <div class="input-with-suffix">

                                            <input type="number" id="quantity" name="quantity" value="${voucher.quantity}">

                                            <span class="material-icons-outlined suffix">confirmation_number</span></div>
                                    </div>
                                    <div class="form-group"><label>Giá trị đơn tối thiểu <span class="required">*</span></label>
                                        <div class="input-with-suffix"><input type="number" id="minOrder" name="minOrderAmount" min="0"
                                                                              value="${voucher.minOrderAmount}" required>

                                            <span class="suffix">VND</span></div>
                                    </div>
                                    <div class="form-group"><label>Giảm tối đa</label>
                                        <div class="input-with-suffix"><input type="number" id="maxDiscount" name="maxDiscount"
                                                                              value="${voucher.maxDiscount}" required>

                                            <span class="suffix">VND</span></div>
                                    </div>
                                </div>
                                <div class="date-grid">
                                    <div class="form-group"><label>Ngày bắt đầu</label>
                                        <div class="input-with-suffix"><input type="date" name="startDate"
                                                                              value="${voucher.startDate.toLocalDate()}"></div>

                                    </div>
                                    <div class="form-group"><label>Ngày kết thúc</label>
                                        <div class="input-with-suffix"><input type="date" name="endDate"
                                                                              value="${voucher.endDate.toLocalDate()}">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-divider"></div>
                        <div class="form-actions">
                            <button type="button" class="btn btn-secondary" onclick="window.history.back()">Hủy bỏ
                            </button>
                            <button type="submit" class="btn btn-success"><span
                                    class="material-icons-outlined">save</span> Lưu Voucher
                            </button>
                        </div>
                    </form>
                </div>
                <div class="bottom-tips">
                    <div class="tip-card info-tip"><span class="material-icons-outlined icon">info</span>
                        <div class="tip-content"><strong>Mẹo nhanh</strong>
                            <p>Tỉ lệ giảm giá 10-15% thường mang lại tỷ lệ chuyển đổi cao nhất cho các sản phẩm bán lẻ.</p></div>
                    </div>
                    <div class="tip-card success-tip">
                        <div class="tip-content"><strong>Tạo chiến dịch thành công</strong>
                            <p>Kết hợp mã giảm giá với các dịp lễ lớn để tối ưu hóa doanh thu dự định của doanh
                                nghiệp.</p></div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
<script src="script.js"></script>
</body>
</html>