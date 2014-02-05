/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.weatherForecast;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ForecastExtractor {

    public Forecast getWeatherFeed(String city) {

        city = city.replaceAll(" ", "_");
        city = city.replaceAll("-", "_");

        String url = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + city
                + "&format=xml&num_of_days=3&key=92xtvz99sth9zk3aspabqtu6";

        JAXBContext jc;
        Forecast forecast = null;
        try {
            jc = JAXBContext.newInstance(Forecast.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            URL urlJaxB = new URL(url);
            forecast = (Forecast) unmarshaller.unmarshal(urlJaxB);
        } catch (JAXBException e) {
            System.out.println("JaxB Exception");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return forecast;
    }

    public String getCurrentForecast(Document doc) {
        StringBuilder sb = new StringBuilder();
        NodeList currentCondition = doc.getElementsByTagName("current_condition");
        for (int i = 0; i < currentCondition.getLength(); i++) {
            Node currentNode = currentCondition.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) currentNode;
                sb.append("#");
                sb.append("Current temp C: ");
                sb.append(element.getElementsByTagName("temp_C").item(0).getTextContent());
                sb.append("#");
                sb.append("Current Wind speed: ");
                sb.append(element.getElementsByTagName("windspeedKmph").item(0).getTextContent());
                sb.append("#");
                sb.append("Current Wind direction: ");
                sb.append(element.getElementsByTagName("winddir16Point").item(0).getTextContent());
                sb.append("#");
                sb.append("Current condition: ");
                sb.append(element.getElementsByTagName("weatherDesc").item(0).getTextContent());
                sb.append("#");
            }
        }
        return sb.toString();
    }

    public String getThreeDayForecast(Document doc) {
        StringBuilder sb = new StringBuilder();
        NodeList weatherForecasts = doc.getElementsByTagName("weather");
        for (int i = 0; i < weatherForecasts.getLength(); i++) {
            Node forecast = weatherForecasts.item(i);
            if (forecast.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) forecast;
                sb.append("#");
                switch (i) {
                case 0:
                    sb.append("Today: ");
                    break;
                case 1:
                    sb.append("Tommorow: ");
                    break;
                default:
                    sb.append("Day after tommorow: ");
                    break;
                }
                sb.append(element.getElementsByTagName("date").item(0).getTextContent());
                sb.append("#");
                sb.append("Max temp C: ");
                sb.append(element.getElementsByTagName("tempMaxC").item(0).getTextContent());
                sb.append("#");
                sb.append("Min temp C: ");
                sb.append(element.getElementsByTagName("tempMinC").item(0).getTextContent());
                sb.append("#");
                sb.append("Wind speed: ");
                sb.append(element.getElementsByTagName("windspeedKmph").item(0).getTextContent());
                sb.append("#");
                sb.append("Wind direction: ");
                sb.append(element.getElementsByTagName("winddir16Point").item(0).getTextContent());
                sb.append("#");
                sb.append("Condition: ");
                sb.append(element.getElementsByTagName("weatherDesc").item(0).getTextContent());
                sb.append("#");
            }
        }
        return sb.toString();
    }

    public Forecast getForecast(String city) {
        return getWeatherFeed(city);
    }
}
