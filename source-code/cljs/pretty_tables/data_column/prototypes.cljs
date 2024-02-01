
(ns pretty-tables.data-column.prototypes
    (:require [fruits.css.api :as css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-props-prototype
  ; @ignore
  ;
  ; @param (keyword) column-id
  ; @param (map) column-props
  ; {:cells (maps in vector)(opt)}
  ;
  ; @return (map)
  ; {:template (string)
  ;  :width (keyword, px or string)}
  [_ {:keys [cells] :as column-props}]
  (merge {;:template (css/repeat (count cells) (css/fr 1))
          :grid-column-template :even
          :grid-column-count (count cells)}
          ;:width :s}
         (-> column-props)))
