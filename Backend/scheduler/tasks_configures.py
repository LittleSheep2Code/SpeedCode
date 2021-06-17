
class scheduler_config(object):
    JOBS = [
        {
            "id": "database_model_cleaner",
            "func": "scheduler.model_tasks:model_cleaner",
            "trigger": "interval",
            "seconds": 10
        }
    ]

    SCHEDULER_API_ENABLED = True

