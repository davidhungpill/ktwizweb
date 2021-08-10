<template>
  <div class="PredictionEvent">
    <div class="content">
      <div class="mainText">{{ mainData.name }}</div>
      <div class="predBox">
        <h2 class="predTit">kt ds AI Centro가 예측한 결과</h2>
        <ul class="predAiList">
          <li><strong>예</strong> : <span>%</span></li>
          <li><strong>아니요</strong> : <span>%</span></li>
        </ul>
        <div id="predDetail" style="display: none">
          {{ mainData }}
        </div>
        <div class="btnMore" @click="openMore()">{{ btnMoreText }}</div>
      </div>
      <p>{{ mainData.description }}</p>
      <ul class="radioList">
        <li>
          <input type="radio" value="Yes" name="choice" id="choice01" />
          <label for="choice01">예</label>
        </li>
        <li>
          <input type="radio" value="No" name="choice" id="choice02" />
          <label for="choice02">아니요</label>
        </li>
      </ul>
      <div class="btnSubmit" @click="submitEvent()" v-if="this.eventFlag == 0">
        이벤트 응모
      </div>
      <div v-if="this.eventFlag == 2">
        <p class="resultTit">{{ resultData.message }}</p>
      </div>
      <div v-if="this.eventFlag == 2 && this.resultData.result == 'success'">
        <div class="resultBox">
          <p class="resultStateTit">현재응모 현황</p>
          <ul class="resultList">
            <li>예 : {{ resultData.statistic.Yes }}명</li>
            <li>아니요 : {{ resultData.statistic.No }}명</li>
          </ul>
        </div>
      </div>
      <div v-if="this.eventFlag == 3">
        <p class="resultTit">kt wiz 앱에서 로그인 한 후 응모해주세요.</p>
      </div>
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
      mainData: {},
      resultData: {},
      eventFlag: 0,
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
      let obj_length = document.getElementsByName("choice").length;
      let choice = "";
      for (var i = 0; i < obj_length; i++) {
        if (document.getElementsByName("choice")[i].checked == true) {
          choice = document.getElementsByName("choice")[i].value;
        }
      }
      if (choice == "") {
        alert("의견을 선택해 주세요.");
        return false;
      }
      console.log(this.$route.query.id);
      if (this.$route.query.id == undefined) {
        this.eventFlag = 3;
        return false;
      }
      let param = {
        choice: choice,
        event: { id: this.mainData.id },
        userId: this.$route.query.id,
      };
      axios
        .post("http://ktwiz.api.ktds.amazonaws.com/event/apply", param)
        .then((res) => {
          this.resultData = res.data;
          this.eventFlag = 2;
        })
        .catch((err) => {
          console.log(err); //통신에러가 떴을때
        });
    },
    setData() {
      console.log(this.$route.query.id);
      console.log("통신 테스트 1");
      axios
        .get("http://ktwiz.api.ktds.amazonaws.com/event/Available")
        .then((res) => {
          console.log(res); //값을 불러왔을때
          this.mainData = res.data;
        })
        .catch((err) => {
          console.log(err); //통신에러가 떴을때
        });
      console.log("통신 테스트 2");
    },
  },
  mounted() {
    this.setData();
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
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
  padding-bottom: 20px;
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
.resultTit {
  text-align: center;
  font-size: 22px;
  font-weight: bold;
}
.resultStateTit {
  font-size: 18px;
  padding: 5px 0 5px;
  font-weight: bold;
}
.resultBox {
  border: 1px solid #ccc;
  padding: 10px 20px;
  margin: 20px 0;
  border-radius: 5px;
  background: #fff;
}
</style>
