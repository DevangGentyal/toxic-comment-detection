# 🔥 Hinglish Toxic Comment Detection

Detect toxic comments in Hinglish with precision and innovation! This project leverages both traditional ML techniques and transfer learning models to push the boundaries of multilingual and code-mixed language detection.

---

## 📐 Project Architecture

> **Flow**: Input Text ➔ Data Preprocessing ➔ Character-level TF-IDF (1-6 n-grams) ➔ Baseline Models ➔ mBERT Fine-Tuning ➔ Predictions ➔ Performance Evaluation

- **Data Preprocessing**: Text normalization, cleaning, and tokenization
- **Feature Extraction**: Character-level TF-IDF with 1-6 n-grams for capturing Hinglish nuances
- **Baseline Models**: Logistic Regression and Naive Bayes with TF-IDF features
- **Transfer Learning**: Fine-tuning mBERT for Hinglish toxic comment detection
- **Output**: Final predictions with performance metrics

---

## 🔍 Methods & Models

### 1. **Data Preprocessing**
   - Text is cleaned and normalized (lowercased, punctuation removed).
   - Character-level n-grams (1 to 6) capture code-mixed patterns.

### 2. **Baseline Models**
   - **Logistic Regression (LR)**: Classic linear model for binary classification.
   - **Naive Bayes (NB)**: Effective with TF-IDF features, leveraging prior probabilities.

### 3. **Transfer Learning with mBERT**
   - **mBERT (Multilingual BERT)**: Pre-trained transformer fine-tuned for Hinglish toxic comment detection, providing deeper context.

---

## 📊 Datasets

1. **HASOC - Hinglish**: Main dataset, focused on toxic/non-toxic Hinglish comments.
2. **HASOC - English, Hindi, Marathi**: Additional multilingual comments to enhance Hinglish modeling.
3. **IITD - Transliterated**: Abusive Hinglish content for enriched training data.
4. **Multilingual Abusive Comment Dataset**: Mixed-language toxic and non-toxic comments.

> Each dataset enhances the model’s capacity to understand Hinglish-specific and code-mixed patterns.

---

## 🎯 Objectives

- **High Accuracy**: Target 90%+ accuracy in toxic Hinglish comment detection.
- **Model Comparison**: Evaluate LR, NB, and fine-tuned mBERT performance.
- **Optimize for Code-Mixed Language**: Leverage n-grams, TF-IDF, and mBERT for precision in Hinglish.

---

## 🚀 Implementation Steps

### Stage 1: Setup and Data Preparation
   - **Install Libraries**: `pandas`, `numpy`, `scikit-learn`, `nltk`, `transformers`, `torch`
   - **Data Cleaning**: Normalize text, handle missing values
   - **Feature Extraction**: Generate TF-IDF vectors with character-level n-grams (1-6)

### Stage 2: Baseline Models
   - **Train Logistic Regression and Naive Bayes** on TF-IDF features
   - **Evaluate Baseline Performance**: Measure accuracy, precision, recall, F1-score

### Stage 3: Transfer Learning (mBERT)
   - **Fine-Tune mBERT**: Multilingual BERT trained on Hinglish for deeper understanding
   - **Evaluate Transfer Learning**: Compare mBERT’s performance against baselines

### Stage 4: Final Evaluation & Reporting
   - **Consolidate Results**: Compare LR, NB, and mBERT models
   - **Document Findings**: Report accuracy, precision, recall, and F1-scores

---

## 🧑‍🤝‍🧑 Team Roles

- **Member A**: Data Preparation and Cleaning
- **Member B**: Baseline Model Training
- **Member C**: Transfer Learning Fine-Tuning
- **Member D**: Evaluation and Reporting

---

## 🔧 Requirements

- **Python 3.8+**
- **Libraries**:
  - Data Handling: `pandas`, `numpy`
  - Machine Learning: `scikit-learn`, `nltk`
  - Deep Learning: `transformers`, `torch`

---

Let’s build a robust Hinglish toxic comment detection system! For contributions or inquiries, reach out at [contact@example.com](mailto:contact@example.com).
