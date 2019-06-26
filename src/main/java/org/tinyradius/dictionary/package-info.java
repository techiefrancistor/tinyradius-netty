/**
 * This package contains interfaces and classes that manage
 * a dictionary of Radius attribute types.
 * <p>
 * Each attribute type is represented by an instance of the
 * object <code>AttributeType</code>. This class stores the
 * type code, the type name and the vendor ID for each
 * attribute type.
 * <p>
 * The interface <code>Dictionary</code> declares the methods necessary
 * to access a dictionary. Every class that implements this
 * interface can be used to resolve AttributeType objects.
 * <p>
 * Dictionaries that can be changed after construction should
 * implement the interface <code>WritableDictionary</code>.
 * You can use the class <code>DictionaryParser</code>
 * to populate <code>WritableDictionary</code> objects with
 * data from a dictionary file in the well-known Radiator
 * dictionary file format.
 * <p>
 * <code>MemoryDictionary</code> is the default implementation
 * of <code>WritableDictionary</code>. It manages a dictionary held
 * in the RAM. This class is used together with the
 * <code>DictionaryParser</code> by the class <code>DefaultDictionary</code>
 * which is a singleton object containing the default dictionary
 * that is used if there is no dictionary explicitly specified.
 * The <code>DefaultDictionary</code> class is initialised from the
 * classpath resource
 * <code>org.tinyradius.dictionary.default_dictionary</code>.
 */
package org.tinyradius.dictionary;