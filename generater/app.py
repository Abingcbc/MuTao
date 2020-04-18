from flask import Flask, request
from generate import Generator
from plan import Planner

app = Flask(__name__)


@app.route('/')
def generate():
    text = request.args['keywords']
    planner = Planner()
    generator = Generator()
    keywords = planner.plan(text)
    # print("Keywords: " + ' '.join(keywords))
    poem = generator.generate(keywords)
    # print("Poem generated:")
    # for sentence in poem:
    #     print(sentence)
    return '\n'.join(poem)

if __name__ == '__main__':
    app.run()
