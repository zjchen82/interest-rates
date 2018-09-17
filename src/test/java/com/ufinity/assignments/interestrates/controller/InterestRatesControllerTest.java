package com.ufinity.assignments.interestrates.controller;

import com.ufinity.assignments.interestrates.InterestRatesController;
import com.ufinity.assignments.interestrates.InterestRatesService;
import com.ufinity.assignments.interestrates.ServiceException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static com.ufinity.assignments.interestrates.ControllerExceptionHandler.XMLHTTP_REQUEST;
import static com.ufinity.assignments.interestrates.ControllerExceptionHandler.X_REQUESTED_WITH;
import static com.ufinity.assignments.interestrates.InterestRatesController.URL_INTEREST_RATES;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(InterestRatesController.class)
public class InterestRatesControllerTest {

  private static final String SAMPLE_DATA = "{\"success\":true,\"result\":{\"resource_id\":[\"5f2b18a8-0883-4769-a635-879c63d3caac\"],\"limit\":100,\"total\":\"20\",\"records\":[{\"end_of_month\":\"2017-01\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.15\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.18\",\"fc_hire_purchase_motor_3y\":\"4.97\",\"fc_housing_loans_15y\":\"3.41\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-02\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.15\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.18\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.41\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-03\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-04\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-05\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-06\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-07\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-08\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-09\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-10\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.26\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-11\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.06\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2017-12\",\"prime_lending_rate\":\"5.28\",\"banks_fixed_deposits_3m\":\"0.14\",\"banks_fixed_deposits_6m\":\"0.20\",\"banks_fixed_deposits_12m\":\"0.33\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.06\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2018-01\",\"prime_lending_rate\":\"5.33\",\"banks_fixed_deposits_3m\":\"0.15\",\"banks_fixed_deposits_6m\":\"0.22\",\"banks_fixed_deposits_12m\":\"0.34\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.02\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2018-02\",\"prime_lending_rate\":\"5.33\",\"banks_fixed_deposits_3m\":\"0.15\",\"banks_fixed_deposits_6m\":\"0.22\",\"banks_fixed_deposits_12m\":\"0.34\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.02\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2018-03\",\"prime_lending_rate\":\"5.33\",\"banks_fixed_deposits_3m\":\"0.15\",\"banks_fixed_deposits_6m\":\"0.22\",\"banks_fixed_deposits_12m\":\"0.34\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"5.02\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2018-04\",\"prime_lending_rate\":\"5.33\",\"banks_fixed_deposits_3m\":\"0.15\",\"banks_fixed_deposits_6m\":\"0.22\",\"banks_fixed_deposits_12m\":\"0.34\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"4.83\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2018-05\",\"prime_lending_rate\":\"5.33\",\"banks_fixed_deposits_3m\":\"0.15\",\"banks_fixed_deposits_6m\":\"0.22\",\"banks_fixed_deposits_12m\":\"0.37\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"4.83\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2018-06\",\"prime_lending_rate\":\"5.33\",\"banks_fixed_deposits_3m\":\"0.15\",\"banks_fixed_deposits_6m\":\"0.22\",\"banks_fixed_deposits_12m\":\"0.37\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"4.83\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2018-07\",\"prime_lending_rate\":\"5.33\",\"banks_fixed_deposits_3m\":\"0.16\",\"banks_fixed_deposits_6m\":\"0.23\",\"banks_fixed_deposits_12m\":\"0.38\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":\"4.83\",\"fc_housing_loans_15y\":\"3.16\",\"fc_fixed_deposits_3m\":\"0.30\",\"fc_fixed_deposits_6m\":\"0.38\",\"fc_fixed_deposits_12m\":\"0.50\",\"fc_savings_deposits\":\"0.17\",\"timestamp\":\"1536940464\"},{\"end_of_month\":\"2018-08\",\"prime_lending_rate\":\"5.33\",\"banks_fixed_deposits_3m\":\"0.16\",\"banks_fixed_deposits_6m\":\"0.23\",\"banks_fixed_deposits_12m\":\"0.40\",\"banks_savings_deposits\":\"0.16\",\"fc_hire_purchase_motor_3y\":null,\"fc_housing_loans_15y\":null,\"fc_fixed_deposits_3m\":null,\"fc_fixed_deposits_6m\":null,\"fc_fixed_deposits_12m\":null,\"fc_savings_deposits\":null,\"timestamp\":\"1536940464\"}]}}";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private InterestRatesService interestRatesService;

  @Before
  public void setUp() {
  }

  @Test
  public void getInterestRates_Empty_Months() throws Exception {

    mvc.perform(get(URL_INTEREST_RATES)
        .param("startMonth", "")
        .param("endMonth", "")
        .header(X_REQUESTED_WITH, XMLHTTP_REQUEST)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors", hasSize(2)));
  }

  @Test
  public void getInterestRates_Invalid_Months_Format() throws Exception {

    mvc.perform(get(URL_INTEREST_RATES)
        .param("startMonth", "01/01/2018")
        .param("endMonth", "31/08/2018")
        .header(X_REQUESTED_WITH, XMLHTTP_REQUEST)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors", hasSize(2)));
  }

  @Test
  public void getInterestRates_StartMonth_Earlier_Than_EndMonth() throws Exception {

    mvc.perform(get(URL_INTEREST_RATES)
        .param("startMonth", "2018-01")
        .param("endMonth", "2017-01")
        .header(X_REQUESTED_WITH, XMLHTTP_REQUEST)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors", hasSize(1)));
  }

  @Test
  public void getInterestRates_Processing_Fail() throws Exception {
    doThrow(ServiceException.class).when(interestRatesService).getData(any(Date.class), any(Date.class));

    mvc.perform(get(URL_INTEREST_RATES)
        .param("startMonth", "2018-01")
        .param("endMonth", "2018-08")
        .header(X_REQUESTED_WITH, XMLHTTP_REQUEST)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors", hasSize(1)));
  }

  @Test
  public void getInterestRates() throws Exception {

    String startMonth = "2017-01";
    String endMonth = "2018-08";

    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject = (JSONObject) jsonParser.parse(SAMPLE_DATA);

    JSONArray jsonArray = (JSONArray) ((JSONObject) jsonObject.get("result")).get("records");

    when(interestRatesService.getData(any(Date.class), any(Date.class))).thenReturn(jsonArray);

    mvc.perform(get(URL_INTEREST_RATES)
        .param("startMonth", startMonth)
        .param("endMonth", endMonth)
        .header(X_REQUESTED_WITH, XMLHTTP_REQUEST)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors").doesNotExist())
        .andExpect(jsonPath("$.data", hasSize(20)));
  }
}