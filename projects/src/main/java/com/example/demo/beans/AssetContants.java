package com.example.demo.beans;

public enum AssetContants {

    AC_01("货币资金","money_funds"),AC_02("交易性金融资产","trading_financial_assets"),AC_03("应收票据","notes_receivable"),
    AC_04("应收账款","receivables"),AC_05("预付账款","prepayment"),AC_06("应收利息","interest_receivable"),
    AC_07("应收股利","dividends_receivable"),AC_08("其他应收款","other_receivables"),AC_09("存货","inventory"),
    AC_10("一年内到期的非流动资产","non_current_assets_12m"),AC_11("其他流动资产","other_current_assets"),AC_12("流动资产合计","total_current_assets"),
    AC_13("可供出售的金融资产","financial_assets_available_for_sale"),AC_14("持有至到期投资","hold_investment_due"),AC_15("长期股权投资","long_term_equity_investment"),
    AC_16("长期应收款","long_term_receivables"),AC_17("投资性房地产","investment_property"),AC_18("固定资产","fixed_assets"),
    AC_19("在建工程","construction_in_progress"),AC_20("工程物资","engineering_material"),AC_21("固定资产清理","disposal_of_fixed_assets"),
    AC_22("生产性生物资产","productive_biological_asset"),AC_23("油气资产","oil_and_gas_assets"),AC_24("无形资产","intangible_assets"),
    AC_25("开发支出","development_expenditure"),AC_26("商誉","goodwill"),AC_27("长期待摊费用","long_term_unamortized_expenses"),
    AC_28("递延所得税资产","deferred_tax_assets"),AC_29("其他非流动资产","other_no_current_assets"),AC_30("非流动资产合计","total_no_current_assets"),
    AC_31("资产合计","total_assets"),

    BC_01("短期借款","short_term_borrowing"),BC_02("交易性金融负债","trading_financial_liabilities"),BC_03("应付票据","notes_payable"),
    BC_04("应付账款","accounts_payable"),BC_05("预收账款","deposit_received"),BC_06("应付职工薪酬","employee_pay_payable"),
    BC_07("应交税费","tax_payable"),BC_08("应付利息","accrual_interest_payable"),BC_09("应付股利","dividends_payable"),
    BC_10("其他应付款","other_payables"),BC_11("一年内到期的非流负债","non_current_liabilities_due_12m"),BC_12("其他流动负债","other_current_liabilities"),
    BC_13("流动负债总计","total_current_liabilities"),BC_14("长期借款","long_term_borrowing"),BC_15("应付债券","bonds_payable"),
    BC_16("长期应付款","long_term_payable"),BC_17("专项应付款","account_payable_special_funds"),BC_18("预计负债","anticipation_liabilities"),
    BC_19("递延所得税负债","deferred_income_tax_liabilities"),BC_20("其他非流动负债","other_non_current_liabilities"),BC_21("非流动负债合计","total_non_current_liabilities"),
    BC_22("负债合计","total_liabilities"),BC_23("实收资本(或股本)","paicl_up_capital"),BC_24("资本公积","capital_reserve"),
    BC_25("减：库存股","treasury_stock"),BC_26("盈余公积","earned_surplus"),BC_27("未分配利润","undistributed_profit"),
    BC_28("归属于母公司所有者权益合计","equity_attributable_parent_company"),BC_29("少数股东权益","minority_equity"),BC_30("股东权益合计","total_shareholders_equity"),
    BC_31("负债及股东权益总计","total_liabilities_shareholders_equity");

    private String code;
    private String msg;

    AssetContants(String msg, String code){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
