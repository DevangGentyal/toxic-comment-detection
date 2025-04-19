from flask import Flask, request, jsonify
import joblib
import traceback

# Initialize Flask app
app = Flask(__name__)

# Load Model and Vectorizer
model = joblib.load("models/voting_clf_model.pkl")
vectorizer = joblib.load("models/tfidf_vectorizer.pkl")

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        message = data.get("message", "")

        # Transform input message
        vectorized = vectorizer.transform([message])

        # Predict
        prediction = model.predict(vectorized)[0]
        print("Prediction: ",prediction)

        # Interpret prediction
        result = "Toxic" if prediction == 1.0 else "Not Toxic"
        print("Result: ",result)
        return jsonify({"result": result})

    except Exception as e:
        traceback.print_exc()
        return jsonify({"error": "Something went wrong", "details": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
