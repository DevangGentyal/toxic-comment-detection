import customtkinter as ctk
import joblib

# Load the Saved Model and Vectorizer
model = joblib.load("models/voting_clf_model.pkl")
vectorizer = joblib.load("models/tfidf_vectorizer.pkl")

# Function to Predict Toxicity
def classify_comment():
    comment = text_input.get("1.0", "end").strip()
    if not comment:
        ctk.CTkMessagebox(title="Input Error", message="Please enter some text to analyze.", icon="warning")
        return
    # Transform the input comment and Predict
    comment_tfidf = vectorizer.transform([comment])
    result = model.predict(comment_tfidf)
    result_label.configure(
        text=f"Result: {'Toxic' if result[0] == 1 else 'Not Toxic'}", 
        fg_color=('red' if result[0] == 1 else 'green'), 
        corner_radius=8
    )
    # Display the result
    print(f"\nComment: {comment}")
    print(f"Predicted Label: {'Toxic' if result[0] == 1 else 'Not Toxic'}")

# Initialize the app
ctk.set_appearance_mode("light")
ctk.set_default_color_theme("blue")

app = ctk.CTk()
app.geometry("500x400")
app.title("Toxicity Detector")
app.resizable(False, False)

# Header
header = ctk.CTkLabel(
    app, text="Toxicity Detector", 
    font=("Roboto", 20, "bold"), 
    text_color="#333"
)
header.pack(pady=20)

# Text Input
text_input = ctk.CTkTextbox(app, height=150, width=400, font=("Roboto", 14))
text_input.pack(pady=10)

# Analyze Button
analyze_button = ctk.CTkButton(
    app, text="Analyze", 
    command=classify_comment, 
    font=("Roboto", 14), 
    fg_color="#0078D7", 
    hover_color="#0056a3"
)
analyze_button.pack(pady=20)

# Result Label
result_label = ctk.CTkLabel(
    app, text="", 
    font=("Roboto", 16), 
    text_color="#FFFFFF", 
    fg_color=None
)
result_label.pack(pady=10)

# Footer
footer = ctk.CTkLabel(
    app, text="", 
    font=("Roboto", 12), 
    text_color="#888"
)
footer.pack(side="bottom", pady=10)

# Run the app
app.mainloop()