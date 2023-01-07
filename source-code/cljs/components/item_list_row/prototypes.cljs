
(ns components.item-list-row.prototypes
    (:require [candy.api  :refer [param]]
              [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @param (map) row-props
  ; {:cells (vector)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:cells (vector)
  ;  :marker-position (keyword)}
  [{:keys [cells marker-color] :as row-props}]
  ; XXX#0561
  ; A cells vektor nil értékeket is tartalmazhat a feltételesen hozzáadott celláknál,
  ; a feltétel nem teljesülésekor!
  (merge (if marker-color {:marker-position :tr})
         (param row-props)
         {:cells (vector/remove-items-by cells nil?)}))
