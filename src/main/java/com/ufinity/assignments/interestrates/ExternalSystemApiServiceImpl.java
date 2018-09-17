package com.ufinity.assignments.interestrates;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
public class ExternalSystemApiServiceImpl implements ExternalSystemApiService {
  public static final Logger LOGGER = LoggerFactory.getLogger(ExternalSystemApiServiceImpl.class);

  @Override
  public String fetchData(URI targetUrl) throws ServiceException {

    HttpGet getMethod = new HttpGet(targetUrl);
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      HttpResponse response = httpClient.execute(getMethod);
      LOGGER.debug("response is {}", response);

      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != 200) {
        throw new ServiceException("Error fetching data with status code: " + statusCode);
      }

      return EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new ServiceException("Error fetching data", e);
    }
  }

}
