package com.passta.a2ndproj.main;

public class MsgCategoryPoint_VO {
    private double coronaRoute;
    private double coronaUpbreak;
    private double coronaSafetyRule;
    private double economy;
    private double disaster;

    public MsgCategoryPoint_VO(double coronaRoute, double coronaUpbreak, double coronaSafetyRule, double disaster, double economy) {
        //1. 동선 2. 발생방역 3. 안전수칙 4.재난상황 5. 경제금융
        this.coronaRoute = coronaRoute;
        this.coronaUpbreak = coronaUpbreak;
        this.coronaSafetyRule = coronaSafetyRule;
        this.disaster = disaster;
        this.economy = economy;
    }

    public double getCoronaRoute() {
        return coronaRoute;
    }

    public void setCoronaRoute(double coronaRoute) {
        this.coronaRoute = coronaRoute;
    }

    public double getCoronaUpbreak() {
        return coronaUpbreak;
    }

    public void setCoronaUpbreak(double coronaUpbreak) {
        this.coronaUpbreak = coronaUpbreak;
    }

    public double getCoronaSafetyRule() {
        return coronaSafetyRule;
    }

    public void setCoronaSafetyRule(double coronaSafetyRule) {
        this.coronaSafetyRule = coronaSafetyRule;
    }

    public double getEconomy() {
        return economy;
    }

    public void setEconomy(double economy) {
        this.economy = economy;
    }

    public double getDisaster() {
        return disaster;
    }

    public void setDisaster(double disaster) {
        this.disaster = disaster;
    }
}
