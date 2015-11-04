"""
Import sample data for Sentiment Analysis Engine Template
"""

import predictionio
import argparse

def import_events(client, file):
  f = open(file, 'r')
  count = 0
  print "Importing data..."
  for line in f:
    data = line.rstrip('\r\n').split("\t")
    if True:
      client.create_event(
        event="train",
        entity_type="user",
        entity_id=data[0],
        properties= {
          "phrase" : str(data[2]),
          "sentiment" : float(data[3])
        }
      )
    count += 1
    if count % 100 == 0:
      print count

  f.close()
  print "%s events are imported." % count

if __name__ == '__main__':
  parser = argparse.ArgumentParser(
    description="Import sample data for sentiment analysis engine")
  parser.add_argument('--access_key', default='invalid-access-key')
  parser.add_argument('--url', default="http://localhost:7070")
  parser.add_argument('--file', default="./data/train.tsv")

  args = parser.parse_args()
  print args

  client = predictionio.EventClient(
    access_key=args.access_key,
    url=args.url,
    threads=10,
    qsize=1000)
  import_events(client, args.file)
