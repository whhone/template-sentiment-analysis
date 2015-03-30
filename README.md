# Sentiment Analysis Template

Given a sentence, return a score between 0 and 4, indicating the sentence's sentiment. 0 being very negative, 4 being very positive, 2 being neutral.

The engine uses the stanford CoreNLP library and the Scala binding `gangeli/CoreNLP-Scala` for parsing.

## Versions

### v0.1.0

- initial version

## import sample data

```
$ python data/import_eventserver.py --access_key <your_access_key> --file data/train.tsv
```

The sample training data comes from https://www.kaggle.com/c/sentiment-analysis-on-movie-reviews. It is a tsv file. Each line contains 4 data, `PhraseId`, `SentenceId`, `Phrase` and `Sentiment`. 

For example,
```
1	1	bad	1
```

## Step to build, train and deploy the engine

```
$ pio build && pio train && pio deploy
```

## Query

The query takes a `String` `s`. The result contains a `Double` called `sentiment`. 

normal:

```
$ curl -H "Content-Type: application/json" \
-d '{
  "s" : "I am happy"
  }' \
http://localhost:8000/queries.json \
-w %{time_connect}:%{time_starttransfer}:%{time_total}

{"sentiment":3.0714285712172384}0.005:0.027:0.027
```

```
$ curl -H "Content-Type: application/json" \
-d '{
  "s" : "This movie sucks!"
  }' \
http://localhost:8000/queries.json \
-w %{time_connect}:%{time_starttransfer}:%{time_total}

{"sentiment":0.8000000001798788}0.005:0.031:0.031
```

