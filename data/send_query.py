"""
Send sample query to prediction engine
"""

import predictionio
client = predictionio.EngineClient(url="http://localhost:8000")

def test(s):
  print s + ' : ' + str(client.send_query({"s": s})['sentiment'])

test('sad')
test('happy')
test('oh')
test('not')
test('not sad')
test('very sad')
test('very happy')
test('not very sad')
