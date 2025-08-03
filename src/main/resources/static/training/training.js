document.addEventListener("DOMContentLoaded", function () {
  const formatted = new Date().toISOString().slice(0, 10);
  document.getElementById("training-datetime").value = formatted;

  const fromPage = sessionStorage.getItem("fromPage"); // "training" or "tournament"

  document.getElementById("training-hour").value = sessionStorage.getItem("hour");
  document.getElementById("training-minute").value = sessionStorage.getItem("minute");
  document.getElementById("training-place").value = sessionStorage.getItem("place");
  document.getElementById("training-content").value = sessionStorage.getItem("content");

  // 時・分セレクトボックス
  const hourSelect = document.getElementById("training-hour");
  const minuteSelect = document.getElementById("training-minute");

  for (let h = 0; h <= 23; h++) {
    const option = document.createElement("option");
    option.value = String(h).padStart(2, '0');
    option.textContent = `${h}`;
    hourSelect.appendChild(option);
  }
  for (let m = 0; m < 60; m += 5) {
    const option = document.createElement("option");
    option.value = String(m).padStart(2, '0');
    option.textContent = `${m}`;
    minuteSelect.appendChild(option);
  }

  // 保存ボタンの処理
  window.saveTraining = function () {
    const dateTime = document.getElementById("training-datetime").value;
    const hour = document.getElementById("training-hour").value;
    const minute = document.getElementById("training-minute").value;
    const place = document.getElementById("training-place").value.trim();
    const content = document.getElementById("training-content").value.trim();

    if (!dateTime || !hour || !minute || !place || !content) {
      alert("全ての項目を入力してください。");
      return;
    }

    const token = sessionStorage.getItem("token");
    if (!token) {
      alert("ログイン情報が見つかりません。再度ログインしてください。");
      return;
    }

    const postData = {
      trainingTime: dateTime,
      trainingDuration: `${hour}:${minute}`,
      trainingPlace: place,
      trainingComments: content
    };

    fetch("http://localhost:8080/api/register-training", {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(postData)
    })
      .then(async res => {
        const contentType = res.headers.get("Content-Type");
        let body = {};
        if (contentType && contentType.includes("application/json")) {
          body = await res.json();
        }

        if (res.ok) {
          alert("練習日誌を保存しました。");
          sessionStorage.removeItem("fromPage");
          if (fromPage === "training") {
            window.location.href = PATHS.TRAINING_PAGE;
          } else {
            window.location.href = PATHS.HOME_PAGE;
          }
        } else {
          const errorMsg = body.error || "保存に失敗しました。";
          alert(errorMsg);
        }
      })
      .catch(err => {
        console.error("送信エラー:", err);
        alert("送信中にエラーが発生しました。");
      });
  };

  // キャンセルボタンの処理
  window.cancel = function () {
    window.location.href = PATHS.HOME_PAGE;
  };
});