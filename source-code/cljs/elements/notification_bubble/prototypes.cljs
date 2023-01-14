
(ns elements.notification-bubble.prototypes
    (:require [candy.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bubble-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
          :selectable? false}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (param bubble-props)))
