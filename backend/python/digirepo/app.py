from flask import Flask

app = Flask(__name__)

@app.route("/api/user/save_document")
def save_document():
    return {'success': True}

if __name__ == "__main__":
    app.run()
