<template>
  <div class="PredictionEvent">
    <div class="content">
      <div class="mainText">강백호의 4할은 오늘도 계속된다?</div>
      <div class="predBox">
        <h2 class="predTit">kt ds AI Centro가 예측한 결과</h2>
        <ul class="predAiList">
          <li><strong>예</strong> : <span>83%</span></li>
          <li><strong>아니요</strong> : <span>17%</span></li>
        </ul>
        <div id="predDetail" style="display: none">
          자세한 내용 자세한 내용 자세한 내용 자세한 내용 자세한 내용 자세한
          내용 <br />
          자세한 내용 자세한 내용 자세한 내용 자세한 내용 자세한 내용 자세한
          내용 <br />
          자세한 내용 자세한 내용 자세한 내용 자세한 내용 자세한 내용 자세한
          내용 <br />
        </div>
        <div class="btnMore" @click="openMore()">{{ btnMoreText }}</div>
      </div>
      <ul class="radioList">
        <li>
          <input type="radio" value="Y" name="a" id="a" />
          <label for="a">예</label>
        </li>
        <li>
          <input type="radio" value="N" name="a" id="b" />
          <label for="b">아니요</label>
        </li>
      </ul>
      <div class="btnSubmit" @click="submitEvent()">이벤트 응모</div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      items: [],
      totalItems: 0,
      totalPages: 0,
      btnMoreText: "자세히 보기",
    };
  },
  methods: {
    openMore() {
      let predDetail = document.getElementById("predDetail");
      if (!predDetail) return;
      if (predDetail.style.display == "none") {
        predDetail.style.display = "block";
        this.btnMoreText = "접기";
      } else {
        predDetail.style.display = "none";
        this.btnMoreText = "자세히 보기";
      }
    },
    submitEvent() {
      console.log("통신 테스트 1");
      axios
        .post("http://ktwiz.api.ktds.amazonaws.com/event/apply")
        .then((res) => {
          console.log(res); //값을 불러왔을때
        })
        .catch((err) => {
          console.log(err); //통신에러가 떴을때
        });
      console.log("통신 테스트 2");
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
.PredictionEvent {
  width: 100%;
  background: #fefefe;
}
.PredictionEvent .content {
  max-width: 900px;
  margin: auto;
  padding: 10px 10px 50px;
  min-height: 75vh;
}
.mainText {
  font-size: 25px;
  font-weight: bold;
  padding: 30px 10px 10px;
  text-align: center;
}
.predBox {
  border: 1px solid #ccc;
  padding: 10px 20px;
  margin: 20px 0;
  border-radius: 5px;
  background: #fff;
}
.predTit {
  font-size: 20px;
  padding-bottom: 10px;
}
.predAiList {
  padding-bottom: 10px;
}
.predAiList li {
  padding: 5px 0;
}
.radioList {
  padding-bottom: 40px;
}
.radioList li {
  padding: 5px 0;
}
.radioList li label {
  padding-left: 10px;
}
#predDetail {
  padding-bottom: 20px;
  display: none;
}
.btnMore {
  width: 100px;
  font-size: 14px;
  text-align: center;
  padding: 8px 10px;
  margin: auto;
  background: #000;
  color: #fff;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}
.btnSubmit {
  width: 140px;
  margin: auto;
  text-align: center;
  padding: 10px;
  background: #ec0d17;
  color: #fff;
  font-weight: bold;
  border-radius: 5px;
  cursor: pointer;
}
</style>
