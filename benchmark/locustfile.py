from locust import HttpUser, task

class BlockingChassis(HttpUser):
    @task
    def list_chassis(self):
        self.client.get("/api/v1/chassis")