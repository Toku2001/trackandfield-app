document.getElementById("newpasswordForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const userName = document.getElementById("regUserName").value.trim();
    const newUserPassword = document.getElementById("password").value;
    const passwordConfirm = document.getElementById("rePassword").value;
	const urlParams = new URLSearchParams(window.location.search);
	const token = urlParams.get("token");

    if (newUserPassword !== passwordConfirm) {
        alert("パスワードが一致しません。");
        return;
    }

    fetch('http://localhost:8080/auth/reset-password', {
        method: 'POST',
		headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token, userName, newUserPassword })
    })
    .then(response => response.json())
    .then(data => {
        if (data.update) {
            alert("パスワードの再登録が完了しました。ログイン画面に戻ります。");

            // URLからパラメータを削除
            history.replaceState({}, '', location.pathname);

            // ログイン画面へ
            window.location.href = PATHS.LOGIN_PAGE;
        } else {
            alert(data.responseMessage || "パスワード再登録に失敗しました。");
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