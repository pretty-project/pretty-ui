
(ns pathom.api
    (:require [pathom.validator :as validator]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; pathom.validator
(def validate-data        validator/validate-data)
(def clean-validated-data validator/clean-validated-data)
(def data-valid?          validator/data-valid?)
(def data-invalid?        validator/data-invalid?)
