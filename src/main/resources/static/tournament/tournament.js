const fromPage = sessionStorage.getItem("fromPage"); // "training" or "tournament"

document.addEventListener("DOMContentLoaded", function () {
    const today = new Date();
    const thisYear = today.getFullYear();
    const thisMonth = today.getMonth() + 1;
    const thisDay = today.getDate();

    // 年の選択肢
    const yearSelect = document.getElementById("year");
    for (let i = thisYear - 10; i <= thisYear + 10; i++) {
        let option = document.createElement("option");
        option.value = i;
        option.textContent = i;
        if (i === thisYear) option.selected = true;
        yearSelect.appendChild(option);
    }

    // 月の選択肢
    const monthSelect = document.getElementById("month");
    for (let i = 1; i <= 12; i++) {
        let option = document.createElement("option");
        option.value = i;
        option.textContent = i;
        if (i === thisMonth) option.selected = true;
        monthSelect.appendChild(option);
    }

    // 日の選択肢
    const daySelect = document.getElementById("day");
    function updateDays() {
        let selectedYear = parseInt(yearSelect.value);
        let selectedMonth = parseInt(monthSelect.value);
        let daysInMonth = new Date(selectedYear, selectedMonth, 0).getDate();

        daySelect.innerHTML = "";
        for (let i = 1; i <= daysInMonth; i++) {
            let option = document.createElement("option");
            option.value = i;
            option.textContent = i;
            if (i === thisDay) option.selected = true;
            daySelect.appendChild(option);
        }
    }
    updateDays();

    yearSelect.addEventListener("change", updateDays);
    monthSelect.addEventListener("change", updateDays);
});

// **キャンセルボタン**
function cancel() {
    window.location.href = PATHS.HOME_PAGE;
}

//跳躍内容を記入するため、一時保存
window.createJump = function () {

	const year = document.getElementById("year").value;
	const month = String(document.getElementById("month").value).padStart(2, '0');
	const day = String(document.getElementById("day").value).padStart(2, '0');
	const tournamentDate = `${year}-${month}-${day}`;

	const tournamentName = document.getElementById("tournament-name").value.trim();
	const tournamentPlace = document.getElementById("tournament-place").value.trim();
	const tournamentComments = document.getElementById("tournament-comments").value.trim();
  
  if (!hour || !minute || !place || !content) {
    alert("全ての項目を入力してください。");
    return;
  }
  
  if (!tournamentName || !tournamentPlace) {
      alert("大会名と場所を入力してください。");
      return;
    }

    const token = sessionStorage.getItem("token");
    if (!token) {
      alert("ログイン情報が見つかりません。再度ログインしてください。");
      return;
    }
    
  sessionStorage.setItem("tournamentDate", tournamentDate);
  sessionStorage.setItem("tournamentName", tournamentName);
  sessionStorage.setItem("tournamentPlace", tournamentPlace);
  sessionStorage.setItem("tournamentComments", tournamentComments);

  // ===== 遷移先に "from=training" をクエリとして付与 =====
	  window.location.href = PATHS.JUMP_PAGE + "?from=training";
};
function saveTournament() {
  const year = document.getElementById("year").value;
  const month = String(document.getElementById("month").value).padStart(2, '0');
  const day = String(document.getElementById("day").value).padStart(2, '0');
  const tournamentDate = `${year}-${month}-${day}`;

  const tournamentName = document.getElementById("tournament-name").value.trim();
  const tournamentPlace = document.getElementById("tournament-place").value.trim();
  const tournamentComments = document.getElementById("tournament-comments").value.trim();

  if (!tournamentName || !tournamentPlace) {
    alert("大会名と場所を入力してください。");
    return;
  }

  const token = sessionStorage.getItem("token");
  if (!token) {
    alert("ログイン情報が見つかりません。再度ログインしてください。");
    return;
  }

  const postData = {
    competitionTime: tournamentDate,
    competitionPlace: tournamentPlace,
    competitionName: tournamentName,
    competitionComments: tournamentComments,
  };

  fetch("http://localhost:8080/api/register-competition", {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify(postData)
  })
    .then(res => res.json())
    .then(data => {
      if (data === 1 || data.success) {
        alert("大会情報を保存しました。");
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
}
