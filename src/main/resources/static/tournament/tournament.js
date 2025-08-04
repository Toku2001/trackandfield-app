const fromPage = sessionStorage.getItem("fromPage");

document.addEventListener("DOMContentLoaded", function () {
  const today = new Date();
  const thisYear = today.getFullYear();
  const thisMonth = today.getMonth() + 1;
  const thisDay = today.getDate();

  // 年セレクト
  const yearSelect = document.getElementById("year");
  for (let y = thisYear - 5; y <= thisYear + 5; y++) {
    const option = document.createElement("option");
    option.value = y;
    option.textContent = y;
    if (y === thisYear) option.selected = true;
    yearSelect.appendChild(option);
  }

  // 月セレクト
  const monthSelect = document.getElementById("month");
  for (let m = 1; m <= 12; m++) {
    const option = document.createElement("option");
    option.value = m;
    option.textContent = m;
    if (m === thisMonth) option.selected = true;
    monthSelect.appendChild(option);
  }

  // 日セレクト
  const daySelect = document.getElementById("day");

  function updateDayOptions() {
    const selectedYear = parseInt(yearSelect.value);
    const selectedMonth = parseInt(monthSelect.value);
    const lastDay = new Date(selectedYear, selectedMonth, 0).getDate();

    daySelect.innerHTML = "";
    for (let d = 1; d <= lastDay; d++) {
      const option = document.createElement("option");
      option.value = d;
      option.textContent = d;
      if (d === thisDay) option.selected = true;
      daySelect.appendChild(option);
    }
  }

  yearSelect.addEventListener("change", updateDayOptions);
  monthSelect.addEventListener("change", updateDayOptions);
  updateDayOptions(); // 初期実行
});

// キャンセル処理
function cancel() {
  window.location.href = PATHS.HOME_PAGE;
}

// 保存処理
function saveTournament() {
  const year = document.getElementById("year").value;
  const month = String(document.getElementById("month").value).padStart(2, "0");
  const day = String(document.getElementById("day").value).padStart(2, "0");
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
    .then(async res => {
      const contentType = res.headers.get("Content-Type");
      let responseBody = {};
      if (contentType && contentType.includes("application/json")) {
        responseBody = await res.json();
      }

      if (res.ok) {
        alert("大会情報を保存しました。");
        sessionStorage.removeItem("fromPage");
        if (fromPage === "training") {
          window.location.href = PATHS.TRAINING_PAGE;
        } else {
          window.location.href = PATHS.HOME_PAGE;
        }
      } else {
        const errorMsg = responseBody.error || "保存に失敗しました。";
        alert(errorMsg);
      }
    })
    .catch(err => {
      console.error("送信エラー:", err);
      alert("送信中にエラーが発生しました。");
    });
}