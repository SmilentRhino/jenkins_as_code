#!/usr/bin/env python
'''
Conver yaml to json
'''
import os
import os.path
import logging
import json
import yaml
import click


logging.basicConfig(level=logging.INFO)

@click.command()
@click.option('--yaml-path', default='seed_job/files',
              help='Path of the yaml files')
@click.option('--path-type', default='relative',
              type=click.Choice(['real', 'relative']))
@click.option('--overwrite/--no-overwrite', default=True)
def yaml2json(yaml_path, path_type, overwrite):
    '''
    Covert all yaml file in path yaml_path to json in the same
    dir.
    '''
    if path_type == 'relative':
        workspace = os.environ.get('WORKSPACE', os.getcwd())
        file_path = os.path.join(workspace, yaml_path)
    else:
        file_path = yaml_path
    for entry in os.listdir(file_path):
        if entry.endswith('.yaml'):
            logging.info("Found yaml file %s in %s", entry, file_path)
            yaml_path = os.path.join(file_path, entry)
            json_path = yaml_path[:-5] + '.json'
            if os.path.exists(json_path) and not overwrite:
                logging.warning("%s exists, skip...", json_path)
            else:
                with open(yaml_path, 'r') as yaml_file:
                    yaml_content = yaml.load(yaml_file)
                with open(json_path, 'w') as json_file:
                    json.dump(yaml_content, json_file, indent=4)



if __name__ == '__main__':
    yaml2json()
