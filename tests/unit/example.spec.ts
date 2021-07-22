import { shallowMount } from "@vue/test-utils";
import PredictionEvent from "@/components/PredictionEvent.vue";

describe("PredictionEvent.vue", () => {
  it("renders props.msg when passed", () => {
    const msg = "new message";
    const wrapper = shallowMount(PredictionEvent, {
      props: { msg },
    });
    expect(wrapper.text()).toMatch(msg);
  });
});
