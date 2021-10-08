<template>
  <div class="PredictionEvent">
    <div class="content">
      <div class="mainText">{{ mainData.name }}</div>
      <div class="predBox">
        <h2 class="predTit">kt ds AI Centro가 예측한 결과</h2>
        <ul class="predAiList">
          <li v-for="(item, index) in mainData.answers" v-bind:key="index">
            <strong>{{ questions["content" + item.question] }}</strong> :
            {{ item.answer }}
          </li>
        </ul>
        <div
          id="predDetail"
          style="display: none"
          v-html="mainData.analysis"
        ></div>
        <div class="btnMore" @click="openMore()">{{ btnMoreText }}</div>
      </div>
      <div
        class="choiceBox"
        v-for="(item, i) in mainData.questions"
        v-bind:key="i"
      >
        <p :id="'content' + item.id">{{ questions["content" + item.id] }}</p>
        <ul class="radioList">
          <li
            v-for="(subItem, j) in questions['choices' + item.id]"
            v-bind:key="j"
          >
            <input
              type="radio"
              :value="subItem"
              :name="'choice' + item.id"
              :id="'choice' + item.id + '_' + j"
            />
            <label :for="'choice' + item.id + '_' + j">{{ subItem }}</label>
          </li>
        </ul>
      </div>
      <div class="btnSubmit" @click="submitEvent()" v-if="this.eventFlag == 0">
        이벤트 응모
      </div>
      <div v-if="this.eventFlag == 2">
        <p class="resultTit">{{ resultData.message }}</p>
      </div>
      <div v-if="this.eventFlag == 2 && this.resultData.result == 'success'">
        <div class="resultBox">
          <p class="resultStateTit">현재응모 현황</p>
          <div v-for="(item, i) in total" v-bind:key="i">
            <p class="resultSubTit">{{ item.content }}</p>
            <ul class="resultList">
              <li
                v-for="(info, j) in total[i].info"
                v-bind:key="j"
                :style="'width:' + (info.cnt / total[i].cnt) * 100 + '%;'"
              >
                <span>{{ info.choice }} : {{ info.cnt }}명</span>
              </li>
            </ul>
          </div>
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
      questions: {},
      choicesList: {},
      resultData: {},
      eventFlag: 0,
      total: {},
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
      console.log(this.mainData.questions.length);
      let param = [];
      for (let i = 0; i < this.mainData.questions.length; i++) {
        let obj_length = document.getElementsByName(
          "choice" + this.mainData.questions[i].id
        ).length;
        let question = document.getElementById(
          "content" + this.mainData.questions[i].id
        ).innerText;
        let choiceObj = {
          event: { id: this.mainData.id },
          question: {
            id: this.mainData.questions[i].id,
          },
          userId: this.$route.query.id,
        };
        for (let j = 0; j < obj_length; j++) {
          if (
            document.getElementsByName(
              "choice" + this.mainData.questions[i].id
            )[j].checked == true
          ) {
            choiceObj.choice = document.getElementsByName(
              "choice" + this.mainData.questions[i].id
            )[j].value;
          }
        }
        if (choiceObj.choice == "" || choiceObj.choice == undefined) {
          alert("'" + question + "'의 의견을 선택해 주세요.");
          return false;
        }
        param.push(choiceObj);
      }
      axios
        .post(
          "http://ec2-3-36-248-102.ap-northeast-2.compute.amazonaws.com/event/apply",
          param
        )
        .then((res) => {
          this.resultData = res.data;
          this.total = {};
          if (this.resultData.result == "success") {
            for (let i = 0; i < this.resultData.statistic.length; i++) {
              let target = "question" + this.resultData.statistic[i].question;
              if (this.total[target] == undefined) {
                (this.total[target] = {}), (this.total[target].cnt = 0);
                this.total[target].content =
                  this.questions[
                    "content" + this.resultData.statistic[i].question
                  ];
                this.total[target].info = [];
              }
              this.total[target].cnt =
                this.total[target].cnt + this.resultData.statistic[i].cnt;
              this.total[target].info.push(this.resultData.statistic[i]);
            }
          }
          console.log(this.total);
          this.eventFlag = 2;
        })
        .catch((err) => {
          console.log(err); //통신에러가 떴을때
        });
    },
    setData() {
      axios
        .get(
          "http://ec2-3-36-248-102.ap-northeast-2.compute.amazonaws.com/event/Available"
        )
        .then((res) => {
          this.mainData = res.data;
          this.questions = {};
          for (var i = 0; i < this.mainData.questions.length; i++) {
            this.questions["choices" + this.mainData.questions[i].id] =
              this.mainData.questions[i].choices.split("_");
            this.questions["content" + this.mainData.questions[i].id] =
              this.mainData.questions[i].content;
          }
        })
        .catch((err) => {
          console.log(err); //통신에러가 떴을때
        });
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
  color: #fff;
}
.resultStateTit {
  font-size: 20px;
  padding: 5px 0 5px;
  font-weight: bold;
}
.resultSubTit {
  font-weight: bold;
  padding-top: 10px;
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
