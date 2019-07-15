from kanren import Relation, facts

def main():
    parent = Relation()
    facts(parent, ("Darth Vader", "Luke Skywalker"),
                  ("Darth Vader", "Leia Organa"),
                  ("Leia Organa", "Kylo Ren"),
                  ("Han Solo", "Kylo Ren"))
    # Darth Vader is Luke and Leia's Parent
    # Leia and Han are Kylo's Parents

    run(1, x, parent(x, "Luke Skywalker"))
    # What is x, such that x is the parent of Luke
    run(2, y, parent("Darth Vader", y))
    # What is y, such that y is the children of Vader

    grandparent = Relation()
    facts(grandparent, ("Darth Vader", "Kylo Ren"))
    # Darth Vader is Kylo Ren's Grandparent

    run(1, z, grandparent(z, "Kylo Ren"))
    # What is z, such that z is the grandparent of Kylo Ren
    #OR
    y = var()
    run(1, x, parent(x, y),
              parent(y, "Kylo Ren"))
    # What is x, such that x is the parent of y, where y is the parent
    # of Kylo Ren

def logicPy():
    # Create a separate dictionary for each character's tree
    Luke = {
        "Parent": "Darth Vader",
        "Sister": "Leia Organa",
        "Brother-in-Law": "Han Solo",
        "Nephew": "Kylo Ren"
    }

    Leia = {
        "Parent": "Darth Vader",
        "Brother": "Luke Skywalker",
        "Spouse": "Han Solo",
        "Son": "Kylo Ren"
    }

    Vader = {
        "Son": "Luke Skywalker",
        "Daughter": "Leia Organa",
        "Grandson": "Kylo Ren",
        "Son-in-Law": "Han Solo"
    }

    Kylo = {
        "Mother": "Leia Organa",
        "Father": "Han Solo",
        "Grandfather": "Darth Vader",
        "Uncle": "Luke Skywalker"
    }

    print(Luke["Parent"])
    # Print the parent of Luke Skywalker
    print(Vader["Son"] + Vader["Daughter"])
    # Print the son and daughter of Darth Vader
    print(Kylo["Grandparent"])
    # Print the grandfather of Kylo Ren
