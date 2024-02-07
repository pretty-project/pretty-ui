
(ns pretty-elements.notification-bubble.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-props-prototype
  ; @ignore
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [border-color] :as bubble-props}]
  (merge {:font-size   :s
          :font-weight :medium
          :text-color  :default}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> bubble-props)))
         ; text unselectable
