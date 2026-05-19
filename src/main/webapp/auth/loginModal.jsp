<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
  .slm-dropdown {
    width: 100%;
    max-width: unset;
    padding: 1.8rem 2rem 1.2rem;
    box-sizing: border-box;
    background: #fff;
  }

  .slm-dropdown h2 {
    font-size: 22px;
    font-weight: 700;
    color: #243b55;
    text-align: center;
    margin-bottom: 1rem;
    letter-spacing: 0;
    text-transform: none;
  }


  .slm-dropdown .slm-error-box {
    background-color: #fff0f0;
    color: #d8000c;
    border: 1px solid #ffbaba;
    padding: 8px;
    margin-bottom: 10px;
    border-radius: 6px;
    font-size: 12px;
    text-align: center;
    width: 100%;
    box-sizing: border-box;
    display: none;
  }


  .slm-dropdown .slm-field {
    width: 100%;
    position: relative;
    margin-bottom: 12px;
  }

  .slm-dropdown .slm-field input {
    width: 100%;
    padding: 18px 14px 6px 14px;
    border: 2px solid #c8d4e0;
    border-radius: 6px;
    font-size: 14px;
    color: #243b55;
    outline: none;
    background: #f4f7fa;
    box-sizing: border-box;
    transition: border-color 0.2s, background 0.2s;
  }

  .slm-dropdown .slm-field input:focus {
    border-color: #243b55;
    background: #fff;
  }

  .slm-dropdown .slm-field label {
    position: absolute;
    left: 14px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 14px;
    color: #7a8fa6;
    pointer-events: none;
    transition: all 0.2s;
  }

  .slm-dropdown .slm-field input:focus + label,
  .slm-dropdown .slm-field input:not(:placeholder-shown) + label {
    top: 8px;
    transform: none;
    font-size: 10px;
    color: #243b55;
    font-weight: 600;
  }
  .slm-dropdown .slm-field .toggle-password {
    position: absolute;
    right: 14px;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
    color: #7a8fa6;
    font-size: 15px;
    line-height: 1;
  }

  .slm-dropdown .slm-field .slm-req { color: red; margin-left: 2px; }

  .slm-dropdown .slm-field .slm-eye {
    position: absolute;
    right: 13px;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
    color: #7a8fa6;
    font-size: 15px;
  }


  .slm-dropdown .slm-remember input[type="checkbox"] {
    accent-color: #243b55;
    width: 14px;
    height: 14px;
  }

  .slm-dropdown .slm-btn {
    width: 100%;
    padding: 13px;
    background: linear-gradient(to right, #141e30, #243b55);
    border: none;
    border-radius: 30px;
    font-size: 13px;
    font-weight: 700;
    letter-spacing: 1px;
    text-transform: uppercase;
    color: #fff;
    cursor: pointer;
    transition: opacity 0.2s;
    margin-top: 4px;
    margin-bottom: 1rem;
  }

  .slm-dropdown .slm-btn:hover { opacity: 0.85; }


  .slm-dropdown .slm-footer {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;
    padding: 0;
    margin-bottom: 0.8rem;
    width: 100%;
  }

  .slm-dropdown .slm-footer a {
    color: #243b55;
    font-weight: 600;
    text-decoration: none;
  }

  .slm-dropdown .slm-footer a:hover { text-decoration: underline; }

  .slm-dropdown .slm-home {
    display: block;
    text-align: center;
    font-size: 12px;
    color: #aaa;
    text-decoration: none;
    margin-top: 6px;
  }

</style>

<div class="slm-dropdown">
  <h2>Đăng nhập</h2>
  <div id="auth-error-message" class="slm-error-box"></div>

  <form action="#" id="modalSignInForm">

    <div class="slm-field">
      <input type="email"
             id="modal-email"
             name="email"
             placeholder=" "
             required />

      <label for="modal-email">
        Email <span class="slm-req">*</span>
      </label>
    </div>

    <div class="slm-field">
      <input type="password"
             id="modal-password"
             placeholder=" "
             required />

      <label for="modal-password">
        Mật khẩu <span class="slm-req">*</span>
      </label>

      <i class="fa-solid fa-eye toggle-password"
         data-toggle="#modal-password"></i>
    </div>

    <button type="submit"
            id="modalSignInButton"
            class="slm-btn">
      Đăng nhập
    </button>

  </form>

  <div class="slm-footer">
    <a href="${pageContext.request.contextPath}/login">Tạo tài khoản</a>
    <a href="${pageContext.request.contextPath}/auth/forgot-password">Quên mật khẩu</a>
  </div>
</div>

<script src="${pageContext.request.contextPath}/static/style-page/auth/auth.js"></script>

<% if (request.getAttribute("errorMessage") != null) { %>
<script>
  var errBox = document.getElementById('auth-error-message');
  if (errBox) {
    errBox.textContent = '<%= request.getAttribute("errorMessage") %>';
    errBox.style.display = 'block';
  }
</script>
<% } %>
