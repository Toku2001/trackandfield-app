document.addEventListener("DOMContentLoaded", function () {
  const container = document.getElementById("record-container");
  const addBtn = document.getElementById("addRecordBtn");

  addBtn.addEventListener("click", () => {
    addJumpRecordBlock();
  });

  // 最初の1件を追加
  addJumpRecordBlock();
});

function addJumpRecordBlock() {
  const container = document.getElementById("record-container");

  const index = document.querySelectorAll(".record-entry").length;
  const entryDiv = document.createElement("div");
  entryDiv.className = "record-entry";
  entryDiv.id = `record-entry-${index}`; // 削除のためにIDを設定

  entryDiv.innerHTML = `
    <hr>
    <div>
      <label for="event-${index}">種目</label>
      <select id="event-${index}" onchange="togglePoleFields(${index})">
        <option value="long_jump">走幅跳</option>
        <option value="triple_jump">三段跳</option>
        <option value="pole_vault">棒高跳</option>
      </select>

      <label for="distance-${index}">記録</label>
      <select id="distance-${index}">
        <option value="">選択してください</option>
      </select>

      <label for="approach-${index}">助走距離（歩）</label>
      <select id="approach-${index}">
        <option value="">選択してください</option>
      </select>

      <div id="pole-feat-div-${index}" style="display: none;">
        <label for="pole-feat-${index}">ポール(長さ)</label>
        <select id="pole-feat-${index}">
          <option value="">選択してください</option>
        </select>
      </div>

      <div id="pole-pond-div-${index}" style="display: none;">
        <label for="pole-pond-${index}">ポール(硬さ)</label>
        <select id="pole-pond-${index}">
          <option value="">選択してください</option>
        </select>
      </div>

      <button type="button" onclick="removeJumpRecordBlock(${index})">削除</button>
    </div>
  `;

  container.appendChild(entryDiv);

  // 初期化
  initializeSelects(index);
}

function removeJumpRecordBlock(index) {
  const entryDiv = document.getElementById(`record-entry-${index}`);
  if (entryDiv) {
    entryDiv.remove();
  }
}

// キャンセルボタンの処理
function cancel() {
  window.location.href = PATHS.HOME_PAGE;
}

function initializeSelects(index) {
  const distanceSelect = document.getElementById(`distance-${index}`);
  const approachSelect = document.getElementById(`approach-${index}`);
  const poleFeatSelect = document.getElementById(`pole-feat-${index}`);
  const polePondSelect = document.getElementById(`pole-pond-${index}`);

  for (let i = 3.5; i <= 9.0; i += 0.1) {
    const opt = document.createElement("option");
    opt.value = i.toFixed(2);
    opt.textContent = `${i.toFixed(2)} m`;
    distanceSelect.appendChild(opt);
  }

  for (let i = 8; i <= 24; i++) {
    const opt = document.createElement("option");
    opt.value = i;
    opt.textContent = `${i} 歩`;
    approachSelect.appendChild(opt);
  }

  for (let i = 3.8; i <= 5.2; i += 0.1) {
    const opt = document.createElement("option");
    opt.value = i.toFixed(2);
    opt.textContent = `${i.toFixed(2)} m`;
    poleFeatSelect.appendChild(opt);
  }

  for (let i = 100; i <= 200; i += 10) {
    const opt = document.createElement("option");
    opt.value = i;
    opt.textContent = `${i}`;
    polePondSelect.appendChild(opt);
  }
}

function togglePoleFields(index) {
  const eventVal = document.getElementById(`event-${index}`).value;
  const featDiv = document.getElementById(`pole-feat-div-${index}`);
  const pondDiv = document.getElementById(`pole-pond-div-${index}`);
  const show = eventVal === "pole_vault";
  featDiv.style.display = show ? "block" : "none";
  pondDiv.style.display = show ? "block" : "none";
}

function submitAllJumpRecords() {
  const records = [];
  const token = sessionStorage.getItem("token");

  const jumpDate = document.getElementById("jumpDate").value;
  const recordType = document.getElementById("recordType").value;

  if (!jumpDate || !recordType) {
    alert("日付と記録種別を選択してください。");
    return;
  }

  const entries = document.querySelectorAll(".record-entry");
  entries.forEach((entry, i) => {
    const record = {
      event: document.getElementById(`event-${i}`).value,
      distance: parseFloat(document.getElementById(`distance-${i}`).value),
      approach: parseInt(document.getElementById(`approach-${i}`).value),
      poleFeat: document.getElementById(`pole-feat-${i}`).value || null,
      polePond: document.getElementById(`pole-pond-${i}`).value || null,
      recordType: recordType,
      jumpDate: jumpDate
    };
    records.push(record);
  });

  fetch("http://localhost:8080/api/register-jump-records", {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify(records)
  })
    .then(res => res.json())
    .then(data => {
      if (data.result === 1) {
        alert("跳躍記録を保存しました。");
        window.location.href = PATHS.HOME_PAGE;
      } else {
        alert("保存に失敗しました。");
      }
    })
    .catch(err => {
      console.error("送信エラー:", err);
      alert("送信中にエラーが発生しました。");
    });
}