package vttp2022.miniproject.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Quote {

    private Double current_price;
    private Double previous_close_price;
    private Double change;
    private Double percent_change;
    private Double total_change;
    private Double total_change_percentage;

    public Double getCurrent_price() {
        return current_price;
    }
    public void setCurrent_price(Double current_price) {
        this.current_price = current_price;
    }
    public Double getPrevious_close_price() {
        return previous_close_price;
    }
    public void setPrevious_close_price(Double previous_close_price) {
        this.previous_close_price = previous_close_price;
    }

    public Double getChange() {
        return change;
    }
    public void setChange(Double change) {
        this.change = change;
    }
    public Double getPercent_change() {
        return percent_change;
    }
    public void setPercent_change(Double percent_change) {
        this.percent_change = percent_change;
    }
    public Double getTotal_change() {
        return total_change;
    }
    public void setTotal_change(Double total_change) {
        this.total_change = total_change;
    }
    public Double getTotal_change_percentage() {
        return total_change_percentage;
    }
    public void setTotal_change_percentage(Double total_change_percentage) {
        this.total_change_percentage = total_change_percentage;
    }

    public static Quote create(String json) throws IOException {
        Quote q = new Quote();
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader r = Json.createReader(is);
            JsonObject o = r.readObject();
            q.current_price = (o.getJsonNumber("c")).doubleValue();
            q.previous_close_price = (o.getJsonNumber("pc")).doubleValue();
            q.change = (o.getJsonNumber("d")).doubleValue();
            q.percent_change = (o.getJsonNumber("dp")).doubleValue();
        }
        return q;
    }
    
}
