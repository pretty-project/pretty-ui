
(ns components.popup-label-bar.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn bar-props-prototype
  ; @param (map) bar-props
  ; {:label (map)(opt)
  ;  :primary-button (map)(opt)
  ;  :secondary-button (map)(opt)}
  ;
  ; @return (map)
  ; {:label (map)
  ;  :primary-button (map)
  ;  :secondary-button (map)}
  [{:keys [label primary-button secondary-button] :as bar-props}]
  (merge bar-props (if label            {:label            (merge {:indent {:all :xxs}}
                                                                  (param label))})
                   (if primary-button   {:primary-button   (merge {:color       :primary
                                                                   :font-size   :xs
                                                                   :hover-color :highlight
                                                                   :indent      {:all :xxs}
                                                                   :outdent     {:all :xxs}
                                                                   :keypress    {:key-code 13}}
                                                                  (param primary-button))})
                   (if secondary-button {:secondary-button (merge {:font-size   :xs
                                                                   :hover-color :highlight
                                                                   :indent      {:all :xxs}
                                                                   :outdent     {:all :xxs}
                                                                   :keypress    {:key-code 27}}
                                                                  (param secondary-button))})))
