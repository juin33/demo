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
    BC_31("负债及股东权益总计","total_liabilities_shareholders_equity"),

    PR_01("营业收入","operating_income"),PR_02("营业成本","operating_costs"),PR_03("营业税金及附加","business_tax_and_surcharges"),
    PR_04("销售费用","selling_expenses"),PR_05("管理费用","manage_expenses"),PR_06("财务费用","financial_expense"),
    PR_07("资产减值损失","assets_impairment_loss"),PR_08("公允价值变动净收益","changes_in_fair_net_income"),PR_09("投资收益","investment_income"),
    PR_10("对联营企业和合营企业的投资收益","investment_joint_ventures_income"),PR_11("营业利润","operating_profit"),PR_12("营业外收入","operating_outer_income"),
    PR_13("非流动资产处置净损失","no_current_assent_loss"),PR_14("利润总额","total_profit"),PR_15("所得税费用","tax_cost"),
    PR_16("净利润","retained_profits"),PR_17("归属于母公司所有者的净利润","retained_profits_parent_company"),PR_18("少数股东损益","minority_interest_income"),
    PR_19("每股收益","earnings_per_share"),PR_20("基本每股收益","earnings_per_share_basic"),PR_21("稀释每股收益","earnings_per_share_attenuation"),

    FW_01("经营活动产生的现金流量","cash_flow_from_operating_activities"),FW_02("销售商品、提供劳务收到的现金","selling_goods_and_providing_services_cash"),FW_03("收到的税费返还","refun_of_tax_levies"),
    FW_04("收到其他与经营活动有关的现金","other_cash_related_operating_activities"),FW_05("经营活动现金流入小计","cash_inflows_operating_activities"),FW_06("购买商品、接受劳务支付的现金","cash_payment_for_goods_and_services"),
    FW_07("支付给职工以及为职工支付的现金","cash_paid_employees"),FW_08("支付的各项税费","tax_payments"),FW_09("支付其他与经营活动有关的现金","payment_other_cash_related_business_activities"),
    FW_10("经营活动现金流出小计","cash_outflow_operating_activities"),FW_11("经营活动产生的现金流量净额","net_cash_flow_from_operating_activities"),FW_12("投资活动产生的现金流量","cash_flow_from_investment_activities"),
    FW_13("收回投资收到的现金","proceeds_from_sell_of_investment"),FW_14("取得投资收益收到的现金","cash_received_on_investment_income"),FW_15("处置固定资产、无形资产和其他长期资产收回的现金净额","net_cash_fixed_intangible_other_long_term_assets"),
    FW_16("处置子公司及其他营业单位收到的现金净额","net_cash_by_subsidiaries_and_other_business_units"),FW_17("收到其他与投资活动有关的现金","other_cash_related_to_investment_activities"),FW_18("投资活动现金流入小计","cash_inflow_from_investment_activities"),
    FW_19("购建固定资产、无形资产和其他长期资产支付的现金","cash_paid_fixed_intangible_other_long_term_assets"),FW_20("投资支付的现金","cash_paid_for_investment"),FW_21("取得子公司及其他营业单位支付的现金净额","net_cash_paid_by_subsidiaries_and_other_business_units"),
    FW_22("支付其他与投资活动有关的现金","pay_other_cash_related_to_investment_activities"),FW_23("投资活动现金流出小计","cash_outflow_for_investment_activities"),FW_24("投资活动产生的现金流量净额","net_cash_flow_from_investment_activities"),
    FW_25("筹资活动产生的现金流量","cash_flow_from_fundraising_activities"),FW_26("吸收投资收到的现金","receipts_equity_securities"),FW_27("子公司吸收少数股东投资收到的现金","subsidiaries_receive_cash_from_minority_investors"),
    FW_28("取得借款收到的现金","receipts_from_loan"),FW_29("收到其他与筹资活动有关的现金","receipt_of_other_cash_related_fund_raising_activities"),FW_30("筹资活动现金流入小计","cash_inflow_from_financing_activities"),
    FW_31("偿还债务支付的现金","payment_for_debt"),FW_32("分配股利、利润或偿付利息支付的现金","cash_to_distribute_dividends_or_pay_interest"),FW_33("子公司支付给少数股东的股利、利润","dividends_paid_by_subsidiary_to_minority_shareholders"),
    FW_34("支付其他与筹资活动有关的现金","payment_of_other_cash_related_fund_raising_activities"),FW_35("筹资活动现金流出小计","cash_outflow_financing_activities"),FW_36("筹资活动产生的现金流量净额","net_cash_flow_from_financing_activities"),
    FW_37("汇率变动对现金及现金等价物的影响","effect_of_exchange_rate_and_cash_equivalents"),FW_38("现金及现金等价物净增加额","net_increase_in_cash_and_cash_equivalents"),FW_39("期初现金及现金等价物余额","beginning_balance_cash_and_cash_equivalents"),
    FW_40("期末现金及现金等价物余额","ending_balance_cash_and_cash_equivalents");

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
