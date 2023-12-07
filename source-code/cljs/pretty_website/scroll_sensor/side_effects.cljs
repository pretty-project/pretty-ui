
(ns pretty-website.scroll-sensor.side-effects
    (:require [dom.api           :as dom]
              [fruits.hiccup.api :as hiccup]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sensor-did-mount-f
  ; @ignore
  ;
  ; @param (keyword) sensor-id
  ; @param (function) callback-f
  [sensor-id callback-f]
  (let [sensor-element (-> sensor-id hiccup/value dom/get-element-by-id)]
       (dom/setup-intersection-observer! sensor-element callback-f)))
