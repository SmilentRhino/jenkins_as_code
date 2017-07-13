#!/usr/bin/env groovy
import groovy.json.*
node('master'){
    stage("Print") {
        def map1 = [name: 'Gromit', likes: 'cheese', id: 1234]
        def map2 = [
            simple : 123,
            complex: [a: 1, b: 2]
        ]
        if ( map1.containsKey('id')) {
            echo "id not in map1"
        } else {
            echo "map['id'] : ${map1['id']}"
        }
        if ( map1.containsKey('not_exist')) {
            echo "The key is not in map1"
        } else {
            echo "map['not_exist'] : ${map1['not_exist']}"
        }
        echo '${map2["complex"]["a"]}'
        echo "${map2['complex']['a']}"
        echo 'It is fun to use json'
        def jsonSlurper = new JsonSlurper()
        def object = jsonSlurper.parseText('{ "myList": [4, 8, 15, 16, 23, 42] }')
        echo "${object['myList']}"
        def object1 = jsonSlurper.parseText '''
            { "simple": 123,
              "fraction": 123.66,
              "exponential": 123e12
            }'''
        echo "${object1['simple']}"
    }
}
