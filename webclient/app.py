from flask import *
import requests
from pydantic import BaseModel

app = Flask(__name__)


class Log(BaseModel):
    TAG: str
    message: str
    severity: str
    timestamp: str


class Action(BaseModel):
    statusString: str
    progress: int
    name: str
    status: str
    startTimestamp: str


class MainResponse(BaseModel):
    logs: list[Log]
    actions: list[Action]


@app.route('/')
def index():
    r = requests.get("http://localhost:7070")
    return render_template('index.html', resp=MainResponse(**r.json()))
