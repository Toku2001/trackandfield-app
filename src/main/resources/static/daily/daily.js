document.addEventListener("DOMContentLoaded", async function () {
  const urlParams = new URLSearchParams(window.location.search);
  const date = urlParams.get("date");
  sessionStorage.setItem("selectDate", date);
  const token = sessionStorage.getItem("token");

  try {
    // === 練習データ ===
    const resTraining = await fetch(`http://localhost:8080/api/get-training?trainingDate=${date}`, {
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (resTraining.ok) {
      const dataTraining = await resTraining.json();
	  sessionStorage.setItem("trainingId", dataTraining.trainingId);
      document.getElementById("training-time").textContent = dataTraining.trainingTime;
      document.getElementById("training-place").textContent = dataTraining.trainingPlace;
      document.getElementById("training-comments").textContent = dataTraining.trainingComments;
    } else {
      document.getElementById("training-time").textContent = "登録されていません";
      document.getElementById("training-place").textContent = "登録されていません";
      document.getElementById("training-comments").textContent = "登録されていません";
    }

    // === 競技会データ ===
    const resCompetitions = await fetch(`http://localhost:8080/api/get-competition?competitionDate=${date}`, {
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (resCompetitions.ok) {
      const dataCompetitions = await resCompetitions.json();
	  sessionStorage.setItem("tournamentId", dataCompetitions.competitionId);
      document.getElementById("tournament-time").textContent = dataCompetitions.competitionTime;
      document.getElementById("tournament-place").textContent = dataCompetitions.competitionPlace;
      document.getElementById("tournament-name").textContent = dataCompetitions.competitionName;
      document.getElementById("tournament-comments").textContent = dataCompetitions.competitionComments;
    } else {
      document.getElementById("tournament-time").textContent = "登録されていません";
      document.getElementById("tournament-place").textContent = "登録されていません";
      document.getElementById("tournament-name").textContent = "登録されていません";
      document.getElementById("tournament-comments").textContent = "登録されていません";
    }

    // === 跳躍記録 ===
    const resJumps = await fetch(`http://localhost:8080/api/get-jump-records?jumpDate=${date}`, {
      headers: { "Authorization": `Bearer ${token}` }
    });

    const jumpContainer = document.getElementById("jump-records");
    jumpContainer.innerHTML = "";

    if (resJumps.ok) {
      const dataJumps = await resJumps.json();

      if (dataJumps.length === 0) {
        jumpContainer.textContent = "跳躍記録は登録されていません。";
      } else {
        const table = document.createElement("table");
        table.border = "1";

        table.innerHTML = `
          <tr>
            <th>種目</th>
            <th>記録 (m)</th>
            <th>助走 (歩)</th>
            <th>ポール長さ</th>
            <th>ポール硬さ</th>
            <th>日付</th>
            <th>記録種別</th>
          </tr>
        `;

        dataJumps.forEach(record => {
          const row = document.createElement("tr");
          row.innerHTML = `
            <td>${record.event}</td>
            <td>${record.distance}</td>
            <td>${record.approach}</td>
            <td>${record.poleFeat || "―"}</td>
            <td>${record.polePond || "―"}</td>
            <td>${record.jumpDate}</td>
            <td>${record.recordType}</td>
          `;
          table.appendChild(row);
        });

        jumpContainer.appendChild(table);
      }

      // セッションストレージに保存
      sessionStorage.setItem("jumpRecords", JSON.stringify(dataJumps));
    } else if (resJumps.status === 403) {
      jumpContainer.textContent = "認証エラー：ログインし直してください。";
    } else {
      jumpContainer.textContent = "跳躍記録の取得に失敗しました。";
    }

  } catch (err) {
    console.error("データ取得エラー:", err);
    document.getElementById("jump-records").textContent = "データの読み込み中にエラーが発生しました。";
  }
});

// 戻る／編集ページへ
function goToHome() {
  window.location.href = PATHS.HOME_PAGE;
}

function goToEdit() {
  // 練習
  sessionStorage.setItem("trainingTime", document.getElementById("training-time").textContent);
  sessionStorage.setItem("trainingPlace", document.getElementById("training-place").textContent);
  sessionStorage.setItem("trainingComments", document.getElementById("training-comments").textContent);

  // 競技会
  sessionStorage.setItem("tournamentTime", document.getElementById("tournament-time").textContent);
  sessionStorage.setItem("tournamentPlace", document.getElementById("tournament-place").textContent);
  sessionStorage.setItem("tournamentComments", document.getElementById("tournament-comments").textContent);
  sessionStorage.setItem("tournamentName", document.getElementById("tournament-name").textContent);

  const date = sessionStorage.getItem("selectDate");
  window.location.href = PATHS.DAILYEDIT_PAGE + `?date=${date}`;
}