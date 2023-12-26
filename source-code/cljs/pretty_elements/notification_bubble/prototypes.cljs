
(ns pretty-elements.notification-bubble.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-props-prototype
  ; @ignore
  ;
  ; @param (map) bubble-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [border-color] :as bubble-props}]
  (merge {:font-size   :s
          :font-weight :medium
          :selectable? false
          :text-color  :default}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> bubble-props)))
