// 基準パスを設定
const BASE_PATH = "";

function getPath(relativePath) {
  if (BASE_PATH.endsWith('/') && relativePath.startsWith('/')) {
    return BASE_PATH + relativePath.slice(1);
  }
  if (!BASE_PATH.endsWith('/') && !relativePath.startsWith('/')) {
    return BASE_PATH + '/' + relativePath;
  }
  return BASE_PATH + relativePath;
}

const PATHS = {
  LOGIN_PAGE: getPath("login/login.html"),
  HOME_PAGE: getPath("home/home.html"),
  DAILY_PAGE: getPath("daily/daily.html"),
  DAILYEDIT_PAGE: getPath("daily/dailyEdit.html"),
  DAILYCREATE_PAGE: getPath("dailyCreate/dailyCreate.html"),
  TOURNAMENT_PAGE: getPath("tournament/tournament.html"),
  TRAINING_PAGE: getPath("training/training.html"),
  JUMP_PAGE: getPath("jump/jump.html"),
  USERCREATE_PAGE: getPath("userCreate/userCreate.html"),
  PASSWORDRESET_PAGE: getPath("passwordReset/passwordReset.html")
};