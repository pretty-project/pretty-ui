
(ns pretty-inputs.core.utils
    (:require [fruits.keyword.api :as keyword]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-id->subitem-id
  ; @ignore
  ;
  ; @param (keyword) input-id
  ;
  ; @usage
  ; (input-id->button-id :my-input :my-subitem)
  ; =>
  ; :my-input--my-subitem
  ;
  ; @return (keyword)
  [input-id subitem-id]
  (keyword/append input-id subitem-id "--"))
