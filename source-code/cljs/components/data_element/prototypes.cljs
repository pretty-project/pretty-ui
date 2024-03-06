
(ns components.data-element.prototypes
    (:require [fruits.vector.api     :as vector]
              [multitype-content.api :as multitype-content]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-props-prototype
  ; @param (map) element-props
  ; {:value (multitype-content or multitype-contents in vector)}
  ;
  ; @return (map)
  ; {:font-size (keyword, px or string)
  ;  :value (multitype-contents in vector)}
  [{:keys [marked? value] :as element-props}]
  ; XXX#0516 (source-code/app/common/frontend/data_element/views.cljs)
  ;
  ; XXX#0510
  ; Mivel a data-element-label feliraton megjelenő {:marked? true} jelölő a value
  ; vágólapra helyezhetőségét jelzi, ezért csak akkor számít {:markable? true}
  ; állapotúnak az adat, ha vágólapra helyezhető a tartalma!
  (merge {:font-size :s}
         (-> element-props)
         (cond (vector/not-empty? value) {:marked? false :value value}
               (vector?           value) {:marked? false :value [nil]}
               :return {:value [value] :marked? (and marked? (-> value multitype-content/compose empty? not))})))
