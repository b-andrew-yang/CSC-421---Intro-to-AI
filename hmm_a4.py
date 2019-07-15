#Andrew Yang
#V00878595

import matplotlib.pyplot as plt
from scipy import stats
import numpy as np
from hmmlearn import hmm

def main():
    model_states = hmm.MultinomialHMM(n_components=2)
    model_states.startprob_ = np.array([1.0, 0.0])

    model_states.transmat_ = np.array([[0.7, 0.3],
                                       [0.5, 0.5]])

    model_states.emissionprob_ = np.array([[0.2, 0.1, 0.7],
                                           [0.3, 0.6, 0.1]])

    X, Z = model_states.sample(300)
    print("Original model: ")
    print(Z)

    remodel = hmm.MultinomialHMM(n_components=2, n_iter=300)
    remodel.fit(X)

    predict = remodel.predict(X)

    print("Learned transition matrix")
    print(remodel.transmat_)
    print("Learned emission probabilities")
    print(remodel.emissionprob_)

    print("Learned model: ")
    print(predict)


if __name__ == '__main__':
    main()
