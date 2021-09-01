<template>
  <div class="PredictionEvent">
    <div class="content">
      <div class="mainText">{{ mainData.name }}</div>
      <div class="predBox">
        <h2 class="predTit">kt ds AI Centro가 예측한 결과</h2>
        <ul class="predAiList">
          <li v-for="(item, index) in choicesList" v-bind:key="index">
            <strong>{{ item }}</strong> : {{ answersList[index] }}<span>%</span>
          </li>
        </ul>
        <div
          id="predDetail"
          style="display: none"
          v-html="mainData.analysis"
        ></div>
        <div class="btnMore" @click="openMore()">{{ btnMoreText }}</div>
      </div>
      <div class="choiceBox">
        <p>{{ mainData.description }}</p>
        <ul class="radioList">
          <li v-for="(item, index) in choicesList" v-bind:key="index">
            <input
              type="radio"
              :value="choicesList[index]"
              name="choice"
              :id="'choice' + index"
            />
            <label :for="'choice' + index">{{ choicesList[index] }}</label>
          </li>
        </ul>

        <div
          class="btnSubmit"
          @click="submitEvent()"
          v-if="this.eventFlag == 0"
        >
          이벤트 응모
        </div>
        <div v-if="this.eventFlag == 2">
          <p class="resultTit">{{ resultData.message }}</p>
        </div>
        <div v-if="this.eventFlag == 2 && this.resultData.result == 'success'">
          <div class="resultBox">
            <p class="resultStateTit">현재응모 현황</p>
            <ul class="resultList">
              <li
                v-for="(item, index) in choicesList"
                v-bind:key="index"
                :style="
                  'width:' +
                  (this.resultData.statistic[choicesList[index]] / this.total) *
                    100 +
                  '%'
                "
              >
                <span
                  >{{ choicesList[index] }} :
                  {{ this.resultData.statistic[choicesList[index]] }}명</span
                >
              </li>
            </ul>
          </div>
        </div>
        <div v-if="this.eventFlag == 3">
          <p class="resultTit">kt wiz 앱에서 로그인 한 후 응모해주세요.</p>
        </div>
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
      answersList: [],
      choicesList: [],
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
      if (this.$route.query.id == undefined) {
        this.eventFlag = 3;
        return false;
      }

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

      let param = {
        choice: choice,
        event: { id: this.mainData.id },
        userId: this.$route.query.id,
      };
      axios
        .post(
          "http://ec2-3-36-248-102.ap-northeast-2.compute.amazonaws.com/event/apply",
          param
        )
        .then((res) => {
          console.log(res);
          this.resultData = res.data;
          this.total = 0;
          for (let i = 0; i < this.choicesList.length; i++) {
            this.total =
              this.total + this.resultData.statistic[this.choicesList[i]];
          }
          this.eventFlag = 2;
          console.log(this.total);
        })
        .catch((err) => {
          console.log(err); //통신에러가 떴을때
        });
    },
    setData() {
      console.log(this.$route.query.id);
      console.log("통신 테스트 1");
      axios
        .get(
          "http://ec2-3-36-248-102.ap-northeast-2.compute.amazonaws.com/event/Available"
        )
        .then((res) => {
          console.log(res); //값을 불러왔을때
          this.mainData = res.data;
          this.choicesList = this.mainData.choices.split("_");
          this.answersList = this.mainData.answers.split("_");
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
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;300&display=swap");

* {
  margin: 0;
  padding: 0;
  font-family: "Noto Sans KR", sans-serif;
}
ul li {
  list-style: none;
}
a {
  text-decoration: none;
}
html {
  background: #272727;
}

.PredictionEvent {
  width: 100%;
  background: #fefefe;
  background: url("../assets/visual_bg.jpg") no-repeat center top;
  background-size: 100%;
  max-width: 800px;
  margin: auto;
}
.PredictionEvent .content {
  margin: auto;
  padding: 10px 10px 50px;
  padding-top: 45%;
}
.mainText {
  font-size: 30px;
  font-weight: bold;
  padding: 30px 10px 0;
  text-align: center;
  color: #fff;
}
.predBox {
  border: 1px solid #ccc;
  padding: 10px 20px;
  margin: 20px 0;
  border-radius: 5px;
  background: #fff;
  color: #333;
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
.choiceBox {
  font-size: 20px;
  color: #fff;
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
#predDetail h3 {
  font-size: 18px;
  padding-bottom: 10px;
  padding-top: 20px;
}
#predDetail img {
  max-width: 100%;
  margin: auto;
  display: block;
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
  color: #000;
  font-size: 16px;
}
.resultBox ul li {
  width: 0%;
  margin: 10px 0;
  height: 30px;
  background: #eee;
  position: relative;
  display: block;
  border-radius: 2px;
}
.resultBox ul li span {
  min-width: 320px;
  position: absolute;
  top: 3px;
  left: 10px;
}
</style>
