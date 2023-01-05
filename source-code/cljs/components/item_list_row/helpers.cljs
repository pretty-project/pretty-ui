
(ns components.item-list-row.helpers)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn row-attributes
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {:border (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :drag-attributes (map)(opt)
  ;  :highlighted? (boolean)(opt)
  ;  :marked? (boolean)(opt)
  ;  :template (string)}
  ;
  ; @return (map)
  ; {:data-border (keyword)
  ;  :data-highlighted (boolean)
  ;  :data-marked (boolean)
  ;  :style (map)}
  [_ {:keys [border disabled? drag-attributes highlighted? marked? template]}]
  (if drag-attributes (-> drag-attributes (update :style merge {:grid-template-columns template})
                                          (assoc  :data-border border :data-highlighted highlighted?))
                      {:data-border      border
                       :data-highlighted highlighted?
                       :data-marked      marked?
                       :style {:grid-template-columns template :opacity (if disabled? ".5")}}))
