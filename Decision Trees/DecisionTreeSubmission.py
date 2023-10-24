import numpy as np
from collections import Counter
import time


class DecisionNode:
    """Class to represent a single node in a decision tree."""

    def __init__(self, left, right, decision_function, class_label=None):
        """Create a decision function to select between left and right nodes.
        Note: In this representation 'True' values for a decision take us to
        the left. This is arbitrary but is important for this assignment.
        Args:
            left (DecisionNode): left child node.
            right (DecisionNode): right child node.
            decision_function (func): function to decide left or right node.
            class_label (int): label for leaf node. Default is None.
        """

        self.left = left
        self.right = right
        self.decision_function = decision_function
        self.class_label = class_label

    def decide(self, feature):
        # """Get a child node based on the decision function.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        # Args:͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #     feature (list(int)): vector for feature.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        # Return:͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #     Class label if a leaf node, otherwise a child node.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        # """͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        """Determine recursively the class of an input array by testing a value
           against a feature's attributes values based on the decision function.

        Args:
            feature: (numpy array(value)): input vector for sample.

        Returns:
            Class label if a leaf node, otherwise a child node.
        """

        if self.class_label is not None:
            return self.class_label

        elif self.decision_function(feature):
            return self.left.decide(feature)

        else:
            return self.right.decide(feature)


def load_csv(data_file_path, class_index=-1):
    """Load csv data in a numpy array.
    Args:
        data_file_path (str): path to data file.
        class_index (int): slice output by index.
    Returns:
        features, classes as numpy arrays if class_index is specified,
            otherwise all as nump array.
    """

    handle = open(data_file_path, 'r')
    contents = handle.read()
    handle.close()
    rows = contents.split('\n')
    out = np.array([[float(i) for i in r.split(',')] for r in rows if r])

    if(class_index == -1):
        classes= out[:,class_index]
        features = out[:,:class_index]
        return features, classes
    elif(class_index == 0):
        classes= out[:, class_index]
        features = out[:, 1:]
        return features, classes

    else:
        return out


def build_decision_tree():
    """Create a decision tree capable of handling the sample data.
    Tree is built fully starting from the root.

    Returns:
        The root node of the decision tree.
    """


    func0 = lambda feature : feature[0] == 1 #a1
    func1 = lambda feature : feature[1] == 1 #a2
    func2 = lambda feature: feature[2] == 1 #a3
    func3 = lambda feature: feature[3] == 1 #a4

    decision_tree_root = DecisionNode(None, None, func0)
    decision_tree_root.left = DecisionNode(None, None, None, 1)
    decision_tree_root.right = DecisionNode(None, None, func3)
    decision_tree_root.right.left = DecisionNode(None, None, func1)
    decision_tree_root.right.right = DecisionNode(None, None, func2)
    decision_tree_root.right.right.left= DecisionNode(None, None, None, 0)
    decision_tree_root.right.right.right = DecisionNode(None, None, None, 1)  
    decision_tree_root.right.left.left = DecisionNode(None, None, None, 0)
    decision_tree_root.right.left.right = DecisionNode(None, None, None, 1)


    # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
    #raise NotImplemented()

    return decision_tree_root


def confusion_matrix(classifier_output, true_labels):
    """Create a confusion matrix to measure classifier performance.

    Classifier output vs true labels, which is equal to:
    Predicted  vs  Actual Values.

    Output will in the format:

                        |Predicted|
    |T|                
    |R|    [[true_positive, false_negative],
    |U|    [false_positive, true_negative]]
    |E|

    Args:
        classifier_output (list(int)): output from classifier.
        true_labels: (list(int): correct classified labels.
    Returns:
        A two dimensional array representing the confusion matrix.
    """
    
    countTP = 0
    countFN = 0
    countFP = 0
    countTN = 0
    for i in range(len(classifier_output)):
        if (classifier_output[i]) == 1:
            if (true_labels[i] == 1):
                countTP += 1
            if (true_labels[i] == 0):
                countFP += 1
        if (classifier_output[i] == 0):
            if (true_labels[i] == 0):
                countTN += 1
            if (true_labels[i] == 1):
                countFN += 1
    confusion_matrix = np.array([[countTP, countFN],  [countFP, countTN]])
    return confusion_matrix




    # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
    #raise NotImplemented()


def precision(classifier_output, true_labels):
    """Get the precision of a classifier compared to the correct values.
    Precision is measured as:
        true_positive/ (true_positive + false_positive)
    Args:
        classifier_output (list(int)): output from classifier.
        true_labels: (list(int): correct classified labels.
    Returns:
        The precision of the classifier output.
    """

    countTP = 0
    countFN = 0
    countFP = 0
    countTN = 0
    for i in range(len(classifier_output)):
        if (classifier_output[i]) == 1:
            if (true_labels[i] == 1):
                countTP += 1
            if (true_labels[i] == 0):
                countFP += 1
        if (classifier_output[i] == 0):
            if (true_labels[i] == 0):
                countTN += 1
            if (true_labels[i] == 1):
                countFN += 1
    confusion_matrix = np.array([[countTP, countFN],  [countFP, countTN]])
    precision = (countTP) / (countTP + countFP)
    return precision

    # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
    #raise NotImplemented()


def recall(classifier_output, true_labels):
    """Get the recall of a classifier compared to the correct values.
    Recall is measured as:
        true_positive/ (true_positive + false_negative)
    Args:
        classifier_output (list(int)): output from classifier.
        true_labels: (list(int): correct classified labels.
    Returns:
        The recall of the classifier output.
    """

    countTP = 0
    countFN = 0
    countFP = 0
    countTN = 0
    for i in range(len(classifier_output)):
        if (classifier_output[i]) == 1:
            if (true_labels[i] == 1):
                countTP += 1
            if (true_labels[i] == 0):
                countFP += 1
        if (classifier_output[i] == 0):
            if (true_labels[i] == 0):
                countTN += 1
            if (true_labels[i] == 1):
                countFN += 1
    recall = (countTP) / (countTP + countFN)
    return recall

    # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
    #raise NotImplemented()


def accuracy(classifier_output, true_labels):
    """Get the accuracy of a classifier compared to the correct values.
    Accuracy is measured as:
        correct_classifications / total_number_examples
    Args:
        classifier_output (list(int)): output from classifier.
        true_labels: (list(int): correct classified labels.
    Returns:
        The accuracy of the classifier output.
    """

    countTP = 0
    countFN = 0
    countFP = 0
    countTN = 0
    for i in range(len(classifier_output)):
        if (classifier_output[i]) == 1:
            if (true_labels[i] == 1):
                countTP += 1
            if (true_labels[i] == 0):
                countFP += 1
        if (classifier_output[i] == 0):
            if (true_labels[i] == 0):
                countTN += 1
            if (true_labels[i] == 1):
                countFN += 1
    accuracy = (countTP + countTN) / (countTP + countTN + countFP + countFN)

    return accuracy

    # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
    #raise NotImplemented()


def gini_impurity(class_vector):
    """Compute the gini impurity for a list of classes.
    This is a measure of how often a randomly chosen element
    drawn from the class_vector would be incorrectly labeled
    if it was randomly labeled according to the distribution
    of the labels in the class_vector.
    It reaches its minimum at zero when all elements of class_vector
    belong to the same class.
    Args:
        class_vector (list(int)): Vector of classes given as 0 or 1.
    Returns:
        Floating point number representing the gini impurity.
    """
    
    prob1 = np.mean(class_vector) #prob of being 1
    prob0 = 1 - prob1 #prob of being 0

    gini_imp = prob0*(1-prob0) + prob1*(1-prob1)


    return gini_imp



    #raise NotImplemented()


def gini_gain(previous_classes, current_classes):
    """Compute the gini impurity gain between the previous and current classes.
    Args:
        previous_classes (list(int)): Vector of classes given as 0 or 1.
        current_classes (list(list(int): A list of lists where each list has
            0 and 1 values).
    Returns:
        Floating point number representing the information gain.
    """
 
    parent = gini_impurity(previous_classes)
    current = 0
    for i in (range(len(current_classes))):
        current += gini_impurity(current_classes[i])
    current = current / len(current_classes)
    return parent - current


    #raise NotImplemented()


class DecisionTree:
    """Class for automatic tree-building and classification."""

    def __init__(self, depth_limit= 10):
        """Create a decision tree with a set depth limit.
        Starts with an empty root.
        Args:
            depth_limit (float): The maximum depth to build the tree.
        """

        self.root = None
        self.depth_limit = depth_limit

    def fit(self, features, classes):
        """Build the tree from root using __build_tree__().
        Args:
            features (m x n): m examples with n features.
            classes (m x 1): Array of Classes.
        """
        

        self.root = self.__build_tree__(features, classes)

    def __build_tree__(self, features, classes, depth=0):
        """Build tree that automatically finds the decision functions.
        Args:
            features (m x n): m examples with n features.
            classes (m x 1): Array of Classes.
            depth (int): depth to build tree to.
        Returns:
            Root node of decision tree.
        """

        # base cases 
        # iterate thru columns of features and see if each one is equal, if they are all equal you can
        #break and retunr the node with that label, if they are not equal you go to the next iteration

        #print(classes[0])
        #print([classes[:]])

        #print(depth)

        print("features: ")
        #print(features)
        print(features)
        print(features.shape)
        if len(np.unique(classes)) == 1:
            print("base 1")
            return DecisionNode(None, None, None, classes[0])

        #print("length of features:")
        #print(len(features))
        print("classes:")
        print(classes)
        """
        
        if len(features) == 0:
            print("features is 0")
            return DecisionNode(None, None, None, np.bincount(features).argmax())
        
        if len(features) == 1:
            print("base 2")
            print(classes[0])
            return DecisionNode(None, None, None, classes[0])
        
        if len(classes) == 1:
            print("base 3")
            return DecisionNode(None, None, None, classes[0])
        
        if depth >= self.depth_limit:
            print("base 4")
            return DecisionNode(None, None, None, np.bincount(features).argmax())
        """

        #print("BASE CASE")
        if depth < self.depth_limit:
            if len(np.unique(classes)) == 1:
                print("base 1")
                return DecisionNode(None, None, None, classes[0])
            if len(features) == 1:
                print("base 2")
                print(classes[0])
                return DecisionNode(None, None, None, classes[0])
            if len(classes) == 1:
                print("base 3")
                return DecisionNode(None, None, None, classes[0])

            gains = np.zeros(features.shape[1])
            for col in range(features.shape[1]):
                threshold = np.mean(features[:, col])
                left_classes = classes[classes <= threshold]
                right_classes = classes[classes > threshold]
                gains[col] = gini_gain(classes, [left_classes, right_classes])
            #print("FOR LOOP done")
            best_col = np.argmax(gains)
            best_mean = np.mean(features[:,best_col])

            left_index = features[:,best_col] <= best_mean
            right_index = features[:,best_col] > best_mean
            left_features = features[left_index, :]
            right_features = features[right_index, :]
            best_lc = classes[left_index]
            best_gc = classes[right_index]

            func = lambda feature: feature[best_col] <= best_mean
        
            right_node = self.__build_tree__(right_features, best_gc, depth + 1)
            left_node = self.__build_tree__(left_features, best_lc, depth + 1)
            print("right done")
        else:
            print("base 4")
            print(features)
            #print(features.shape)
            classes2 = []
            for i in range(len(classes)):
                classes2.append(int(classes[i]))
            #print(classes)
            #print(classes2)
            return DecisionNode(None, None, None, np.bincount(classes2).argmax())
        return DecisionNode(left_node, right_node, func)
    
        



        # old code
        """
        print("depth: ")
        print(depth)

        print("features: ")
        #print(features)
        #print(features.shape)
        print("classes: ")
        #print(classes)

        if len(classes) < 100:
            np.bincount(features).argmax()

        if len(classes) == 0 or len(features) == 0:
            print("base case 0")
            return None

        #if np.all(classes[0] == classes[:]):
            #return DecisionNode(None, None, None, classes[0])

        if len(np.unique(classes)) == 1:
            return DecisionNode(None, None, None, classes[0])

        
        if len(features) == 1 or len(classes) == 1:
            print("base case 1")
            return DecisionNode(None, None, None, classes[0])

            # np.all(featurers[0] == features[:])

        if depth >= self.depth_limit:
            #print("base case D")
            return DecisionNode(None, None, None, np.bincount(features).argmax())
            


    
        maxGain = 0
        tempGain = 0
        best_col = None
        best_mean = None
        for col in range(features.shape[1]):
            print("col")
            #print(col)
            print("col contents:")
            #print(features[:,col])
            threshold = np.mean(features[:, col])
            print("mean: = ")
            #print(threshold)
            #if (threshold >= 1 or threshold < 0):
                #continue
            mask = classes <= threshold
            mask1 = classes > threshold 
            less_classes = classes[mask]
            print("less_classes:")
            #print(less_classes)
            greater_classes = classes[mask1]
            print("greater_classes")
            #print(greater_classes)
            print("tempGain:")
            tempGain = gini_gain(classes, [less_classes, greater_classes])
            print(tempGain)
            #if tempGain <= 0:
                #return DecisionNode(None, None, None, np.bincount(features).argmax())
            if tempGain > maxGain:
                maxGain = tempGain
                best_col = col
                best_mean = threshold
        if best_col == None or best_mean == None:
            return DecisionNode(None, None, None, np.bincount(features).argmax())
        left_index = features[:,best_col] <= best_mean
        print("left index:")
        #print(left_index)
        right_index = features[:,best_col] > best_mean
        print("right index:")
        #print(right_index)
        left_features = features[left_index, :]
        print("left features:")
        #print(left_features)
        right_features = features[right_index, :]
        print("right_features:")
        #print(right_features)
        best_lc = classes[left_index]
        print("best_lc:")
        #print(best_lc)
        best_gc = classes[right_index]
        print("best_gc")
        #print(best_gc)
        
        func = lambda feature: feature[best_col] <= best_mean

       
        #print(decision_tree_root)
        
        left = self.__build_tree__(left_features, best_lc, depth + 1)
        print("left done")
        right = self.__build_tree__(right_features, best_gc, depth + 1)
        return DecisionNode(left, right, func)
      
        """

        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()

    def classify(self, features):
        """Use the fitted tree to classify a list of example features.
        Args:
            features (m x n): m examples with n features.
        Return:
            A list of class labels.
        """
        class_labels = []
        for element in features:
            class_labels.append(self.root.decide(element))
        return class_labels

        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()



class RandomForest:
    """Random forest classification."""

    def __init__(self, num_trees, depth_limit, example_subsample_rate,
                 attr_subsample_rate):
        """Create a random forest.
         Args:
             num_trees (int): fixed number of trees.
             depth_limit (int): max depth limit of tree.
             example_subsample_rate (float): percentage of example samples.
             attr_subsample_rate (float): percentage of attribute samples.
        """

        self.trees = []
        self.num_trees = num_trees
        self.depth_limit = depth_limit
        self.example_subsample_rate = example_subsample_rate
        self.attr_subsample_rate = attr_subsample_rate

    def fit(self, features, classes):
        """Build a random forest of decision trees using Bootstrap Aggregation.
            features (m x n): m examples with n features.
            classes (m x 1): Array of Classes.
        """
        subsamples = np.zeros((int(self.example_subsample_rate * len(features)),int(self.attr_subsample_rate * (features.shape[1] - 1))))
        for num in range(self.num_trees):
            randExamples = np.random.choice(len(features), int(self.example_subsample_rate * len(features)), replace = True)
            #print(randExamples)
            subsample = features[randExamples]
            #print(subsample)
            subsampleClass = classes[randExamples]       
            randAttributes = np.random.choice(features.shape[1], int(self.attr_subsample_rate * (features.shape[1] - 1)), replace = False)
            #print(randAttributes)
            #print(int(self.attr_subsample_rate * (features.shape[1] - 1)))
            subsample = subsample[:,randAttributes]
            #for i in range(len(subsamples)):
                #for j in range(subsamples.shape[1]):
                    #subsamples[i][j] = features[i][j]
            #print(subsamples)
            print("subsample:")
            print(subsample)
            print(subsampleClass)
            tree = DecisionTree(depth_limit = 10)
            tree.__build_tree__(subsample, subsampleClass, depth = 0)
            self.trees.append(tree)

        
        

        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()

    def classify(self, features):
        """Classify a list of features based on the trained random forest.
        Args:
            features (m x n): m examples with n features.
        """

        class_labels = []
        class_majority = []
        for tree in self.trees:
            print(tree)
            #class_labels.append(tree.root.decide(element))
        #for item in class_labels:
            #class_majority.append(np.bincount(class_labels).argmax())

        return np.bincount(class_majority).argmax()

        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()


class Vectorization:
    """Vectorization preparation for Assignment 5."""

    def __init__(self):
        pass

    def non_vectorized_loops(self, data):
        """Element wise array arithmetic with loops.
        This function takes one matrix, multiplies by itself and then adds to
        itself.
        Args:
            data: data to be added to array.
        Returns:
            Numpy array of data.
        """

        non_vectorized = np.zeros(data.shape)
        for row in range(data.shape[0]):
            for col in range(data.shape[1]):
                non_vectorized[row][col] = (data[row][col] * data[row][col] +
                                            data[row][col])
        return non_vectorized

    def vectorized_loops(self, data):
        """Element wise array arithmetic using vectorization.
        This function takes one matrix, multiplies by itself and then adds to
        itself.
        Args:
            data: data to be sliced and summed.
        Returns:
            Numpy array of data.
        """

        b = np.array(data)
        c = np.multiply(b,b)
        d = np.add(b,c)

        return d


        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()

    def non_vectorized_slice(self, data):
        """Find row with max sum using loops.
        This function searches through the first 100 rows, looking for the row
        with the max sum. (ie, add all the values in that row together).
        Args:
            data: data to be added to array.
        Returns:
            Tuple (Max row sum, index of row with max sum)
        """

        max_sum = 0
        max_sum_index = 0
        for row in range(100):
            temp_sum = 0
            for col in range(data.shape[1]):
                temp_sum += data[row][col]

            if temp_sum > max_sum:
                max_sum = temp_sum
                max_sum_index = row

        return max_sum, max_sum_index

    def vectorized_slice(self, data):
        """Find row with max sum using vectorization.
        This function searches through the first 100 rows, looking for the row
        with the max sum. (ie, add all the values in that row together).
        Args:
            data: data to be sliced and summed.
        Returns:
            Tuple (Max row sum, index of row with max sum)
        """

        a = np.array(data)
        max = a[0:100].sum(axis=1).max()
        index = a[0:100].sum(axis = 1).argmax()

        return max, index


        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()

    def non_vectorized_flatten(self, data):
        """Display occurrences of positive numbers using loops.
         Flattens down data into a 1d array, then creates a dictionary of how
         often a positive number appears in the data and displays that value.
         ie, [(1203,3)] = integer 1203 appeared 3 times in data.
         Args:
            data: data to be added to array.
        Returns:
            List of occurrences [(integer, number of occurrences), ...]
        """

        unique_dict = {}
        flattened = np.hstack(data)
        for item in range(len(flattened)):
            if flattened[item] > 0:
                if flattened[item] in unique_dict:
                    unique_dict[flattened[item]] += 1
                else:
                    unique_dict[flattened[item]] = 1

        return unique_dict.items()

    def vectorized_flatten(self, data):
        """Display occurrences of positive numbers using vectorization.
         Flattens down data into a 1d array, then creates a dictionary of how
         often a positive number appears in the data and displays that value.
         ie, [(1203,3)] = integer 1203 appeared 3 times in data.
         Args:
            data: data to be added to array.
        Returns:
            List of occurrences [(integer, number of occurrences), ...]
        """
        
        
        a = np.array(data)
        a = a.flatten()
        positive_nums = a[a > 0]
        unique, occurence_count = np.unique(positive_nums, return_counts = True)
        unique_dict = dict(zip(unique,occurence_count))
        
    

        

        """
        a = np.array(data)
        # as type
        a = a[a>0]
        d = a.astype(int)
        b = np.bincount(a.astype(int).flatten()) 
        #c = np.arange((a.shape))
        for item in range(a.shape()[1]):
            unique_dict[a[item]] = b[item]
        
       """
        return unique_dict.items()



        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()
    
    
    def non_vectorized_glue(self, data, vector, dimension='c'):
        """Element wise array arithmetic with loops.
        This function takes a multi-dimensional array and a vector, and then combines
        both of them into a new multi-dimensional array. It must be capable of handling
        both column and row-wise additions.
        Args:
            data: multi-dimensional array.
            vector: either column or row vector
            dimension: either c for column or r for row
        Returns:
            Numpy array of data.
        """
        if dimension == 'c' and len(vector) == data.shape[0]:
            non_vectorized = np.ones((data.shape[0],data.shape[1]+1), dtype=float)
            non_vectorized[:, -1] *= vector
        elif dimension == 'r' and len(vector) == data.shape[1]:
            non_vectorized = np.ones((data.shape[0]+1,data.shape[1]), dtype=float)
            non_vectorized[-1, :] *= vector
        else:
            raise ValueError('This parameter must be either c for column or r for row')
        for row in range(data.shape[0]):
            for col in range(data.shape[1]):
                non_vectorized[row, col] = data[row, col]
        return non_vectorized

    def vectorized_glue(self, data, vector, dimension='c'):
        """Array arithmetic without loops.
        This function takes a multi-dimensional array and a vector, and then combines
        both of them into a new multi-dimensional array. It must be capable of handling
        both column and row-wise additions.
        Args:
            data: multi-dimensional array.
            vector: either column or row vector
            dimension: either c for column or r for row
        Returns:
            Numpy array of data.
            
        """

        
        if dimension == 'c' and len(vector) == data.shape[0]:
            b = np.hstack((data, vector.reshape(-1,1)))
        elif dimension == 'r' and len(vector) == data.shape[1]:
            b = np.vstack((data, vector))  
        else:
            raise ValueError('This parameter must be either c for column or r for row')
        
        
        return b

        
        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()

    def non_vectorized_mask(self, data, threshold):
        """Element wise array evaluation with loops.
        This function takes a multi-dimensional array and then populates a new
        multi-dimensional array. If the value in data is below threshold it
        will be squared.
        Args:
            data: multi-dimensional array.
            threshold: evaluation value for the array if a value is below it, it is squared
        Returns:
            Numpy array of data.
        """
        non_vectorized = np.zeros_like(data, dtype=float)
        for row in range(data.shape[0]):
            for col in range(data.shape[1]):
                val = data[row, col]
                if val >= threshold:
                    non_vectorized[row, col] = val
                    continue
                non_vectorized[row, col] = val**2

        #return non_vectorized

    def vectorized_mask(self, data, threshold):
        """Array evaluation without loops.
        This function takes a multi-dimensional array and then populates a new
        multi-dimensional array. If the value in data is below threshold it
        will be squared. You are required to use a binary mask for this problem
        Args:
            data: multi-dimensional array.
            threshold: evaluation value for the array if a value is below it, it is squared
        Returns:
            Numpy array of data.
        """
     
        
        mask = data < threshold 
        b = np.multiply(data, data)
        c = np.where(mask, b, data)
        #c = np.where(mask, b, a)
        #print(b)
        #print(c)
        #print(data)
        #print(threshold)
        return c

    


        # TODO: finish this.͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
        #raise NotImplemented()

def return_your_name():
    # return your name͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
    return "Shreya"
    # TODO: finish this͏︆͏󠄃͏󠄌͏󠄍͏󠄂͏️͏︊͏󠄅͏︉
    #raise NotImplemented()
