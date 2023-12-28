
(ns components.item-list-row.prototypes
    (:require [fruits.vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-props-prototype
  ; @param (map) row-props
  ; {:border-color (keyword or string)(opt)}
  ;  :cells (vector)
  ;  :marker-color (keyword)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :cells (vector)
  ;  :marker-position (keyword)}
  [{:keys [border-color cells marker-color] :as row-props}]
  ; XXX#0561
  ; A cells vektor nil értékeket is tartalmazhat a feltételesen hozzáadott celláknál,
  ; a feltétel nem teljesülésekor!
  (merge (if border-color {:border-position :all
                           :border-width    :xxs})
         (if marker-color {:marker-position :tr})
         (-> row-props)
         {:cells (vector/remove-items-by cells nil?)}))
