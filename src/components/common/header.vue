<template>
  <header>
    <h1>
      <a href="#" class="logo"
        ><img alt="KT Wiz" src="../../assets/logo_w.png"
      /></a>
      <span class="title">"AI를 이겨라" 이벤트</span>
    </h1>
  </header>
</template>
<script>
export default {};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300&display=swap");

* {
  margin: 0;
  padding: 0;
  font-family: "Noto Sans KR", sans-serif;
  color: #333;
}
ul li {
  list-style: none;
}
a {
  text-decoration: none;
  color: #333;
}
html {
  background: #111;
}
header {
  background: #111;
  border-bottom: 5px solid #ec0d17;
}
header h1 {
  position: relative;
  text-align: center;
  font-size: 25px;
  padding: 10px 30px;
  font-weight: bold;
}
header h1 .logo {
  position: absolute;
  left: 10px;
  top: 8px;
}
header h1 .title {
  color: #fff;
}
header h1 .logo img {
  width: 55px;
}
</style>
