FROM python:3.9

WORKDIR /code
COPY ./requirements.txt /code/requirements.txt
RUN pip3 install --upgrade -r /code/requirements.txt -v
COPY ./ /code/app

EXPOSE 7003

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "7003"]