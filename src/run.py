#!python3
import os
import json
from code_gen import CodeGen

if __name__ == '__main__':
    path = os.path.join('../resources', 'config.json')
    config = json.loads(open(path, 'r').read())
    codeGen = CodeGen(config['tables'])
    codeGen.gen_code()
