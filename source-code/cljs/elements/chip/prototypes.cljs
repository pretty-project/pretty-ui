
(ns elements.chip.prototypes
    (:require [noop.api :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn chip-props-prototype
  ; @ignore
  ;
  ; @param (map) chip-props
  ; {:icon (keyword)(opt)
  ;  :primary-button (map)(opt)}
  ;
  ; @return (map)
  ; {:color (keyword or string)
  ;  :fill-color (keyword or string)
  ;  :icon-family (keyword)
  ;  :primary-button (map)
  ;   {:icon-family (keyword)}
  ;  :width (keyword)}
  [{:keys [icon primary-button] :as chip-props}]
  (merge {:color      :default
          :fill-color :primary
          :width      :content}
         (if icon {:icon-family :material-symbols-outlined})
         (param chip-props)
         (if primary-button {:primary-button (merge {:icon-family :material-symbols-outlined}
                                                    (param primary-button))})))
