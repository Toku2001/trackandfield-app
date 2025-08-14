document.addEventListener("DOMContentLoaded", function () {
  const fields = {
    tournament: ["tournament-name", "tournament-place", "tournament-time", "tournament-comments"],
    training: ["training-time", "training-place", "training-comments"]
  };

  for (const section in fields) {
    fields[section].forEach(id => {
      const value = sessionStorage.getItem(toCamel(id));
      const input = document.getElementById(id);

      if (!input) return;

      if (value === "登録されていません") {
        input.value = "";
        input.disabled = true;
        input.placeholder = "登録されていません";
      } else if (value !== null) {
        input.value = value;
      }
    });
  }

  renderJumpRecords();
  
  const trainingDeleteBtn = document.createElement("button");
  trainingDeleteBtn.id = "deleteTraining";
  trainingDeleteBtn.type = "button";
  trainingDeleteBtn.textContent = "練習日誌を削除";
  trainingDeleteBtn.style.marginTop = "1em";

  const tournamentDeleteBtn = document.createElement("button");
  tournamentDeleteBtn.id = "deleteTournament";
  tournamentDeleteBtn.type = "button";
  tournamentDeleteBtn.textContent = "競技会情報を削除";
  tournamentDeleteBtn.style.marginTop = "1em";

  // DOM上の適切な場所に追加（必要に応じて調整）
  document.getElementById("training-comments")?.parentNode?.appendChild(trainingDeleteBtn);
  document.getElementById("tournament-comments")?.parentNode?.appendChild(tournamentDeleteBtn);

  const registerButton = document.createElement("button");
  registerButton.textContent = "跳躍記録を登録";
  registerButton.type = "button";
  registerButton.style.marginBottom = "1em";
  registerButton.addEventListener("click", function () {
    window.location.href = PATHS.JUMP_PAGE;
  });
  document.getElementById("jump-records-container").before(registerButton);

  document.getElementById("editForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const token = sessionStorage.getItem("token");
    if (!token) {
      alert("ログイン情報が失われました。再ログインしてください。");
      return;
    }

    const inputValues = getInputValues(fields);
    const jumpRecords = collectJumpRecords();
    if (jumpRecords === null) return;

    const changes = {};
    for (const section in fields) {
      if (hasChanges(fields[section], inputValues, sessionStorage)) {
        changes[section] = true;
      }
    }

    const oldJumpRecords = JSON.parse(sessionStorage.getItem("jumpRecords") || "[]");
    if (!areJumpRecordsEqual(oldJumpRecords, jumpRecords)) {
      changes.jumpRecords = true;
    }

    if (Object.keys(changes).length === 0) {
      alert("すでに最新の状態です。");
      return;
    }

    try {
      if (changes.tournament) {
        await saveData('/api/change-competition', {
          competitionId: sessionStorage.getItem("tournamentId"),
          competitionName: inputValues["tournament-name"],
          competitionPlace: inputValues["tournament-place"],
          competitionTime: inputValues["tournament-time"],
          competitionComments: inputValues["tournament-comments"]
        }, token);
      }

      if (changes.training) {
        await saveData('/api/change-training', {
          trainingId: sessionStorage.getItem("trainingId"),
          trainingTime: inputValues["training-time"],
          trainingPlace: inputValues["training-place"],
          trainingComments: inputValues["training-comments"]
        }, token);
      }

      if (changes.jumpRecords) {
        await saveData('/api/change-jump-records', jumpRecords, token);
        sessionStorage.setItem("jumpRecords", JSON.stringify(jumpRecords));
      }

      alert("保存が完了しました。");
      const selectDate = sessionStorage.getItem("selectDate");
      window.location.href = PATHS.DAILY_PAGE + `?date=${selectDate}`;
    } catch (err) {
      console.error("保存エラー:", err);
      alert("保存中にエラーが発生しました。");
    }
  });
  
  document.getElementById("deleteTraining").addEventListener("click", async function () {
    const token = sessionStorage.getItem("token");
    const date = sessionStorage.getItem("selectDate");
    if (!confirm(`${date} の練習日誌を削除しますか？`)) return;

    try {
      const res = await fetch(`/api/delete-training?trainingDate=${date}`, {
        method: "DELETE",
        headers: { "Authorization": `Bearer ${token}` }
      });
      if (!res.ok) throw new Error(`削除失敗: ${res.status}`);
      alert("練習日誌を削除しました。");
      // 無効化して保存ボタン無効に
      ["training-time", "training-place", "training-comments"].forEach(id => {
        const input = document.getElementById(id);
        if (input) {
          input.value = "";
          input.disabled = true;
          input.placeholder = "登録されていません";
          sessionStorage.setItem(toCamel(id), "登録されていません");
        }
      });
      checkSaveButtonEnabled();
    } catch (err) {
      console.error("削除エラー:", err);
      alert("削除中にエラーが発生しました。");
    }
  });

  document.getElementById("deleteTournament").addEventListener("click", async function () {
    const token = sessionStorage.getItem("token");
    const date = sessionStorage.getItem("selectDate");
    if (!confirm(`${date} の競技会情報を削除しますか？`)) return;

    try {
      const res = await fetch(`/api/delete-competition?competitionDate=${date}`, {
        method: "DELETE",
        headers: { "Authorization": `Bearer ${token}` }
      });
      if (!res.ok) throw new Error(`削除失敗: ${res.status}`);
      alert("競技会情報を削除しました。");
      ["tournament-name", "tournament-place", "tournament-time", "tournament-comments"].forEach(id => {
        const input = document.getElementById(id);
        if (input) {
          input.value = "";
          input.disabled = true;
          input.placeholder = "登録されていません";
          sessionStorage.setItem(toCamel(id), "登録されていません");
        }
      });
      checkSaveButtonEnabled();
    } catch (err) {
      console.error("削除エラー:", err);
      alert("削除中にエラーが発生しました。");
    }
  });

  document.getElementById("deleteAllJumps").addEventListener("click", async function () {
    const token = sessionStorage.getItem("token");
    const date = sessionStorage.getItem("selectDate");
    if (!confirm(`${date} の跳躍記録を全て削除しますか？`)) return;

    try {
      const res = await fetch(`/api/delete-jump-records-by-date?jumpDate=${date}`, {
        method: "DELETE",
        headers: { "Authorization": `Bearer ${token}` }
      });

      if (!res.ok) throw new Error(`削除失敗: ${res.status}`);
      alert("すべての跳躍記録を削除しました。");
      sessionStorage.setItem("jumpRecords", JSON.stringify([]));
      location.reload();
    } catch (err) {
      console.error("全削除エラー:", err);
      alert("削除中にエラーが発生しました。");
    }
  });

  function renderJumpRecords() {
    const jumpRecords = JSON.parse(sessionStorage.getItem("jumpRecords") || "[]");
    const jumpContainer = document.getElementById("jump-records-container");
    const deleteAllBtn = document.getElementById("deleteAllJumps");

    jumpContainer.innerHTML = "";

    if (jumpRecords.length === 0) {
      jumpContainer.textContent = "跳躍記録はありません。";
      deleteAllBtn.disabled = true;
      return;
    }

    deleteAllBtn.disabled = false;

    jumpRecords.forEach((record, index) => {
      const table = document.createElement("table");
      table.border = "1";
      table.innerHTML = `
        <tr><th colspan="2">跳躍記録 ${index + 1}</th></tr>
        <tr><th>種目</th><td>
          <select id="jumpEvent${index}" onchange="togglePoleFields(${index})">
            <option value="long_jump" ${record.event === "long_jump" ? "selected" : ""}>走幅跳</option>
            <option value="triple_jump" ${record.event === "triple_jump" ? "selected" : ""}>三段跳</option>
            <option value="pole_vault" ${record.event === "pole_vault" ? "selected" : ""}>棒高跳</option>
          </select>
        </td></tr>
        <tr><th>記録 (m)</th><td><select id="jumpDistance${index}"></select></td></tr>
        <tr><th>助走 (歩)</th><td><select id="jumpApproach${index}"></select></td></tr>
        <tr id="poleFeatRow${index}" style="display:${record.event === 'pole_vault' ? '' : 'none'}">
          <th>ポール長さ</th><td><select id="jumpPoleFeat${index}"></select></td>
        </tr>
        <tr id="polePondRow${index}" style="display:${record.event === 'pole_vault' ? '' : 'none'}">
          <th>ポール硬さ</th><td><select id="jumpPolePond${index}"></select></td>
        </tr>
        <tr><th>日付</th><td><input type="date" id="jumpDate${index}" value="${record.jumpDate || ''}" required></td></tr>
        <tr><th>記録種別</th><td>
          <select id="jumpRecordType${index}">
            <option value="training" ${record.recordType === "training" ? "selected" : ""}>練習</option>
            <option value="official" ${record.recordType === "official" ? "selected" : ""}>競技会</option>
          </select>
        </td></tr>
        <tr><td colspan="2" style="text-align: right;"><button type="button" onclick="deleteJumpRecord(${index})">削除</button></td></tr>
        <input type="hidden" id="jumpSequence${index}" value="${record.jumpEventId || ''}">
      `;
      jumpContainer.appendChild(table);
      jumpContainer.appendChild(document.createElement("br"));
      initializeSelects(index, record);
    });
  }

  function initializeSelects(index, record) {
    const distanceSelect = document.getElementById(`jumpDistance${index}`);
    const approachSelect = document.getElementById(`jumpApproach${index}`);
    const poleFeatSelect = document.getElementById(`jumpPoleFeat${index}`);
    const polePondSelect = document.getElementById(`jumpPolePond${index}`);

    for (let i = 3.5; i <= 9.0; i += 0.1) {
      const val = i.toFixed(2);
      const opt = document.createElement("option");
      opt.value = val;
      opt.textContent = `${val} m`;
      if (record.distance && parseFloat(val) === parseFloat(record.distance)) opt.selected = true;
      distanceSelect.appendChild(opt);
    }

    for (let i = 8; i <= 24; i++) {
      const opt = document.createElement("option");
      opt.value = i;
      opt.textContent = `${i} 歩`;
      if (record.approach && parseInt(i) === parseInt(record.approach)) opt.selected = true;
      approachSelect.appendChild(opt);
    }

    for (let i = 3.8; i <= 5.2; i += 0.1) {
      const val = i.toFixed(2);
      const opt = document.createElement("option");
      opt.value = val;
      opt.textContent = `${val} m`;
      if (record.poleFeat && val === record.poleFeat) opt.selected = true;
      poleFeatSelect.appendChild(opt);
    }

    for (let i = 100; i <= 200; i += 10) {
      const opt = document.createElement("option");
      opt.value = i;
      opt.textContent = i;
      if (record.polePond && parseInt(i) === parseInt(record.polePond)) opt.selected = true;
      polePondSelect.appendChild(opt);
    }
  }

  function collectJumpRecords() {
    const tables = document.querySelectorAll("#jump-records-container table");
    const result = [];

    for (let i = 0; i < tables.length; i++) {
      const event = document.getElementById(`jumpEvent${i}`).value;
      const distance = document.getElementById(`jumpDistance${i}`).value;
      const approach = document.getElementById(`jumpApproach${i}`).value;
      const jumpDate = document.getElementById(`jumpDate${i}`).value;
      const recordType = document.getElementById(`jumpRecordType${i}`).value;

      if (!event || !distance || !approach || !jumpDate || !recordType) {
        alert(`跳躍記録 ${i + 1} の項目が未入力です。`);
        return null;
      }

      const poleFeat = event === "pole_vault" ? document.getElementById(`jumpPoleFeat${i}`).value : null;
      const polePond = event === "pole_vault" ? document.getElementById(`jumpPolePond${i}`).value : null;

      result.push({
        jumpEventId: document.getElementById(`jumpSequence${i}`).value || null,
        event,
        distance: parseFloat(distance),
        approach: parseInt(approach),
        poleFeat,
        polePond,
        jumpDate,
        recordType
      });
    }
    return result;
  }

  window.deleteJumpRecord = function (index) {
    const jumpRecords = JSON.parse(sessionStorage.getItem("jumpRecords") || "[]");
    const record = jumpRecords[index];
    const token = sessionStorage.getItem("token");

    if (!confirm("この跳躍記録を削除しますか？")) return;

    if (!record.jumpEventId) {
      jumpRecords.splice(index, 1);
      sessionStorage.setItem("jumpRecords", JSON.stringify(jumpRecords));
      renderJumpRecords();
      return;
    }

    try {
      fetch(`/api/delete-jump-record?jumpEventId=${record.jumpEventId}`, {
        method: "DELETE",
        headers: { "Authorization": `Bearer ${token}` }
      }).then(res => {
        if (!res.ok) throw new Error(`削除失敗: ${res.status}`);
        alert("跳躍記録を削除しました。");
        jumpRecords.splice(index, 1);
        sessionStorage.setItem("jumpRecords", JSON.stringify(jumpRecords));
        renderJumpRecords();
      });
    } catch (err) {
      console.error("削除エラー:", err);
      alert("削除中にエラーが発生しました。");
    }
  };

  window.togglePoleFields = function (index) {
    const event = document.getElementById(`jumpEvent${index}`).value;
    document.getElementById(`poleFeatRow${index}`).style.display = event === "pole_vault" ? "" : "none";
    document.getElementById(`polePondRow${index}`).style.display = event === "pole_vault" ? "" : "none";
  };

  checkSaveButtonEnabled();
});

function getInputValues(fields) {
  const result = {};
  for (const section in fields) {
    fields[section].forEach(id => {
      const input = document.getElementById(id);
      if (input && !input.disabled) {
        result[id] = input.value.trim();
      }
    });
  }
  return result;
}

function hasChanges(keys, inputs, storage) {
  return keys.some(id => {
    const el = document.getElementById(id);
    if (!el || el.disabled) return false;
    return inputs[id] !== (storage.getItem(toCamel(id)) || "");
  });
}

function checkSaveButtonEnabled() {
  const saveButton = document.getElementById("saveButton");

  const fields = [
    "tournament-name",
    "tournament-place",
    "tournament-time",
    "tournament-comments",
    "training-time",
    "training-place",
    "training-comments"
  ];

  const hasEnabledInput = fields.some(id => {
    const el = document.getElementById(id);
    return el && !el.disabled;
  });

  const jumpRecords = JSON.parse(sessionStorage.getItem("jumpRecords") || "[]");
  const hasJumpRecord = jumpRecords.length > 0;

  saveButton.disabled = !(hasEnabledInput || hasJumpRecord);
}

async function saveData(url, data, token) {
  const res = await fetch(url, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify(data)
  });
  if (!res.ok) throw new Error(`HTTP error: ${res.status}`);
}

function toCamel(str) {
  return str.replace(/-([a-z])/g, (_, c) => c.toUpperCase());
}

function goToDaily() {
  const date = sessionStorage.getItem("selectDate");
  window.location.href = PATHS.DAILY_PAGE + `?date=${date}`;
}

function areJumpRecordsEqual(oldRecords, newRecords) {
  if (oldRecords.length !== newRecords.length) return false;

  for (let i = 0; i < oldRecords.length; i++) {
    const o = oldRecords[i];
    const n = newRecords[i];

    // nullやundefinedの取り扱いも含めて厳密比較
    if (
      (o.jumpEventId || "") !== (n.jumpEventId || "") ||
      (o.event || "") !== (n.event || "") ||
      parseFloat(o.distance || 0) !== parseFloat(n.distance || 0) ||
      parseInt(o.approach || 0) !== parseInt(n.approach || 0) ||
      (o.poleFeat || "") !== (n.poleFeat || "") ||
      (o.polePond || "") !== (n.polePond || "") ||
      new Date(o.jumpDate).toISOString().slice(0, 10) !== new Date(n.jumpDate).toISOString().slice(0, 10) ||
      (o.recordType || "") !== (n.recordType || "")
    ) {
      return false;
    }
  }

  return true;
}