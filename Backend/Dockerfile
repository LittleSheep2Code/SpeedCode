FROM python:latest

COPY requirements.txt ./
COPY . .

RUN pip3 install gunicorn gevent
RUN pip3 install -r requirements.txt
RUN chmod +x ./LocalStarter.sh

EXPOSE 20020
CMD ["gunicorn", "application:application", "-c", "./gunicorn.conf.py"]
