
(ns pretty-elements.notification-bubble.prototypes
    (:require [pretty-css.api :as pretty-css]))

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
          :text-color  :default}
         (if border-color {:border-position :all
                           :border-width    :xxs})
         (-> bubble-props)))
