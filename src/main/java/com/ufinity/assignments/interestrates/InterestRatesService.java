package com.ufinity.assignments.interestrates;

import org.json.simple.JSONArray;

import java.util.Date;

public interface InterestRatesService {

  /**
   * Return the interest rates information based on the given period.
   *
   * The format will be following:
   *
   * {
   *    "data":[
   *       {
   *          "fc_housing_loans_15y":"3.16",
   *          "end_of_month":"2017-09",
   *          "prime_lending_rate":"5.28",
   *          "fc_savings_deposits":"0.17",
   *          "banks_fixed_deposits_3m":"0.14",
   *          "banks_savings_deposits":"0.16",
   *          "banks_fixed_deposits_6m":"0.20",
   *          "fc_hire_purchase_motor_3y":"5.26",
   *          "fc_fixed_deposits_3m":"0.30",
   *          "fc_fixed_deposits_12m":"0.50",
   *          "fc_fixed_deposits_6m":"0.38",
   *          "banks_fixed_deposits_12m":"0.33",
   *          "timestamp":"1537095931"
   *       }
   *    ]
   * }
   *
   * or
   * {
   *    "errors":[
   *       {
   *          "message:"Required field missing"
   *       }
   *    ]
   * }
   *
   * @param startMonth Starting month
   * @param endMonth Ending month
   * @return Interest rates data or error messages in JSON format
   * @exception ServiceException if data from external party are not in correct format
   *
   */
  JSONArray getData(Date startMonth, Date endMonth) throws ServiceException;

}
