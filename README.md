# 🔎 Toxic Hinglish Comment Detection

## 📌 Overview  
Toxic comments on social media have become a significant issue, especially in **code-mixed languages** like **Hinglish (Hindi-English mix)**. This project introduces an **ensemble-based machine learning model** that efficiently detects toxic comments while being computationally optimized compared to transformer-based approaches.

## 📊 Dataset  
The model is trained and evaluated on the **Multilingual-Abusive-Comment-Detection (nsfw_train)** dataset, which contains labeled **toxic and non-toxic Hinglish comments**.  
- **Preprocessing Techniques Used:**
  - Removal of **special characters, hashtags, URLs, and mentions**  
  - Text normalization and **tokenization**  
  - Handling **class imbalance** using **Random Oversampling**  
  - Feature extraction using **TF-IDF with character-level n-grams (1 to 6)**  

## 🏗️ Model Architecture  
The proposed model is an **ensemble of three traditional machine learning classifiers**:
1. **Logistic Regression (LR)**
2. **Random Forest (RF)**
3. **Gradient Boosting Classifier (GBC)**  

The final predictions are obtained using a **Soft Voting Classifier**, which averages the probabilities from all three models to enhance robustness and generalization.

## 🎯 Performance Comparison  
Our ensemble-based model achieves **competitive accuracy** compared to **deep learning-based approaches** while being **computationally efficient**.

| **Model**                          | **Accuracy** | **Precision** | **Recall** | **F1-Score** |
|------------------------------------|-------------|--------------|------------|-------------|
| **MuRIL BERT**                     | 0.872       | 0.878        | 0.844      | 0.856       |
| **XLM-RoBERT**                     | 0.872       | 0.869        | 0.857      | 0.859       |
| **XLM-RoBERT with TcT + Emoji**    | 0.877       | 0.877        | 0.858      | 0.864       |
| **Our Model (Soft Voting Classifier)** | **0.900**   | **0.894**    | **0.923**  | **0.908**   |

### 🔥 **Key Improvements Over Previous Models**  
✅ **Computational Efficiency:** No heavy transformer models, making it **deployable on low-resource systems**  
✅ **Feature Engineering:** **Character-level TF-IDF** effectively captures **Hinglish transliterations and variations**  
✅ **Better Class Balance Handling:** **Random Oversampling** ensures a fair representation of toxic comments  
✅ **Improved Generalization:** Ensemble-based learning **reduces overfitting** and enhances **robustness**  

## 🛠️ Installation & Setup  
1️⃣ **Clone this repository:**  
```bash
git clone https://github.com/DevangGentyal/toxic-comment-detection.git
cd Hinglish-Toxic-Detection
