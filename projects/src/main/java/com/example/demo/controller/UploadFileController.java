package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.beans.AssetContants;
import com.example.demo.core.RetResponse;
import com.example.demo.core.RetResult;
import com.example.demo.core.utils.UploadActionUtil;
import com.example.demo.dao.Assents;
import com.example.demo.dao.Liabilities;
import com.example.demo.mapper.AssentsMapper;
import com.example.demo.mapper.LiabilitiesMapper;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/uploadFile")
@Api(tags = {"文件上传接口"}, description = "UploadFileController")
public class UploadFileController {

    @Autowired
    private AssentsMapper assentsMapper;
    @Autowired
    private LiabilitiesMapper liabilitiesMapper;

    private final static Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @PostMapping("/upload")
    public RetResult<List<String>> upload(HttpServletRequest httpServletRequest) throws Exception {
        List<String> list = UploadActionUtil.uploadFile(httpServletRequest);
        return RetResponse.makeOKRsp(list);
    }

    @RequestMapping(value = "/import", method = RequestMethod.GET)
    public Object importExcel() throws Exception {
        HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(ResourceUtils.getFile("C:\\Users\\kjNIT\\Desktop\\华鸿第四季度报表.xls")));
        doSheet1(book);
        doSheet2(book);
        doSheet3(book);
        return null;
    }

    private void doSheet3(HSSFWorkbook book){

    }

    private void doSheet2(HSSFWorkbook book){

    }

    private void doSheet1(HSSFWorkbook book) throws ParseException {
        HSSFSheet sheet = book.getSheetAt(0);
        Assents preAssent = new Assents();
        Assents aftAssent = new Assents();
        Liabilities preLiabs = new Liabilities();
        Liabilities aftLiabs = new Liabilities();
        logger.info("rowNum:{}",sheet.getLastRowNum());
        HashMap<String,Double> preAssentMap = new HashMap<>();
        HashMap<String,Double> aftAssentMap = new HashMap<>();
        HashMap<String,Double> preLiabilitiesMap = new HashMap<>();
        HashMap<String,Double> aftLiabilitiesMap = new HashMap<>();

        for (int i = 6; i < sheet.getLastRowNum()+1; i++) {
            HSSFRow row = sheet.getRow(i);
            //资产
            if(null != row && null != row.getCell(0)){
                String key = row.getCell(0).getStringCellValue().replaceAll("\\s*","");
                try {
                    if(StringUtils.isNotBlank(key))
                        preAssentMap.put(key,row.getCell(3).getNumericCellValue());
                    if(StringUtils.isNotBlank(key))
                        aftAssentMap.put(key,row.getCell(2).getNumericCellValue());
                } catch (Exception e) {
                    logger.error("error:{}",e);
                }
            }
            //负债
            if(null != row && null != row.getCell(4)){
                String key = row.getCell(4).getStringCellValue().replaceAll("\\s*","");
                try {
                    if(StringUtils.isNotBlank(key))
                        preLiabilitiesMap.put(key,row.getCell(7).getNumericCellValue());
                    if(StringUtils.isNotBlank(key))
                        aftLiabilitiesMap.put(key,row.getCell(6).getNumericCellValue());
                } catch (Exception e) {
                    logger.error("error:{}",e);
                }
            }
        }
        Date month = changeDate(sheet.getRow(1).getCell(0).getStringCellValue());
        String orgName = sheet.getRow(3).getCell(0).getStringCellValue();
        //资产期末数
        aftAssent = saveAssetResult(aftAssentMap,aftAssent);
        aftAssent.setStatus((byte)1);
        aftAssent.setOrg_name(orgName);
        aftAssent.setMonth(month);
        //资产期初数
        preAssent = saveAssetResult(preAssentMap,preAssent);
        preAssent.setStatus((byte)0);
        preAssent.setOrg_name(orgName);
        preAssent.setMonth(month);
        assentsMapper.insertSelective(preAssent);
        assentsMapper.insertSelective(aftAssent);
        logger.info("导入资产信息成功");

        //负债期末数
        aftLiabs = saveLiabilitiesResult(aftLiabilitiesMap,aftLiabs);
        aftLiabs.setStatus((byte)1);
        aftLiabs.setOrg_name(orgName);
        aftLiabs.setMonth(month);
        //负债期初数
        preLiabs = saveLiabilitiesResult(preLiabilitiesMap,preLiabs);
        preLiabs.setStatus((byte)0);
        preLiabs.setOrg_name(orgName);
        preLiabs.setMonth(month);
        liabilitiesMapper.insertSelective(preLiabs);
        liabilitiesMapper.insertSelective(aftLiabs);
        logger.info("导入负债信息成功");
    }

    private BigDecimal changeNum(Double num){
        if(null != num)
            return new BigDecimal(num);
        return BigDecimal.ZERO;
    }

    private Date changeDate(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.parse(dateStr);
    }

    private Assents saveAssetResult(HashMap<String,Double> map,Assents assents){
        assents.setMoney_funds(changeNum(map.get(AssetContants.AC_01.getMsg())));
        assents.setTrading_financial_assets(changeNum(map.get(AssetContants.AC_02.getMsg())));
        assents.setNotes_receivable(changeNum(map.get(AssetContants.AC_03.getMsg())));
        assents.setReceivables(changeNum(map.get(AssetContants.AC_04.getMsg())));
        assents.setPrepayment(changeNum(map.get(AssetContants.AC_05.getMsg())));
        assents.setInterest_receivable(changeNum(map.get(AssetContants.AC_06.getMsg())));
        assents.setDividends_receivable(changeNum(map.get(AssetContants.AC_07.getMsg())));
        assents.setOther_receivables(changeNum(map.get(AssetContants.AC_08.getMsg())));
        assents.setInventory(changeNum(map.get(AssetContants.AC_09.getMsg())));
        assents.setNon_current_assets_12m(changeNum(map.get(AssetContants.AC_10.getMsg())));
        assents.setOther_current_assets(changeNum(map.get(AssetContants.AC_11.getMsg())));
        assents.setTotal_current_assets(changeNum(map.get(AssetContants.AC_12.getMsg())));
        assents.setFinancial_assets_available_for_sale(changeNum(map.get(AssetContants.AC_13.getMsg())));
        assents.setHold_investment_due(changeNum(map.get(AssetContants.AC_14.getMsg())));
        assents.setLong_term_equity_investment(changeNum(map.get(AssetContants.AC_15.getMsg())));
        assents.setLong_term_receivables(changeNum(map.get(AssetContants.AC_16.getMsg())));
        assents.setInvestment_property(changeNum(map.get(AssetContants.AC_17.getMsg())));
        assents.setFixed_assets(changeNum(map.get(AssetContants.AC_18.getMsg())));
        assents.setConstruction_in_progress(changeNum(map.get(AssetContants.AC_19.getMsg())));
        assents.setEngineering_material(changeNum(map.get(AssetContants.AC_20.getMsg())));
        assents.setDisposal_of_fixed_assets(changeNum(map.get(AssetContants.AC_21.getMsg())));
        assents.setProductive_biological_asset(changeNum(map.get(AssetContants.AC_22.getMsg())));
        assents.setOil_and_gas_assets(changeNum(map.get(AssetContants.AC_23.getMsg())));
        assents.setIntangible_assets(changeNum(map.get(AssetContants.AC_24.getMsg())));
        assents.setDevelopment_expenditure(changeNum(map.get(AssetContants.AC_25.getMsg())));
        assents.setGoodwill(changeNum(map.get(AssetContants.AC_26.getMsg())));
        assents.setLong_term_unamortized_expenses(changeNum(map.get(AssetContants.AC_27.getMsg())));
        assents.setDeferred_tax_assets(changeNum(map.get(AssetContants.AC_28.getMsg())));
        assents.setOther_no_current_assets(changeNum(map.get(AssetContants.AC_29.getMsg())));
        assents.setTotal_no_current_assets(changeNum(map.get(AssetContants.AC_30.getMsg())));
        assents.setTotal_assets(changeNum(map.get(AssetContants.AC_31.getMsg())));
        logger.info("assent:{}",JSONObject.toJSONString(assents));
        return assents;
    }

    private Liabilities saveLiabilitiesResult(HashMap<String,Double> map,Liabilities liabilities){
        liabilities.setShort_term_borrowing(changeNum(map.get(AssetContants.BC_01.getMsg())));
        liabilities.setTrading_financial_liabilities(changeNum(map.get(AssetContants.BC_02.getMsg())));
        liabilities.setNotes_payable(changeNum(map.get(AssetContants.BC_03.getMsg())));
        liabilities.setAccounts_payable(changeNum(map.get(AssetContants.BC_04.getMsg())));
        liabilities.setDeposit_received(changeNum(map.get(AssetContants.BC_05.getMsg())));
        liabilities.setEmployee_pay_payable(changeNum(map.get(AssetContants.BC_06.getMsg())));
        liabilities.setTax_payable(changeNum(map.get(AssetContants.BC_07.getMsg())));
        liabilities.setAccrual_interest_payable(changeNum(map.get(AssetContants.BC_08.getMsg())));
        liabilities.setDividends_payable(changeNum(map.get(AssetContants.BC_09.getMsg())));
        liabilities.setOther_payables(changeNum(map.get(AssetContants.BC_10.getMsg())));
        liabilities.setNon_current_liabilities_due_12m(changeNum(map.get(AssetContants.BC_11.getMsg())));
        liabilities.setOther_current_liabilities(changeNum(map.get(AssetContants.BC_12.getMsg())));
        liabilities.setTotal_current_liabilities(changeNum(map.get(AssetContants.BC_13.getMsg())));
        liabilities.setLong_term_borrowing(changeNum(map.get(AssetContants.BC_14.getMsg())));
        liabilities.setBonds_payable(changeNum(map.get(AssetContants.BC_15.getMsg())));
        liabilities.setLong_term_payable(changeNum(map.get(AssetContants.BC_16.getMsg())));
        liabilities.setAccount_payable_special_funds(changeNum(map.get(AssetContants.BC_17.getMsg())));
        liabilities.setAnticipation_liabilities(changeNum(map.get(AssetContants.BC_18.getMsg())));
        liabilities.setDeferred_income_tax_liabilities(changeNum(map.get(AssetContants.BC_19.getMsg())));
        liabilities.setOther_non_current_liabilities(changeNum(map.get(AssetContants.BC_20.getMsg())));
        liabilities.setTotal_non_current_liabilities(changeNum(map.get(AssetContants.BC_21.getMsg())));
        liabilities.setTotal_liabilities(changeNum(map.get(AssetContants.BC_22.getMsg())));
        liabilities.setPaicl_up_capital(changeNum(map.get(AssetContants.BC_23.getMsg())));
        liabilities.setCapital_reserve(changeNum(map.get(AssetContants.BC_24.getMsg())));
        liabilities.setTreasury_stock(changeNum(map.get(AssetContants.BC_25.getMsg())));
        liabilities.setEarned_surplus(changeNum(map.get(AssetContants.BC_26.getMsg())));
        liabilities.setUndistributed_profit(changeNum(map.get(AssetContants.BC_27.getMsg())));
        liabilities.setEquity_attributable_parent_company(changeNum(map.get(AssetContants.BC_28.getMsg())));
        liabilities.setMinority_equity(changeNum(map.get(AssetContants.BC_29.getMsg())));
        liabilities.setTotal_shareholders_equity(changeNum(map.get(AssetContants.BC_30.getMsg())));
        liabilities.setTotal_liabilities_shareholders_equity(changeNum(map.get(AssetContants.BC_31.getMsg())));
        logger.info("liabilities:{}",JSONObject.toJSONString(liabilities));
        return liabilities;
    }

}
