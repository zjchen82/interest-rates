package com.ufinity.assignments.interestrates;

import org.json.simple.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InterestRatesServiceImplTest {

  private static final String SAMPLE_RESPONSE_STRING = "{\"success\":true,\"result\":{\"resource_id\":[\"5f2b18a8-0883-4769-a635-879c63d3caac\"],\"limit\":5,\"total\":\"428\",\"records\":[{\"end_of_month\":\"1983-01\",\"prime_lending_rate\":\"9.53\",\"banks_fixed_deposits_3m\":\"6.75\",\"banks_fixed_deposits_6m\":\"6.80\",\"banks_fixed_deposits_12m\":\"7.13\",\"banks_savings_deposits\":\"6.50\",\"fc_hire_purchase_motor_3y\":\"12.67\",\"fc_housing_loans_15y\":\"12.42\",\"fc_fixed_deposits_3m\":\"7.15\",\"fc_fixed_deposits_6m\":\"7.30\",\"fc_fixed_deposits_12m\":\"7.70\",\"fc_savings_deposits\":\"7.21\",\"timestamp\":\"1537097627\"},{\"end_of_month\":\"1983-02\",\"prime_lending_rate\":\"9.25\",\"banks_fixed_deposits_3m\":\"6.40\",\"banks_fixed_deposits_6m\":\"6.70\",\"banks_fixed_deposits_12m\":\"6.93\",\"banks_savings_deposits\":\"6.40\",\"fc_hire_purchase_motor_3y\":\"12.58\",\"fc_housing_loans_15y\":\"12.21\",\"fc_fixed_deposits_3m\":\"6.70\",\"fc_fixed_deposits_6m\":\"7.03\",\"fc_fixed_deposits_12m\":\"7.33\",\"fc_savings_deposits\":\"7.08\",\"timestamp\":\"1537097627\"},{\"end_of_month\":\"1983-03\",\"prime_lending_rate\":\"9.10\",\"banks_fixed_deposits_3m\":\"6.18\",\"banks_fixed_deposits_6m\":\"6.48\",\"banks_fixed_deposits_12m\":\"6.83\",\"banks_savings_deposits\":\"6.30\",\"fc_hire_purchase_motor_3y\":\"12.36\",\"fc_housing_loans_15y\":\"11.97\",\"fc_fixed_deposits_3m\":\"6.48\",\"fc_fixed_deposits_6m\":\"6.78\",\"fc_fixed_deposits_12m\":\"7.18\",\"fc_savings_deposits\":\"7.00\",\"timestamp\":\"1537097627\"},{\"end_of_month\":\"1983-04\",\"prime_lending_rate\":\"9.03\",\"banks_fixed_deposits_3m\":\"6.10\",\"banks_fixed_deposits_6m\":\"6.35\",\"banks_fixed_deposits_12m\":\"6.73\",\"banks_savings_deposits\":\"6.15\",\"fc_hire_purchase_motor_3y\":\"12.19\",\"fc_housing_loans_15y\":\"11.92\",\"fc_fixed_deposits_3m\":\"6.38\",\"fc_fixed_deposits_6m\":\"6.65\",\"fc_fixed_deposits_12m\":\"7.13\",\"fc_savings_deposits\":\"7.00\",\"timestamp\":\"1537097627\"},{\"end_of_month\":\"1983-05\",\"prime_lending_rate\":\"9.03\",\"banks_fixed_deposits_3m\":\"6.10\",\"banks_fixed_deposits_6m\":\"6.35\",\"banks_fixed_deposits_12m\":\"6.73\",\"banks_savings_deposits\":\"6.13\",\"fc_hire_purchase_motor_3y\":\"12.14\",\"fc_housing_loans_15y\":\"11.84\",\"fc_fixed_deposits_3m\":\"6.50\",\"fc_fixed_deposits_6m\":\"6.75\",\"fc_fixed_deposits_12m\":\"7.18\",\"fc_savings_deposits\":\"7.00\",\"timestamp\":\"1537097627\"}]}}";
  private static final String ERROR_RESPONSE_STRING_1 = "{\"success\":false}";
  private static final String ERROR_RESPONSE_STRING_2 = "{\"success\":true}";
  private static final String ERROR_RESPONSE_INVALID_FORMAT = "afsadfasf";

  @InjectMocks
  private InterestRatesServiceImpl interestRatesService;

  @Mock
  private ExternalSystemApiService externalSystemApiService;

  private Date startMonth;
  private Date endMonth;

  @Before
  public void setUp() throws ParseException {
    ReflectionTestUtils.setField(interestRatesService, "interestRatesUrl", "https://eservices.mas.gov.sg/api/action/datastore/search.json");
    ReflectionTestUtils.setField(interestRatesService, "resourceId", "5f2b18a8-0883-4769-a635-879c63d3caac");

    startMonth = Constants.SIMPLE_DATE_FORMAT.parse("2018-01");
    endMonth = Constants.SIMPLE_DATE_FORMAT.parse("2018-08");
  }

  @Test(expected = ServiceException.class)
  public void getData_InvalidJson() throws ServiceException {
    when(externalSystemApiService.fetchData(any(URI.class))).thenReturn(ERROR_RESPONSE_INVALID_FORMAT);

    interestRatesService.getData(startMonth, endMonth);
  }

  @Test(expected = ServiceException.class)
  public void getData_Fail() throws ServiceException {
    when(externalSystemApiService.fetchData(any(URI.class))).thenReturn(ERROR_RESPONSE_STRING_1);

    interestRatesService.getData(startMonth, endMonth);
  }

  @Test(expected = ServiceException.class)
  public void getData_Succeed_WithoutResult() throws ServiceException {
    when(externalSystemApiService.fetchData(any(URI.class))).thenReturn(ERROR_RESPONSE_STRING_2);

    interestRatesService.getData(startMonth, endMonth);
  }

  @Test
  public void getData_Succeed_WithResult_WithRecords() throws ServiceException {
    when(externalSystemApiService.fetchData(any(URI.class))).thenReturn(SAMPLE_RESPONSE_STRING);

    JSONArray jsonArray = interestRatesService.getData(startMonth, endMonth);

    assertEquals(5, jsonArray.size());
  }

  @Test
  public void getData_Succeed_WithResult_WithRecords_LargerThanLimit() throws ServiceException, ParseException {
    when(externalSystemApiService.fetchData(any(URI.class))).thenReturn(SAMPLE_RESPONSE_STRING);

    Date startMonth = Constants.SIMPLE_DATE_FORMAT.parse("2000-01");
    JSONArray jsonArray = interestRatesService.getData(startMonth, endMonth);

    assertEquals(15, jsonArray.size());
  }

}