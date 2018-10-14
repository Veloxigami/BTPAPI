import numpy as np 
import tensorflow as tf

def multiply(matrix1, matrix2):
    tf.matmul(matrix1, matrix2)

def matrixgenerator(dimension):
    matrix = np.random.rand(dimension, dimension)
    return matrix
