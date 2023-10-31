
(ns components.notification-bubble.prototypes)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) button-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [{:keys [layout] :as button-props}]
  (case layout :button (merge {:border-radius {:all :s}
                               :font-size     :xs
                               :hover-color   :highlight
                               :indent        {:horizontal :xxs :vertical :xs}}
                              (-> button-props))
                       (merge {:border-radius {:all :s}
                               :hover-color   :highlight}
                              (-> button-props))))

(defn bubble-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  ; @param (map) bubble-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [bubble-id {:keys [primary-button secondary-button] :as bubble-props}]
  (merge {:border-color  :secondary
          :border-radius {:all :m}
          :border-width  :xs
          :fill-color    :default
          :min-width     :m
          :indent        {:horizontal :xs :vertical :xs}
          :outdent       {:bottom     :xs :vertical :xs}
          :primary-button {:border-radius {:all :s}
                           :icon          :close
                           :hover-color   :highlight
                           :on-click      [:x.ui/remove-bubble! bubble-id]}}
         (-> bubble-props)
         (if primary-button   {:primary-button   (button-props-prototype primary-button)})
         (if secondary-button {:secondary-button (button-props-prototype secondary-button)})))
