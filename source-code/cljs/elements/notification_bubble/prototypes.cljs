
(ns elements.notification-bubble.prototypes)

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
  (merge {:color       :default
          :font-size   :s
          :font-weight :medium
          :height      :auto
          :selectable? false
          :width       :content}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> bubble-props)))
