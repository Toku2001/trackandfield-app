document.addEventListener("DOMContentLoaded", function () {
  formatted = new Date().toISOString().slice(0,10);
  document.getElementById("training-datetime").value = formatted;
  const fromPage = sessionStorage.getItem("fromPage"); // "training" or "tournament"

	document.getElementById("training-hour").value = sessionStorage.getItem("hour");
	document.getElementById("training-minute").value = sessionStorage.getItem("minute");
	document.getElementById("training-place").value = sessionStorage.getItem("place");
	document.getElementById("training-content").value = sessionStorage.getItem("content");
  
  // 時と分のセレクトボックスに値を設定（例: 0〜23時）
  const hourSelect = document.getElementById("training-hour");
  const minuteSelect = document.getElementById("training-minute");

  for (let h = 0; h <= 23; h++) {
    const option = document.createElement("option");
    option.value = String(h).padStart(2, '0');
    option.textContent = `${h}`;
    hourSelect.appendChild(option);
  }

  // 保存ボタンのイベント
  window.saveTraining = function () {

    const dateTime = document.getElementById("training-datetime").value;
    const hour = document.getElementById("training-hour").value;
    const minute = document.getElementById("training-minute").value;
    const place = document.getElementById("training-place").value.trim();
    const content = document.getElementById("training-content").value.trim();

    if (!formatted || !hour || !minute || !place || !content) {
      alert("全ての項目を入力してください。");
      return;
    }

    const token = sessionStorage.getItem("token");

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
      .then(res => res.json())
      .then(data => {
        if (data.result === 1) {
          alert("練習日誌を保存しました。");
          sessionStorage.removeItem("fromPage");
          if (fromPage === "training") {
            window.location.href = PATHS.TRAINING_PAGE;
          } else {
            window.location.href = PATHS.HOME_PAGE;
          }
        } else {
          alert("保存に失敗しました。");
        }
      })
      .catch(err => {
        console.error("送信エラー:", err);
        alert("送信中にエラーが発生しました。");
      });
  };
  
  //跳躍内容を記入するため、一時保存
  window.createJump = function () {
	
    const hour = document.getElementById("training-hour").value;
    const minute = document.getElementById("training-minute").value;
    const place = document.getElementById("training-place").value.trim();
    const content = document.getElementById("training-content").value.trim();
    
    if (!hour || !minute || !place || !content) {
      alert("全ての項目を入力してください。");
      return;
    }
      
    sessionStorage.setItem("formatted", formatted);
    sessionStorage.setItem("hour", hour);
    sessionStorage.setItem("minute", minute);
    sessionStorage.setItem("place", place);
    sessionStorage.setItem("content", content);

	  // ===== 遷移先に "from=training" をクエリとして付与 =====
		  window.location.href = PATHS.JUMP_PAGE + "?from=training";
	};

  // キャンセルボタンの処理
  window.cancel = function () {
      window.location.href = PATHS.HOME_PAGE;
  };
});