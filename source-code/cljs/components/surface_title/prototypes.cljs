
(ns components.surface-title.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn title-props-prototype
  ; @param (map) title-props
  ;
  ; @return (map)
  ; {}
  [title-props]
  (let [viewport-large? @(r/subscribe [:x.environment/viewport-large?])]
       (merge title-props {:font-size   (if viewport-large? :5xl :l)
                           :font-weight :extra-bold
                           :line-height :block})))
