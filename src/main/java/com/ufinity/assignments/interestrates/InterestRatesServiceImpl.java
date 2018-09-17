package com.ufinity.assignments.interestrates;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;

@Service
public class InterestRatesServiceImpl implements InterestRatesService {
  public static final Logger LOGGER = LoggerFactory.getLogger(InterestRatesServiceImpl.class);
  private static final String PARAM_NAME_BETWEEN_MONTHS = "between[end_of_month]";
  public static final String PARAM_RESOURCE_ID = "resource_id";
  public static final String PARAM_LIMIT = "limit";
  public static final String PARAM_OFFSET = "offset";
  public static final String RESP_KEY_SUCCESS = "success";
  public static final String RESP_KEY_RESULT = "result";
  public static final String RESP_KEY_RECORDS = "records";
  public static final int LIMIT = 100;

  @Value("${interestrates.mas.api.url}")
  String interestRatesUrl;

  @Value("${interestrates.mas.api.resourceId}")
  String resourceId;

  private ExternalSystemApiService externalSystemApiService;

  @Autowired
  public InterestRatesServiceImpl(ExternalSystemApiService externalSystemApiService) {
    this.externalSystemApiService = externalSystemApiService;
  }

  @Override
  public JSONArray getData(Date startMonth, Date endMonth) throws ServiceException {

    int total = monthsBetween(startMonth, endMonth) + 1;
    int offset = 0;
    String startMonthStr = Constants.SIMPLE_DATE_FORMAT.format(startMonth);
    String endMonthStr = Constants.SIMPLE_DATE_FORMAT.format(endMonth);

    JSONArray jsonArray;
    if (total > LIMIT) {
      jsonArray = new JSONArray();
      while (offset < total) {
        JSONArray tmp = getData(startMonthStr, endMonthStr, offset);
        jsonArray.addAll(tmp);
        LOGGER.debug("Array size is {}", jsonArray.size());
        offset += LIMIT;
      }
    } else {
      jsonArray = getData(startMonthStr, endMonthStr, offset);
    }

    return jsonArray;
  }

  private JSONArray getData(String startMonthStr, String endMonthStr, int offset) throws ServiceException {

    URI targetUrl = UriComponentsBuilder.fromUriString(interestRatesUrl)
        .queryParam(PARAM_RESOURCE_ID, resourceId)
        .queryParam(PARAM_NAME_BETWEEN_MONTHS, startMonthStr + "," + endMonthStr)
        .queryParam(PARAM_LIMIT, LIMIT)
        .queryParam(PARAM_OFFSET, offset)
        .build()
        .encode()
        .toUri();
    LOGGER.info("targetUrl is {}", targetUrl);

    String rawData = externalSystemApiService.fetchData(targetUrl);
    LOGGER.info("Data is {}", rawData);

    JSONParser parser = new JSONParser();
    JSONObject jsonObject;
    try {
      Object obj = parser.parse(rawData);
      jsonObject = (JSONObject) obj;
    } catch (Exception e) {
      throw new ServiceException("Error parsing the returned data: " + rawData, e);
    }

    if (null == jsonObject) {
      throw new ServiceException("jsonObject is null after parsing");
    }

    Boolean result = (Boolean) jsonObject.get(RESP_KEY_SUCCESS);
    if (!result) {
      throw new ServiceException("Error processing data in service provider, returned result will be discarded");
    }

    jsonObject = (JSONObject) jsonObject.get(RESP_KEY_RESULT);
    if (null == jsonObject) {
      throw new ServiceException("No result provided, something may goes wrong in service provider side");
    }

    return (JSONArray) jsonObject.get(RESP_KEY_RECORDS);
  }

  private static int monthsBetween(final Date s1, final Date s2) {
    final Calendar d1 = Calendar.getInstance();
    d1.setTime(s1);
    final Calendar d2 = Calendar.getInstance();
    d2.setTime(s2);
    return (d2.get(Calendar.YEAR) - d1.get(Calendar.YEAR)) * 12 + d2.get(Calendar.MONTH) - d1.get(Calendar.MONTH);
  }
}
