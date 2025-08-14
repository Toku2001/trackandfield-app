document.getElementById("resetForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const userName = document.getElementById("resetUsername").value.trim();
    const userMail = document.getElementById("resetEmail").value.trim();

    fetch('http://localhost:8080/auth/request-password-reset', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userName, userMail }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.sendLink) {
            alert(data.responseMessage);
            window.location.href = PATHS.LOGIN_PAGE;
        } else {
            alert(data.responseMessage);
        }
    })
    .catch(error => console.error('Error:', error));
});

// ログイン画面に戻るボタン
document.getElementById("backToLogin").addEventListener("click", () => {
    window.location.href = PATHS.LOGIN_PAGE;
});