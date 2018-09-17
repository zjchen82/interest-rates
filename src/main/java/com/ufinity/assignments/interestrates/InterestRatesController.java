package com.ufinity.assignments.interestrates;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

import static com.ufinity.assignments.interestrates.Constants.SIMPLE_DATE_FORMAT;

@RestController
public class InterestRatesController {
  private static final Logger LOGGER = LoggerFactory.getLogger(InterestRatesController.class);
  public static final String URL_INTEREST_RATES = "/apis/interest-rates";
  public static final String RESP_KEY_MESSAGE = "message";
  public static final String RESP_KEY_ERRORS = "errors";
  public static final String RESP_KEY_DATA = "data";
  public static final String VALIDATION_ERROR_INVALID_FORMAT_START_MONTH = "Start Month is not in yyyy-MM format";
  public static final String VALIDATION_ERROR_INVALID_FORMAT_END_MONTH = "End Month is not in yyyy-MM format";

  private InterestRatesService interestRatesService;

  @Autowired
  public InterestRatesController(InterestRatesService interestRatesService) {
    this.interestRatesService = interestRatesService;
  }

  @GetMapping(URL_INTEREST_RATES)
  public JSONObject getInterestRates(@RequestParam String startMonth, @RequestParam String endMonth) {

    // validate the input
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject;
    Date startDate = null;
    Date endDate = null;

    // the month format should be in yyyy-MM, which is 7 characters
    if (startMonth == null || startMonth.length() != 7) {
      jsonObject = new JSONObject();
      jsonObject.put(RESP_KEY_MESSAGE, VALIDATION_ERROR_INVALID_FORMAT_START_MONTH);
      jsonArray.add(jsonObject);
    } else {
      try {
        startDate = SIMPLE_DATE_FORMAT.parse(startMonth);
      } catch (ParseException e) {
        jsonObject = new JSONObject();
        jsonObject.put(RESP_KEY_MESSAGE, VALIDATION_ERROR_INVALID_FORMAT_START_MONTH);
        jsonArray.add(jsonObject);
      }
    }

    if (endMonth == null || endMonth.length() != 7) {
      jsonObject = new JSONObject();
      jsonObject.put(RESP_KEY_MESSAGE, VALIDATION_ERROR_INVALID_FORMAT_END_MONTH);
      jsonArray.add(jsonObject);
    } else {
      try {
        endDate = SIMPLE_DATE_FORMAT.parse(endMonth);
      } catch (ParseException e) {
        jsonObject = new JSONObject();
        jsonObject.put(RESP_KEY_MESSAGE, VALIDATION_ERROR_INVALID_FORMAT_END_MONTH);
        jsonArray.add(jsonObject);
      }
    }

    // return errors if any validation error
    if (!jsonArray.isEmpty()) {
      jsonObject = new JSONObject();
      jsonObject.put(RESP_KEY_ERRORS, jsonArray);
      return jsonObject;
    }

    try {
      JSONArray data = interestRatesService.getData(startDate, endDate);
      jsonObject = new JSONObject();
      jsonObject.put(RESP_KEY_DATA, data);
    } catch (ServiceException e) {
      LOGGER.error("Error getting data", e);

      // handle processing exception
      jsonObject = new JSONObject();
      jsonObject.put(RESP_KEY_MESSAGE, e.getMessage());
      jsonArray.add(jsonObject);

      jsonObject = new JSONObject();
      jsonObject.put(RESP_KEY_ERRORS, jsonArray);
      return jsonObject;
    }

    return jsonObject;
  }

}
