# Sentiment Analysis Template

Given a sentence, return a score between 0 and 4, indicating the sentence's sentiment. 0 being very negative, 4 being very positive, 2 being neutral.

The engine uses the stanford CoreNLP library and the Scala binding `gangeli/CoreNLP-Scala` for parsing.

## Versions

### v0.1.0

- initial version

## Development Notes

### import sample data

```
$ python data/import_eventserver.py --access_key <your_access_key>
```

### query

normal:

```
$ curl -H "Content-Type: application/json" \
-d '{
  "s" : "I am happy"
  }' \
http://localhost:8000/queries.json \
-w %{time_connect}:%{time_starttransfer}:%{time_total}
```

```
$ curl -H "Content-Type: application/json" \
-d '{
  "s" : "This movie sucks!"
  }' \
http://localhost:8000/queries.json \
-w %{time_connect}:%{time_starttransfer}:%{time_total}
```

