# 🔎 Hinglish Toxic Comment Detection

## 📌 Overview

Toxic comments on social media have become a significant issue, especially in **code-mixed languages** like **Hinglish (Hindi-English mix)**. This project introduces an **ensemble-based machine learning model** that efficiently detects toxic comments while being computationally optimized compared to transformer-based approaches.

## 🛠️ Model Architecture

The proposed model is an **ensemble of three traditional machine learning classifiers**:

1. **Logistic Regression (LR)**
2. **Random Forest (RF)**
3. **Gradient Boosting Classifier (GBC)**

The final predictions are obtained using a **Soft Voting Classifier**, which averages the probabilities from all three models to enhance robustness and generalization. The dataset was preprocessed using **TF-IDF with character-level n-grams (1 to 6)** to capture **transliterations and variations** commonly found in Hinglish text. Additionally, **Random Oversampling** was applied to handle class imbalance and improve the model's ability to correctly classify toxic comments.

## 📊 Dataset

The model is trained and evaluated on the **Multilingual-Abusive-Comment-Detection (nsfw\_train)** dataset, which contains labeled **toxic and non-toxic Hinglish comments**.

- **Preprocessing Techniques Used:**
  - Removal of **special characters, hashtags, URLs, and mentions**
  - Text normalization and **tokenization**
  - Handling **class imbalance** using **Random Oversampling**
  - Feature extraction using **TF-IDF with character-level n-grams (1 to 6)**

🚨 **Note:** The dataset files are **not uploaded in this repository** due to size limitations. Please follow the steps below to set up the dataset folder:

### 📂 **Steps to Set Up the Dataset Folder**

1️⃣ **Create a folder named ************************`dataset/`************************ in the project directory**\
2️⃣ \*\*Download the following datasets and place them inside \*\***`dataset/`**

| **Dataset Name**         | **Download Link**                                                                                                                   |
| ------------------------ | ----------------------------------------------------------------------------------------------------------------------------------- |
| `nsfw_train.csv`         | [Download](https://www.kaggle.com/c/multilingualabusivecomment/data)                                                                |
| `hinglish_data.csv`      | [Download](https://www.kaggle.com/datasets/shivajeetrai/hinglish-data-for-sentiment-analysis)                                       |
| `trac_codemixed.csv`     | [Download](https://github.com/surrey-nlp/woah-aggression-detection/blob/main/data/TRAC/TRAC_codemixed.csv)                          |
| `iitd_offensive.csv`     | [Download](https://github.com/LCS2-IIITD/Hinglish_offense_detection-Neurocomputing2021/tree/main/data/processed/Aggression_dataset) |
| `hinglish_codemixed.csv` | [Download](https://www.kaggle.com/datasets/bajpaipurva/hinglish-code-mixed-dataset)                                                 |

Once the datasets are downloaded and placed in the `dataset/` folder, you can proceed with training and testing the model.

## 🎯 Performance Comparison

Our ensemble-based model achieves **competitive accuracy** compared to **deep learning-based approaches** while being **computationally efficient**.

| **Model**                              | **Accuracy** | **Precision** | **Recall** | **F1-Score** |
| -------------------------------------- | ------------ | ------------- | ---------- | ------------ |
| **MuRIL BERT**                         | 0.872        | 0.878         | 0.844      | 0.856        |
| **XLM-RoBERT**                         | 0.872        | 0.869         | 0.857      | 0.859        |
| **XLM-RoBERT with TcT + Emoji**        | 0.877        | 0.877         | 0.858      | 0.864        |
| **Our Model (Soft Voting Classifier)** | **0.900**    | **0.894**     | **0.923**  | **0.908**    |

### 🔥 **Key Improvements Over Previous Models**

👉 **Computational Efficiency:** No heavy transformer models, making it **deployable on low-resource systems**\
👉 **Feature Engineering:** **Character-level TF-IDF** effectively captures **Hinglish transliterations and variations**\
👉 **Better Class Balance Handling:** **Random Oversampling** ensures a fair representation of toxic comments\
👉 **Improved Generalization:** Ensemble-based learning **reduces overfitting** and enhances **robustness**

## 🛠️ Installation & Setup

1️⃣ **Clone this repository:**

```bash
git clone https://github.com/DevangGentyal/toxic-comment-detection.git
cd toxic-comment-detection
```

2️⃣ **Install dependencies:**

```bash
pip install -r requirements.txt
```

3️⃣ **Run the model:**

```bash
python main.py
```

## 📂 Project Structure

```
📂 toxic-comment-detection/
│── 📂 dataset/               # Folder where datasets should be downloaded (NOT included in repo)
│── 📂 models/                # Trained model files  
│── 📂 src/                   # Source code (preprocessing, training, and evaluation)  
│── 📝 main.py                # Main script to run the model  
│── 📝 requirements.txt        # Dependencies  
│── 📝 README.md               # Project documentation  
```

## 📝 Citation & References

This study was based on extensive research from **20 academic papers**, 10 of which are directly referenced in the project. The most relevant papers include:

- **Bansal et al. (2022):** *A Transformer-Based Approach for Abuse Detection in Code-Mixed Indic Languages*
- **Farooqi et al. (2021):** *Leveraging Transformers for Hate Speech Detection in Conversational Code-Mixed Tweets*
- **Jhaveri et al. (2022):** *Toxicity Detection for Indic Multilingual Social Media Content*

For the full list of references, check the **research paper included in this repository**.

## ✨ Future Scope

💡 **Hybrid Approaches:** Exploring a combination of **traditional ML and transformers**\
💡 **Real-time Deployment:** Implementing this model into a **live API for social media moderation**\
💡 **Expanding to Other Code-Mixed Languages:** Adapting the approach to **Tamil-English, Bengali-English, and other Indic languages**
