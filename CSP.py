from constraint import *


def main():
    problem = Problem()

    problem.addVariable("F", range(0, 9))
    problem.addVariable("T", range(0, 9))
    problem.addVariable("U", range(0, 9))
    problem.addVariable("W", range(0, 9))
    problem.addVariable("R", range(0, 9))
    problem.addVariable("O", range(0, 9))

    problem.addConstraint(AllDifferentConstraint())
    problem.addConstraint(lambda O, R: ((O + O) % 10 == R, ["OR"])
    problem.addConstraint(lambda W, O, U: (
        (((2 * W) + ((O + O) / 10)) % 10) == U), ["WOU"])
    problem.addConstraint(lambda T, W, O: (
        ((2 * T) + ((W + W) / 10)) % 10 == O), ["TWO"])
    problem.addConstraint(lambda T, F: ((T + T) / 10 == F), ["TF"])

    problem.getSolution()

if __name__ == '__main__':
    main()
