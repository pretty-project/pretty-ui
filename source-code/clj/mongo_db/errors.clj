
(ns mongo-db.errors)



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def MISSING-NAMESPACE-ERROR "Document must be a namespaced map with keyword type keys")

; @constant (string)
(def MISSING-DOCUMENT-ORDER-ERROR "Missing document order error")

; @constant (string)
(def DOCUMENT-DOES-NOT-EXISTS-ERROR "Document does not exists error")

; @constant (string)
(def DOCUMENT-CORRUPTED-ERROR "Document corrupted error")

; @constant (string)
(def UPDATING-DOCUMENTS-ORDER-FAILURE "Updating documents order failed")
