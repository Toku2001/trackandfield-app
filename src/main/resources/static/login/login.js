document.getElementById("loginForm").addEventListener("submit", function(event) {
  event.preventDefault();

  const userName = document.getElementById("userName").value.trim();
  const userPassword = document.getElementById("userPassword").value;

  if (userName === "" || userPassword === "") {
    alert("ユーザー名またはパスワードを入力してください。");
    return;
  }

  fetch('http://localhost:8080/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': "application/json" },
    body: JSON.stringify({ userName, userPassword }),
  })
  .then(response => response.json())
  .then(data => {
    if (data != null && data.userName === userName) {
      sessionStorage.setItem('userName', data.userName);
      sessionStorage.setItem('token', data.accessToken);
      window.location.href = PATHS.HOME_PAGE;
    } else {
      alert("ユーザー名またはパスワードが正しくありません。");
    }
  })
  .catch(error => {
    console.error('Error:', error);
    alert("ユーザー名またはパスワードが正しくありません。");
  });
});

// パスワード表示切替
function showOrHide() {
  const passwordField = document.getElementById("userPassword");
  const checkbox = document.getElementById("showpassword");
  passwordField.type = checkbox.checked ? "text" : "password";
}

// 新規登録画面へ遷移
document.getElementById("goUserCreateBtn").addEventListener("click", () => {
  window.location.href = PATHS.USERCREATE_PAGE;
});

// パスワード再設定画面へ遷移
document.getElementById("goPasswordResetBtn").addEventListener("click", () => {
  window.location.href = PATHS.PASSWORDRESET_PAGE;
});