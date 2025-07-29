document.getElementById("registerForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const userName = document.getElementById("regUserName").value.trim();
    const userMail = document.getElementById("regEmail").value.trim();
    const userPassword = document.getElementById("password").value;
    const passwordConfirm = document.getElementById("rePassword").value;

    if (userPassword !== passwordConfirm) {
        alert("パスワードが一致しません。");
        return;
    }

    fetch('http://localhost:8080/auth/register-user', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userName, userPassword, userMail}),
    })
    .then(response => response.json())
	.then(data => {
	    if (data.registerNumber && data.registerNumber > 0) {
	        alert("登録が完了しました。ログイン画面に戻ります。");
	        window.location.href = PATHS.LOGIN_PAGE;
	    } else {
	        alert("登録に失敗しました。");
	    }
	})
    .catch(error => console.error('Error:', error));
});

// ログイン画面に戻るボタン
document.getElementById("backToLogin").addEventListener("click", () => {
    window.location.href = PATHS.LOGIN_PAGE;
});

// パスワード表示切替
function showOrHide() {
    const passwordField = document.getElementById("rePassword");
    const checkbox = document.getElementById("showpassword");
    passwordField.type = checkbox.checked ? "text" : "password";
}
