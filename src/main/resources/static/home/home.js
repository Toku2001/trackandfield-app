// ===== グローバル変数 =====
let currentYear = new Date().getFullYear();
let currentMonth = new Date().getMonth();

// ===== ページロード時に実行 =====
window.addEventListener("DOMContentLoaded", () => {
  const token = sessionStorage.getItem("token");
  const userName = sessionStorage.getItem("userName");
  if (!token) {
    alert("ログインしてください。");
    window.location.href = PATHS.LOGIN_PAGE;
    return;
  }

  // データ取得
  fetchUpcomingTournament();
  fetchPersonalBests(); // ← 自己ベスト取得を追加

  // ユーザー名表示
  document.getElementById("userName").textContent = userName;

  // カレンダー初期表示
  generateCalendar(currentYear, currentMonth);
});

// ===== 次の大会情報取得 =====
function fetchUpcomingTournament() {
  const token = sessionStorage.getItem("token");
  if (!token) return;

  fetch("http://localhost:8080/api/get-competition-upcoming", {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${token}`
    }
  })
    .then(res => {
      if (!res.ok) throw new Error(`HTTP Error: ${res.status}`);
      return res.json();
    })
    .then(data => {
      const name = data.competitionName;
      const place = data.competitionPlace;
      const timeStr = data.competitionTime;

      if (!name || !place || !timeStr) {
        showNoTournamentInfo();
        return;
      }

      const matchDate = new Date(timeStr);
      const today = new Date();

      matchDate.setHours(0, 0, 0, 0);
      today.setHours(0, 0, 0, 0);

      const daysLeft = Math.ceil((matchDate - today) / (1000 * 60 * 60 * 24));
      const dateStr = `${matchDate.getFullYear()}年${matchDate.getMonth() + 1}月${matchDate.getDate()}日`;

      document.getElementById("nextTournamentName").textContent = `大会名: ${name}`;
      document.getElementById("nextTournamentPlace").textContent = `場所: ${place}`;
      document.getElementById("nextTournamentDate").textContent = `日付: ${dateStr}`;
      document.getElementById("nextCountdown").textContent = `あと ${daysLeft} 日`;

      const countdownBanner = document.getElementById("countdown");
      if (countdownBanner) {
        countdownBanner.textContent = `「${name}」まであと ${daysLeft} 日`;
      }
    })
    .catch(err => {
      console.error("大会情報取得エラー:", err);
      showNoTournamentInfo();
    });

  function showNoTournamentInfo() {
    const info = document.getElementById("nextTournamentInfo");
    info.innerHTML = "<p>近日中の競技会は登録されていません。</p>";
  }
}

// ===== 自己ベスト取得 =====
function fetchPersonalBests() {
  const token = sessionStorage.getItem("token");
  if (!token) return;

  fetch("http://localhost:8080/api/get-personal-best", {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${token}`
    }
  })
    .then(res => {
      if (!res.ok) throw new Error(`HTTP Error: ${res.status}`);
      return res.json();
    })
    .then(data => {
      const recordList = document.getElementById("recordList");
      recordList.innerHTML = "";

      if (Array.isArray(data) && data.length > 0) {
        data.forEach(record => {
          const li = document.createElement("li");

          // 日本語表記に変換（必要に応じて追加可能）
          const eventNameMap = {
            "pole_vault": "棒高跳び",
            "long_jump": "走幅跳",
            "high_jump": "走高跳",
            "triple_jump": "三段跳"
            // 必要に応じて他の種目を追加
          };
          const localizedEvent = eventNameMap[record.event] || record.event;

          li.textContent = `${localizedEvent}: ${record.distance}m（${record.jumpDate}）`;
          recordList.appendChild(li);
        });
      } else {
        recordList.innerHTML = "<li>自己ベスト情報がありません。</li>";
      }
    })
    .catch(err => {
      console.error("自己ベスト取得エラー:", err);
      document.getElementById("recordList").innerHTML = "<li>取得中にエラーが発生しました。</li>";
    });
}

// ===== カレンダー生成処理 =====
function generateCalendar(year, month) {
  const calendarBody = document.getElementById("calendarBody");
  const calendarTitle = document.getElementById("calendarTitle");
  calendarBody.innerHTML = "";

  const firstDay = new Date(year, month, 1).getDay();
  const lastDate = new Date(year, month + 1, 0).getDate();
  const today = new Date();

  let row = document.createElement("tr");

  for (let i = 0; i < firstDay; i++) {
    row.appendChild(document.createElement("td"));
  }

  for (let date = 1; date <= lastDate; date++) {
    let cell = document.createElement("td");
    cell.textContent = date;

    cell.addEventListener("click", () => {
      const selectedDate = new Date(year, month, date);
      const yyyy = selectedDate.getFullYear();
      const mm = String(selectedDate.getMonth() + 1).padStart(2, '0');
      const dd = String(selectedDate.getDate()).padStart(2, '0');
      const formatted = `${yyyy}-${mm}-${dd}`;
      fetchDataForDate(formatted);
    });

    if (
      date === today.getDate() &&
      month === today.getMonth() &&
      year === today.getFullYear()
    ) {
      cell.classList.add("today");
    }

    row.appendChild(cell);

    if ((firstDay + date) % 7 === 0) {
      calendarBody.appendChild(row);
      row = document.createElement("tr");
    }
  }

  if (row.children.length > 0) {
    calendarBody.appendChild(row);
  }

  calendarTitle.textContent = `${year}年${month + 1}月`;
}

function changeMonth(offset) {
  currentMonth += offset;
  if (currentMonth < 0) {
    currentYear--;
    currentMonth = 11;
  } else if (currentMonth > 11) {
    currentYear++;
    currentMonth = 0;
  }
  generateCalendar(currentYear, currentMonth);
}

function fetchDataForDate(dateString) {
  goToDaily(dateString);
}

// ===== ページ遷移関数 =====
function goToTraining() {
  window.location.href = PATHS.TRAINING_PAGE;
}

function goToTournament() {
  window.location.href = PATHS.TOURNAMENT_PAGE;
}

function goToJumpRecord() {
  window.location.href = PATHS.JUMP_PAGE;
}

function goToDaily(dateString){
  window.location.href = PATHS.DAILY_PAGE + `?date=${dateString}`;
}

function logout() {
  sessionStorage.clear();
  window.location.href = PATHS.LOGIN_PAGE;
}