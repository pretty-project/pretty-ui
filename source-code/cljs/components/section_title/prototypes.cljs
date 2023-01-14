
(ns components.section-title.prototypes
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-props-prototype
  ; @param (map) title-props
  ;
  ; @return (map)
  ; {:font-size (keyword)
  ;  :font-weight (keyword)}
  [title-props]
  (let [viewport-min? @(r/subscribe [:x.environment/viewport-min? 720])]
       (merge title-props {:font-size (if viewport-min? :5xl :xl)
                           :font-weight :bold})))