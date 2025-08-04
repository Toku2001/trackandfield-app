document.getElementById("resetForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const username = document.getElementById("resetUsername").value.trim();
    const email = document.getElementById("resetEmail").value.trim();

    fetch('http://localhost:8080/auth/request-password-reset', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userName, userMail }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("パスワード再設定用のメールを送信しました。");
            window.location.href = PATHS.LOGIN_PAGE;
        } else {
            alert(data.message);
        }
    })
    .catch(error => console.error('Error:', error));
});

// ログイン画面に戻るボタン
document.getElementById("backToLogin").addEventListener("click", () => {
    window.location.href = PATHS.LOGIN_PAGE;
});