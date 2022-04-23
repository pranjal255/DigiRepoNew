from flask import Flask
from flask import request, redirect, render_template, send_file
from collections import defaultdict
import os
import json
from datetime import datetime
import time

app = Flask(__name__)

def build_success_response(data):
    return {'success': True, 'data': data}

def build_error_response(error):
    return {'success': False, 'error': error}

@app.route('/api/user/report/metadata/<user_phone>/<lab_id>', methods=['GET'])
def get_user_reports_metadata(user_phone, lab_id):
    upload_folder = f'uploads/metadata/{user_phone}/{lab_id}/'

    reports = []
    report_names = os.listdir(upload_folder)
    for report_name in report_names:
        report = open(f'{upload_folder}{report_name}', 'r')
        reports.append(json.loads(report.read()))
        report.close()

    return build_success_response({'reports': reports})

@app.route('/api/user/report/<user_phone>/<lab_id>/<report_name>', methods=['GET'])
def get_user_report(user_phone, lab_id, report_name):
    user_phone = request.view_args['user_phone']
    lab_id = request.view_args['lab_id']
    report_name = request.view_args['report_name']
    file_name = f'uploads/{user_phone}/{lab_id}/{report_name}'
    return send_file(file_name, as_attachment=True)

@app.route("/", methods=['GET', 'POST'])
def render():
    if request.method == "POST":
        user_phone = request.form['user_phone']
        lab_id = request.form['lab_id']
        f = request.files['file']

        upload_folder = f'uploads/{user_phone}/{lab_id}/'
        if not os.path.exists(upload_folder):
            os.makedirs(upload_folder, exist_ok=True)
        all_file_names = os.listdir(upload_folder)
        if f.filename in all_file_names:
            print('Rejecting upload as file with same name exists')
            return render_template("upload.html")

        file_path = f'uploads/{user_phone}/{lab_id}/{f.filename}'
        f.save(file_path)

        metadata_folder = f'uploads/metadata/{user_phone}/{lab_id}/'
        if not os.path.exists(metadata_folder):
            os.makedirs(metadata_folder, exist_ok=True)
        metadata_file = open(f'{metadata_folder}{f.filename}', 'w')
        metadata_file.write(json.dumps({
            'issue_date': time.mktime(datetime.now().timetuple()),
            'report_name': f.filename,
            'user_phone': user_phone,
            'lab_id': lab_id
        }))
        metadata_file.close()

        print(f'Upload successful. File name: {f.filename}, User phone: {user_phone}, Lab ID: {lab_id}')

    return render_template("upload.html")

if __name__ == "__main__":
    app.run()
