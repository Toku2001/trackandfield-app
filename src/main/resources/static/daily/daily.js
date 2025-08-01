document.addEventListener("DOMContentLoaded", async function () {
  const urlParams = new URLSearchParams(window.location.search);
  const date = urlParams.get("date");
  const token = sessionStorage.getItem("token");

  const resTraining = await fetch(`http://localhost:8080/api/get-training?trainingDate=${date}`, {
    headers: { "Authorization": `Bearer ${token}` }
  });

  const dataTraining = await resTraining.json();
  document.getElementById("training-time").textContent = dataTraining.trainingTime;
  document.getElementById("training-place").textContent = dataTraining.trainingPlace;
  document.getElementById("training-comments").textContent = dataTraining.trainingComments
;

  const resCompetitions = await fetch(`http://localhost:8080/api/get-competition?competitionDate=${date}`, {
    headers: { "Authorization": `Bearer ${token}` }
  });

  const dataCompetitions = await resCompetitions.json();
  document.getElementById("tournament-time").textContent = dataCompetitions.competitionTime;
  document.getElementById("tournament-place").textContent = dataCompetitions.competitionPlace;
  document.getElementById("tournament-name").textContent = dataCompetitions.competitionName;
  document.getElementById("tournament-comments").textContent = dataCompetitions.competitionComments
;
});

function goToHome() {
  window.location.href = PATHS.HOME_PAGE;
}

function goToEdit() {
  const trainingTime = document.getElementById("training-time");
  const trainingPlace = document.getElementById("training-place");
  const trainingText = document.getElementById("training-comments");
  sessionStorage.setItem("trainingTime", trainingTime.textContent);
  sessionStorage.setItem("trainingPlace", trainingPlace.textContent);
  sessionStorage.setItem("trainingComments", trainingText.textContent);
  
  const competitionTime = document.getElementById("tournament-time");
  const competitionPlace = document.getElementById("tournament-place");
  const competitionText = document.getElementById("traitournamentning-comments");
  const competitionName = document.getElementById("tournament-name");
  sessionStorage.setItem("competitionTime", competitionTime.textContent);
  sessionStorage.setItem("competitionPlace", competitionPlace.textContent);
  sessionStorage.setItem("competitionText", competitionText.textContent);
  sessionStorage.setItem("competitionName", competitionName.textContent);
  window.location.href = PATHS.DAILYEDIT_PAGE;
}
