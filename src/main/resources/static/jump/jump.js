document.addEventListener("DOMContentLoaded", function () {
  const distanceSelect = document.getElementById("distance");
  const approachSelect = document.getElementById("approach");
  const poleFeatSelect = document.getElementById("pole-feat");
  const polePondSelect = document.getElementById("pole-pond");
  const poleFeatDiv = document.getElementById("pole-feat-div");
  const polePondDiv = document.getElementById("pole-pond-div");
  const eventSelect = document.getElementById("event");

  // 記録：3.50～9.00（0.10刻み）
  for (let i = 3.5; i <= 9.0; i += 0.1) {
    const option = document.createElement("option");
    option.value = i.toFixed(2);
    option.textContent = `${i.toFixed(2)} m`;
    distanceSelect.appendChild(option);
  }

  // 助走距離：8〜24歩
  for (let i = 8; i <= 24; i++) {
    const option = document.createElement("option");
    option.value = i;
    option.textContent = `${i} 歩`;
    approachSelect.appendChild(option);
  }

  // ポールの長さ：3.80m〜5.20m（0.10刻み）
  for (let i = 3.8; i <= 5.2; i += 0.1) {
    const option = document.createElement("option");
    option.value = i.toFixed(2);
    option.textContent = `${i.toFixed(2)} m`;
    poleFeatSelect.appendChild(option);
  }

  // ポールの硬さ：100〜200（10刻み）
  for (let i = 100; i <= 200; i += 10) {
    const option = document.createElement("option");
    option.value = i;
    option.textContent = `${i}`;
    polePondSelect.appendChild(option);
  }

  // 棒高跳び選択時にポール情報を表示
  eventSelect.addEventListener("change", function () {
    const isPoleVault = eventSelect.value === "pole_vault";
    poleFeatDiv.style.display = isPoleVault ? "block" : "none";
    polePondDiv.style.display = isPoleVault ? "block" : "none";
  });

  // 初期表示時にもチェック
  if (eventSelect.value === "pole_vault") {
    poleFeatDiv.style.display = "block";
    polePondDiv.style.display = "block";
  }
});

function submitAllJumpRecords() {
  const record = {
    event: document.getElementById("event").value,
    distance: document.getElementById("distance").value,
    approach: document.getElementById("approach").value,
    poleFeat: document.getElementById("pole-feat").value,
    polePond: document.getElementById("pole-pond").value,
  };

  console.log("送信された跳躍記録:", record);

  // API送信処理などはここに追記
}