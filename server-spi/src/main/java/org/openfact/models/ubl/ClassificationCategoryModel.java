package org.openfact.models.ubl;

import org.openfact.models.ubl.type.*;

/**
 * A class to define a category within a classification scheme.
 * 
 * @author Erik
 * @version 2.0
 * @created 07-Set.-2016 9:13:31 a. m.
 */
public class ClassificationCategoryModel {

    /**
     * The value of a code used to identify this category within the
     * classification scheme.
     */
    private TextModel codeValue;
    /**
     * Text describing this category.
     */
    private TextModel description;
    /**
     * The name of this category within the classification scheme.
     */
    private NameModel name;
    private ClassificationCategoryModel categorizesClassificationCategory;

}
