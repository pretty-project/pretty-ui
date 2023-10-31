
(ns components.side-menu.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-props-prototype
  ; @param (map) menu-props
  ; {:border-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color] :as menu-props}]
  (merge (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> menu-props)))
