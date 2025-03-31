package org.uwindsor.comp8547.backend.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uwindsor.comp8547.backend.bean.Plan;
import org.uwindsor.comp8547.backend.bean.PlanDataSet;
import org.uwindsor.comp8547.backend.bean.XfinityPlan;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class PlanDataConfiguration {
    private final Gson gson = new Gson();

    @Bean
    public PlanDataSet configurePlanData() {
        PlanDataSet planData = new PlanDataSet();
        List<Plan> plans = new ArrayList<>();
        planData.setPlans(plans);

        plans.addAll(loadXfinityPlans());
        plans.addAll(loadTekPlans());
        return planData;
    }

    private List<Plan> loadXfinityPlans() {
        try {
            // read file
            Reader reader = new FileReader("src/main/resources/plans/xfinity.json");

            // define generic
            Type planListType = new TypeToken<List<XfinityPlan>>(){}.getType();

            List<XfinityPlan> plans = gson.fromJson(reader, planListType);

            reader.close();
            return plans.stream().map(this::convertPlan).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    private List<Plan> loadTekPlans() {
        try {
            // Load the JSON file
            Reader reader = new FileReader("src/main/resources/plans/teksavvy.json");
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray packages = jsonObject.getAsJsonArray("package plan");

            List<Plan> plans = new ArrayList<>();

            for (JsonElement element : packages) {
                JsonObject obj = element.getAsJsonObject();

                Plan plan = new Plan();

                // Set title as name
                plan.setName(obj.get("title").getAsString());

                // Price
                plan.setPrice(obj.get("price").getAsString()+"/Mon.");

                // Vendor
                plan.setVendor("TekSavvy");

                // Features
                JsonObject features = obj.getAsJsonObject("features");
                plan.setDownload(features.get("download speed").getAsString());
                plan.setUpload(features.get("upload speed").getAsString());
                plan.setAdditionalFeatures(obj.get("description").getAsString());
                plan.setSpecialService(features.get("ideal for").getAsString());

                plans.add(plan);
            }

            reader.close();
            return plans;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // return empty list on error
        }
    }


    private Plan convertPlan(XfinityPlan xfinityPlan) {
        Plan plan = new Plan();
        plan.setName(xfinityPlan.getName());
        plan.setPrice(String.format("$%.2f/Mon.", xfinityPlan.getPrice()));
        plan.setDownload(xfinityPlan.getSpeedTier());
        plan.setUpload(xfinityPlan.getSpeedTier());
        plan.setVendor("Xfinity");
        plan.setAdditionalFeatures(String.join(", ", xfinityPlan.getAdditionalFeatures()));
        return plan;
    }

    public static void main(String[] args) {
        PlanDataConfiguration planDataConfiguration = new PlanDataConfiguration();
        List<Plan> plans = planDataConfiguration.loadXfinityPlans();
        // 打印验证
        for (Plan plan : plans) {
            System.out.println(plan);
        }
    }
}
