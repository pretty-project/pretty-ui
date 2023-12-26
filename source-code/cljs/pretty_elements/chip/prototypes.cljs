
(ns pretty-elements.chip.prototypes
    (:require [fruits.noop.api :refer [return]]))

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
  ; {:border-radius (map)
  ;  :content-value-f (function)
  ;  :icon-family (keyword)
  ;  :primary-button (map)
  ;   {:icon-family (keyword)}
  ;  :text-color (keyword or string)
  ;  :width (keyword)}
  [{:keys [icon primary-button] :as chip-props}]
  (merge {:content-value-f  return
          :text-color       :default
          :width            :content}
         (if icon           {:icon-family :material-symbols-outlined})
         (-> chip-props)
         (if primary-button {:primary-button (merge {:icon-family :material-symbols-outlined} primary-button)})))
