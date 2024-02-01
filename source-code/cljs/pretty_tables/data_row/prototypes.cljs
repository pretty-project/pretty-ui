
(ns pretty-tables.data-row.prototypes
    (:require [fruits.css.api :as css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @ignore
  ;
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:cells (maps in vector)(opt)}
  ;
  ; @return (map)
  ; {:template (string)
  ;  :width (keyword, px or string)}
  [_ {:keys [cells] :as row-props}]
  (merge {;:template (css/repeat (count cells) (css/fr 1))
          :grid-row-template :even
          :grid-row-count (count cells)}
          ;:width :s}
         (-> row-props)))
