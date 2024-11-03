# 🚀 Hinglish Toxic Comment Detection Project

Detect toxic comments in Hinglish text with high accuracy! Our project leverages state-of-the-art methods for multilingual and code-mixed language processing, combining classic ML techniques with transfer learning to achieve exceptional performance.

---

## 🌟 Project Architecture

Input Texts ➔ Data Cleaning ➔ Character-level TF-IDF ➔ N-grams ➔ Logistic Regression (LR) ➔ Naive Bayes (NB) ➔ mBERT Fine-Tuning ➔ Output (Predictions & Evaluation)
Input Texts: Hinglish, multilingual abusive, and HASOC datasets
Data Cleaning: Text normalization, punctuation removal
Character-level TF-IDF: N-grams from 1 to 6 characters to capture Hinglish nuances
Model Stages:
Logistic Regression & Naive Bayes: Baseline models trained on TF-IDF features
mBERT Fine-Tuning: Transfer learning approach to capture deeper multilingual patterns
Output: Final predictions and performance metrics
⚙️ Methods & Models Used
1. Data Preprocessing
Normalization: Converts text to lowercase, removes unnecessary punctuation.
Tokenization: Character-level n-grams (1-6) using TF-IDF vectorization to capture code-mixed language features.
2. Machine Learning Models
Logistic Regression (LR): A simple yet effective baseline model that works well with TF-IDF text features.
Naive Bayes (NB): Multinomial Naive Bayes classifier, particularly strong for text classification tasks.
3. Transfer Learning with mBERT
mBERT (Multilingual BERT): Fine-tuned for Hinglish toxic comment detection, leveraging pretrained multilingual representations to boost accuracy on Hinglish.
📚 Datasets
HASOC - Hinglish: Comments in Hinglish with toxic and non-toxic labels.
HASOC - English, Hindi, Marathi: Multilingual comments for supplementary support.
IITD - Transliterated: Hinglish dataset focused on abusive content.
Multilingual Abusive Comment Dataset: Diverse dataset with mixed-language abusive and non-abusive comments.
Each dataset captures unique aspects of Hinglish and mixed-language text, providing a robust foundation for training and testing.

🎯 Goals
Achieve 90%+ Accuracy on toxic comment detection in Hinglish.
Evaluate Model Effectiveness: Compare the performance of LR, NB, and mBERT models.
Optimized Text Processing: Use TF-IDF n-grams and fine-tuning to enhance code-mixed language detection.
📈 Results and Evaluation
Final model performance is evaluated on:

Accuracy: Overall percentage of correct predictions.
Precision & Recall: Measure toxic comment detection accuracy per class.
F1-Score: Balanced score for handling class imbalances.
🤝 Team
Member A: Data Preprocessing and Model Setup
Member B: Baseline Model Implementation
Member C: Transfer Learning Fine-Tuning
Member D: Evaluation and Documentation
🔧 Requirements
Python 3.8+
Libraries:
pandas, numpy, scikit-learn, nltk
transformers, torch
Enjoy building Hinglish toxic comment detection with us! For questions or contributions, reach out via email@example.com.

