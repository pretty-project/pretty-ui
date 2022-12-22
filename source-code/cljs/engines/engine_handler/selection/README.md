
### The items are selected by its id-s

###### XXX#8891

Until the x.4.7.5 version, those engines which handle multiple item selections,
stored the selected items' indexes because it was easier to handle the indexes
instead of the id-s.

From the x4.7.5 version, those engines which handle multiple item selections,
store the id-s because it makes possible to handle more complex selection methods.

E.g. It's necessary for a file browser to be capable of select items in different
     folders and its only feasible when the items are selected by it's id-s.
