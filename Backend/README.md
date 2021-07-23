# SpeedCode Backend

## Guide

### Guide of configure

**TIPS: If you are using SMTP server to send email. Please change `common/email_sender.py` file!**

Example config(`encryption_config.py`):
```python
# To send your email
# Our SMTP service(No ADs): https://send.hedwi.com
EMAIL_API_KEYS = "000-000-000-000"

# Judge0 service url
JUDGE0_API = "http://localhost:2358"

# Access /console apis key
CONSOLE_KEY = "xxxxxxxxxxxx"
```

### Guide of responses

#### Status Codes

| Status   | Code      | Old Code  | Description            |
|----------|-----------|-----------|------------------------|
| OK       | 200       | PASSED    | Request is success     |
| OK       | 204       | UNDFID    | No value for return    |
| Error    | 400       | WRODAT    | Wrong data             |
| Rejected | 401       | WRODAT    | Payload error          |
| Rejected | 403       | REPEAT    | Repeat data            |
| Rejected | 433       | REPEAT    | Forbidden              |
| Error    | 404       | UNDFID    | Cannot found objective |
| Error    | 410       | DATLOT    | Limit Exceeded         |

**TIPS: All status code's type is `string`**

#### Ultra Codes

| With Status   | With Code      | Code  | Group    | Description            |
|---------------|----------------|-------|----------|------------------------|
| Error         | 400            | iu    | Illegal  | Illegal username       |
| Error         | 400            | ie    | Illegal  | Illegal email          |
| OK            | 200            | ag    | Addons   | Request again          |
| Rejected      | 433            | ml    | Equals   | Less / More than       |

**TIPS: All ultra code's type is `string` too**