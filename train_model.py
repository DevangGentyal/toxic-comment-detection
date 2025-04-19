import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier, VotingClassifier
from sklearn.metrics import accuracy_score, f1_score, precision_score, recall_score
from imblearn.over_sampling import RandomOverSampler
import joblib

# Load Dataset
file_path = "D:/Coding/Projects/toxic-comment-detection/dataset/nsfw_train.csv"
data = pd.read_csv(file_path, on_bad_lines='skip', low_memory=False)
data = data[['commentText', 'label']].dropna()

#Reduce size for faster computation
data = data.sample(frac=0.1, random_state=42)  # Use 10% of Dataset

# Address Class Imbalance
oversampler = RandomOverSampler(random_state=42)
X_resampled, y_resampled = oversampler.fit_resample(data[['commentText']], data['label'])
X_resampled = X_resampled['commentText']

# Train-Test Split
X_train, X_val, y_train, y_val = train_test_split(
    X_resampled, y_resampled, test_size=0.2, random_state=42, stratify=y_resampled
)

# TF-IDF Feature Extraction
vectorizer = TfidfVectorizer(analyzer='char', ngram_range=(1, 6), max_features=5000)
X_train_tfidf = vectorizer.fit_transform(X_train)
X_val_tfidf = vectorizer.transform(X_val)

# Define Models
lr = LogisticRegression(max_iter=200, solver='saga', C=1.0)
rf = RandomForestClassifier(n_estimators=100, random_state=42)
gb = GradientBoostingClassifier(n_estimators=50, random_state=42)

# Ensemble Model: Voting Classifier
voting_clf = VotingClassifier(estimators=[('lr', lr), ('rf', rf), ('gb', gb)], voting='soft')

# Train the Model
voting_clf.fit(X_train_tfidf, y_train)

# Evaluate the Model
y_pred = voting_clf.predict(X_val_tfidf)
print("Accuracy:", accuracy_score(y_val, y_pred))
print("Precision:", precision_score(y_val, y_pred, average='weighted'))
print("Recall:", recall_score(y_val, y_pred, average='weighted'))
print("F1-Score:", f1_score(y_val, y_pred, average='weighted'))

# Save the Model and Vectorizer
joblib.dump(voting_clf, "models/voting_clf_model.pkl")
joblib.dump(vectorizer, "models/tfidf_vectorizer.pkl")
print("Model and vectorizer saved!")
