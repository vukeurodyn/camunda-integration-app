package com.camunda_integration.demo.workers;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ReviewEmailWorker implements CommandLineRunner {

    private static final String REVIEW_TASK_KEY = "ReviewPrefilledForm"; // ✅ single source of truth

    private final JavaMailSender mailSender;
    private final WebClient camunda;
    private final String baseUrl;
    private final String fromEmail;

    public ReviewEmailWorker(
            JavaMailSender mailSender,
            @Value("${camunda.rest.base-url}") String baseUrl,
            @Value("${mail.from:noreply@local.test}") String fromEmail
    ) {
        this.mailSender = mailSender;
        this.baseUrl = baseUrl;
        this.fromEmail = fromEmail;
        this.camunda = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public void run(String... args) {
        System.out.println("Bootstrapping ExternalTaskClient to " + baseUrl);
        try {
            String version = WebClient.create(baseUrl)
                    .get().uri("/version")
                    .retrieve().bodyToMono(String.class).block();
            System.out.println("✅ Camunda REST reachable: " + version);
        } catch (Exception e) {
            System.err.println("❌ Cannot reach Camunda REST: " + e.getMessage());
        }

        ExternalTaskClient.create()
                .baseUrl(baseUrl)
                .workerId("review-email-worker")
                .asyncResponseTimeout(20000)
                .build()
                .subscribe("send-review-email")
                .lockDuration(60000)
                .handler(this::handleTask)
                .open();

        System.out.println("✅ Worker subscribed to topic: send-review-email");
    }

    private void handleTask(ExternalTask task, ExternalTaskService svc) {
        try {
            String processInstanceId = task.getProcessInstanceId();
            System.out.println("📧 Handling email task for process=" + processInstanceId);

            // Wait for task to be created
            Map taskResponse = null;
            for (int i = 0; i < 10 && taskResponse == null; i++) {
                taskResponse = camunda.get()
                        .uri(b -> b.path("/task")
                                .queryParam("processInstanceId", processInstanceId)
                                .queryParam("taskDefinitionKey", REVIEW_TASK_KEY)
                                .build())
                        .retrieve()
                        .bodyToFlux(Map.class)
                        .blockFirst();

                if (taskResponse == null) {
                    System.out.println("⏳ Waiting for ReviewPrefilledForm task...");
                    Thread.sleep(300);
                }
            }

            if (taskResponse == null) {
                System.err.println("❌ Task " + REVIEW_TASK_KEY + " still not found after wait");
                svc.handleFailure(task, "User task not available yet",
                        "Still cannot locate task " + REVIEW_TASK_KEY,
                        3, 2000);
                return;
            }

            // ✅ Get taskId from the REST response, not from external task variables
            String taskId = (String) taskResponse.get("id");
            if (taskId == null || taskId.isBlank()) {
                svc.handleFailure(task, "Task ID not found in response",
                        "taskResponse did not contain 'id' field", 3, 2000);
                return;
            }

            String taskUrl = "http://localhost:9091/camunda/app/tasklist/default/#/?task=" + taskId;
            System.out.println("✅ Found user task: " + taskId);

            // Send email via Mailpit SMTP localhost:1025
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(fromEmail);
            mail.setTo("vuk.topalovic@eurodyn.com");
            mail.setSubject("Review Sensor Data Form");
            mail.setText("Review the data here:\n\n" + taskUrl);

            mailSender.send(mail);
            System.out.println("📨 Email sent via Mailpit → " + taskUrl);

            svc.complete(task);
            System.out.println("✅ External task completed");

        } catch (Exception e) {
            System.err.println("❌ Email worker error: " + e.getMessage());
            e.printStackTrace();  // ✅ Added for better debugging
            svc.handleFailure(task, e.getMessage(), e.toString(), 3, 5000);
        }
    }
}
