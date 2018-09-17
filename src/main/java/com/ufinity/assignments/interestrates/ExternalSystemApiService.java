package com.ufinity.assignments.interestrates;

import java.net.URI;

public interface ExternalSystemApiService {

  /**
   * Return the interest rates information based on the given period.
   *
   * Refer to https://secure.mas.gov.sg/api/APIDESCPAGE.ASPX?RESOURCE_ID=5f2b18a8-0883-4769-a635-879c63d3caac
   * for the data format.
   *
   *
   * @param targetUrl endpoint to retrieve the data
   * @return Interest rates data or error in JSON format
   * @throws ServiceException if request is not OK (status 200)
   */
  String fetchData(URI targetUrl) throws ServiceException;

}
